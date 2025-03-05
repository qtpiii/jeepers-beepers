package net.qtpi.jeepersbeepers.block.entity;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.qtpi.jeepersbeepers.block.BeeperHiveBlock;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import net.qtpi.jeepersbeepers.registry.BlockEntityRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BeeperHiveBlockEntity extends BlockEntity {
    private static final List<String> IGNORED_BEE_TAGS = Arrays.asList("Air", "ArmorDropChances", "ArmorItems", "Brain", "CanPickUpLoot", "DeathTime", "FallDistance", "FallFlying", "Fire", "HandDropChances", "HandItems", "HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation", "CannotEnterHiveTicks", "TicksSincePollination", "CropsGrownSincePollination", "HivePos", "Passengers", "Leash", "UUID");
    private final List<BeeperHiveBlockEntity.BeeperData> stored = Lists.newArrayList();
    @Nullable
    private BlockPos savedFlowerPos;

    public BeeperHiveBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.BEEPER_HIVE_BLOCK_ENTITY, pos, blockState);
    }

    public void setChanged() {
        if (this.isFireNearby()) {
            this.emptyAllLivingFromHive((Player)null, this.level.getBlockState(this.getBlockPos()), BeeperHiveBlockEntity.BeeperReleaseStatus.EMERGENCY);
        }

        super.setChanged();
    }

    public boolean isFireNearby() {
        if (this.level == null) {
            return false;
        } else {
            for(BlockPos blockPos : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(1, 1, 1))) {
                if (this.level.getBlockState(blockPos).getBlock() instanceof FireBlock) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isEmpty() {
        return this.stored.isEmpty();
    }

    public boolean isFull() {
        return this.stored.size() == 3;
    }

    public void emptyAllLivingFromHive(@Nullable Player player, BlockState state, BeeperHiveBlockEntity.BeeperReleaseStatus releaseStatus) {
        List<Entity> list = this.releaseAllOccupants(state, releaseStatus);
        if (player != null) {
            for(Entity entity : list) {
                if (entity instanceof BeeperEntity) {
                    BeeperEntity beeper = (BeeperEntity)entity;
                    if (player.position().distanceToSqr(entity.position()) <= (double)16.0F) {
                        if (!this.isSedated()) {
                            beeper.setTarget(player);
                        } else {
                            beeper.setStayOutOfHiveCountdown(400);
                        }
                    }
                }
            }
        }

    }

    private List<Entity> releaseAllOccupants(BlockState state, BeeperHiveBlockEntity.BeeperReleaseStatus releaseStatus) {
        List<Entity> list = Lists.newArrayList();
        this.stored.removeIf((beeperData) -> releaseOccupant(this.level, this.worldPosition, state, beeperData, list, releaseStatus, this.savedFlowerPos));
        if (!list.isEmpty()) {
            super.setChanged();
        }

        return list;
    }

    public void addOccupant(Entity occupant, boolean hasNectar, boolean isNaked) {
        this.addOccupantWithPresetTicks(occupant, hasNectar, isNaked, 0);
    }

    @VisibleForDebug
    public int getOccupantCount() {
        return this.stored.size();
    }

    public static int getHoneyLevel(BlockState state) {
        return (Integer)state.getValue(BeeperHiveBlock.HONEY_LEVEL);
    }

    @VisibleForDebug
    public boolean isSedated() {
        return CampfireBlock.isSmokeyPos(this.level, this.getBlockPos());
    }

    public void addOccupantWithPresetTicks(Entity occupant, boolean hasNectar, boolean isNaked, int ticksInHive) {
        if (this.stored.size() < 3) {
            occupant.stopRiding();
            occupant.ejectPassengers();
            CompoundTag compoundTag = new CompoundTag();
            occupant.save(compoundTag);
            this.storeBeeper(compoundTag, ticksInHive, hasNectar, isNaked);
            if (this.level != null) {
                if (occupant instanceof BeeperEntity beeper) {
                    if (beeper.hasSavedFlowerPos() && (!this.hasSavedFlowerPos() || this.level.random.nextBoolean())) {
                        this.savedFlowerPos = beeper.getSavedCropPos();
                    }
                }

                BlockPos blockPos = this.getBlockPos();
                this.level.playSound((Player)null, (double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(occupant, this.getBlockState()));
            }

            occupant.discard();
            super.setChanged();
        }
    }

    public void storeBeeper(CompoundTag entityData, int ticksInHive, boolean hasNectar, boolean isNaked) {
        this.stored.add(new BeeperHiveBlockEntity.BeeperData(entityData, ticksInHive, hasNectar ? 2400 : (isNaked ? 1200 : 600), isNaked));
    }

    private static boolean releaseOccupant(Level level, BlockPos pos, BlockState state, BeeperHiveBlockEntity.BeeperData data, @Nullable List<Entity> storedInHives, BeeperHiveBlockEntity.BeeperReleaseStatus releaseStatus, @Nullable BlockPos savedFlowerPos) {
        if ((level.isNight() || level.isRaining()) && releaseStatus != BeeperHiveBlockEntity.BeeperReleaseStatus.EMERGENCY) {
            return false;
        } else {
            CompoundTag compoundTag = data.entityData.copy();
            removeIgnoredBeeperTags(compoundTag);
            compoundTag.put("HivePos", NbtUtils.writeBlockPos(pos));
            compoundTag.putBoolean("NoGravity", true);
            Direction direction = (Direction)state.getValue(BeeperHiveBlock.FACING);
            BlockPos blockPos = pos.relative(direction);
            boolean bl = !level.getBlockState(blockPos).getCollisionShape(level, blockPos).isEmpty();
            if (bl && releaseStatus != BeeperHiveBlockEntity.BeeperReleaseStatus.EMERGENCY) {
                return false;
            } else {
                Entity entity = EntityType.loadEntityRecursive(compoundTag, level, (entityx) -> entityx);
                if (entity != null) {
                    if (!entity.getType().is(TagRegistry.Misc.BEEPER_HOME_INHABITORS)) {
                        return false;
                    } else {
                        if (entity instanceof BeeperEntity beeper) {
                            if (savedFlowerPos != null && !beeper.hasSavedFlowerPos() && level.random.nextFloat() < 0.9F) {
                                beeper.setSavedCropPos(savedFlowerPos);
                            }

                            if (releaseStatus == BeeperHiveBlockEntity.BeeperReleaseStatus.HONEY_DELIVERED) {
                                beeper.dropOffNectar();
                                if (state.is(TagRegistry.Blocks.BEEPER_HIVES, (blockStateBase) -> blockStateBase.hasProperty(BeeperHiveBlock.HONEY_LEVEL))) {
                                    int i = getHoneyLevel(state);
                                    if (i < 5) {
                                        int j = level.random.nextInt(100) == 0 ? 2 : 1;
                                        if (i + j > 5) {
                                            --j;
                                        }

                                        level.setBlockAndUpdate(pos, (BlockState)state.setValue(BeeperHiveBlock.HONEY_LEVEL, i + j));
                                    }
                                }
                            }

                            if (beeper.isNaked()) {
                                beeper.regrowFluff();
                            }

                            setBeeperReleaseData(data.ticksInHive, beeper);
                            if (storedInHives != null) {
                                storedInHives.add(beeper);
                            }

                            float f = entity.getBbWidth();
                            double d = bl ? (double)0.0F : 0.55 + (double)(f / 2.0F);
                            double e = (double)pos.getX() + (double)0.5F + d * (double)direction.getStepX();
                            double g = (double)pos.getY() + (double)0.5F - (double)(entity.getBbHeight() / 2.0F);
                            double h = (double)pos.getZ() + (double)0.5F + d * (double)direction.getStepZ();
                            entity.moveTo(e, g, h, entity.getYRot(), entity.getXRot());
                        }

                        level.playSound((Player)null, pos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, level.getBlockState(pos)));
                        return level.addFreshEntity(entity);
                    }
                } else {
                    return false;
                }
            }
        }
    }

    static void removeIgnoredBeeperTags(CompoundTag tag) {
        for(String string : IGNORED_BEE_TAGS) {
            tag.remove(string);
        }

    }

    private static void setBeeperReleaseData(int timeInHive, BeeperEntity beeper) {
        int i = beeper.getAge();
        if (i < 0) {
            beeper.setAge(Math.min(0, i + timeInHive));
        } else if (i > 0) {
            beeper.setAge(Math.max(0, i - timeInHive));
        }

        beeper.setInLoveTime(Math.max(0, beeper.getInLoveTime() - timeInHive));
    }

    private boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    private static void tickOccupants(Level level, BlockPos pos, BlockState state, List<BeeperHiveBlockEntity.BeeperData> data, @Nullable BlockPos savedFlowerPos) {
        boolean bl = false;

        BeeperHiveBlockEntity.BeeperData beeperData;
        for(Iterator<BeeperHiveBlockEntity.BeeperData> iterator = data.iterator(); iterator.hasNext(); ++beeperData.ticksInHive) {
            beeperData = (BeeperHiveBlockEntity.BeeperData)iterator.next();
            if (beeperData.ticksInHive > beeperData.minOccupationTicks) {
                BeeperHiveBlockEntity.BeeperReleaseStatus beeReleaseStatus = beeperData.entityData.getBoolean("HasNectar") ? BeeperHiveBlockEntity.BeeperReleaseStatus.HONEY_DELIVERED : BeeperHiveBlockEntity.BeeperReleaseStatus.BEEPER_RELEASED;
                if (releaseOccupant(level, pos, state, beeperData, (List<Entity>)null, beeReleaseStatus, savedFlowerPos)) {
                    bl = true;
                    iterator.remove();
                }
            }
        }

        if (bl) {
            setChanged(level, pos, state);
        }

    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, BeeperHiveBlockEntity beehive) {
        tickOccupants(level, pos, state, beehive.stored, beehive.savedFlowerPos);
        if (!beehive.stored.isEmpty() && level.getRandom().nextDouble() < 0.005) {
            double d = (double)pos.getX() + (double)0.5F;
            double e = (double)pos.getY();
            double f = (double)pos.getZ() + (double)0.5F;
            level.playSound((Player)null, d, e, f, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    public void load(CompoundTag tag) {
        super.load(tag);
        this.stored.clear();
        ListTag listTag = tag.getList("Beepers", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag = listTag.getCompound(i);
            BeeperHiveBlockEntity.BeeperData beeperData = new BeeperHiveBlockEntity.BeeperData(compoundTag.getCompound("EntityData"), compoundTag.getInt("TicksInHive"), compoundTag.getInt("MinOccupationTicks"), compoundTag.getBoolean("isNaked"));
            this.stored.add(beeperData);
        }

        this.savedFlowerPos = null;
        if (tag.contains("FlowerPos")) {
            this.savedFlowerPos = NbtUtils.readBlockPos(tag.getCompound("FlowerPos"));
        }

    }

    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Beepers", this.writeBeepers());
        if (this.hasSavedFlowerPos()) {
            tag.put("FlowerPos", NbtUtils.writeBlockPos(this.savedFlowerPos));
        }

    }

    public ListTag writeBeepers() {
        ListTag listTag = new ListTag();

        for(BeeperHiveBlockEntity.BeeperData beeperData : this.stored) {
            CompoundTag compoundTag = beeperData.entityData.copy();
            compoundTag.remove("UUID");
            CompoundTag compoundTag2 = new CompoundTag();
            compoundTag2.put("EntityData", compoundTag);
            compoundTag2.putInt("TicksInHive", beeperData.ticksInHive);
            compoundTag2.putInt("MinOccupationTicks", beeperData.minOccupationTicks);
            listTag.add(compoundTag2);
        }

        return listTag;
    }

    public static enum BeeperReleaseStatus {
        HONEY_DELIVERED,
        BEEPER_RELEASED,
        EMERGENCY;

        private BeeperReleaseStatus() {
        }
    }

    static class BeeperData {
        final CompoundTag entityData;
        int ticksInHive;
        final int minOccupationTicks;
        boolean isNaked;

        BeeperData(CompoundTag entityData, int ticksInHive, int minOccupationTicks, boolean isNaked) {
            BeeperHiveBlockEntity.removeIgnoredBeeperTags(entityData);
            this.entityData = entityData;
            this.ticksInHive = ticksInHive;
            this.minOccupationTicks = minOccupationTicks;
            this.isNaked = isNaked;
        }
    }
}
