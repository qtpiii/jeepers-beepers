package net.qtpi.jeepersbeepers.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.block.entity.BeeperHiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.qtpi.jeepersbeepers.registry.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeeperEntity extends Animal implements GeoEntity, NeutralMob, FlyingAnimal, Shearable {
    private static final EntityDataAccessor<Byte> DATA_WOOL_ID;
    private static final Map<DyeColor, Object> ITEM_BY_DYE;
    private static final EnumMap<DyeColor, Object> COLORARRAY_BY_COLOR;
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    private static final EntityDataAccessor<BlockPos> DATA_SAVED_CROP_POS;
    private static final EntityDataAccessor<Boolean> DATA_TAME;
    private static final EntityDataAccessor<Boolean> DATA_WILD;
    private static final UniformInt PERSISTENT_ANGER_TIME;
    @Nullable
    private UUID persistentAngerTarget;
    private float rollAmount;
    private float rollAmountO;
    private int timeSinceSting;
    int ticksWithoutNectarSinceExitingHive;
    private int stayOutOfHiveCountdown;
    private int numCropsGrownSincePollination;
    int remainingCooldownBeforeLocatingNewHive;
    int remainingCooldownBeforeLocatingNewFlower;
    int remainingCooldownBeforeNextSneeze;
    boolean readyToSneeze;
    @Nullable
    BlockPos savedCropPos;
    @Nullable
    BlockPos hivePos;
    Block savedCrop;
    BeeperEntity.BeeperPollinateGoal beePollinateGoal;
    BeeperEntity.BeeperGoToHiveGoal goToHiveGoal;
    private BeeperEntity.BeeperGoToKnownFlowerGoal goToKnownFlowerGoal;
    private int underWaterTicks;

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.beeper.idle");
    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("animation.beeper.fly");
    protected static final RawAnimation SNEEZE_ANIM = RawAnimation.begin().thenPlay("animation.beeper.sneeze");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public static int getColorArray(DyeColor dyeColor) {
        return (int) COLORARRAY_BY_COLOR.get(dyeColor);
    }

    public BeeperEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.lookControl = new BeeperEntity.BeeperLookControl(this, this);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>(this, "flyController", 0, this::flyController));
        controllerRegistrar.add(new AnimationController<>(this, "sneezeController", 10, state -> PlayState.STOP)
                .triggerableAnim("sneeze", SNEEZE_ANIM).setParticleKeyframeHandler(particleKeyframeEvent -> {
                    for (int i = 0; i < random.nextInt(5) + 4; ++i) {
                        level().addParticle(ParticleTypes.POOF, getX(), getY(), getZ(), random.nextGaussian() * 0.05, random.nextGaussian() * 0.025, random.nextGaussian() * 0.05);
                    }
                    for (int i = 0; i < random.nextInt(3) + 6; ++i) {
                        level().addParticle(ParticleRegistry.BEEPER_SNEEZE_POOF, getX(), getY(), getZ(), random.nextGaussian() * 0.15, random.nextGaussian() * 0.15, random.nextGaussian() * 0.15);
                    }
                }).setSoundKeyframeHandler(soundKeyframeEvent -> {
                    Player player = ClientUtils.getClientPlayer();
                    SoundEvent sound;
                    if (level().random.nextFloat() < 0.00001) {
                        sound = SoundRegistry.OPTIMUS_PRIME;
                    } else {
                        sound = SoundRegistry.BEEPER_SNEEZE;
                    }
                    level().playSound(player, BeeperEntity.this, sound, SoundSource.NEUTRAL, 1.0F, 1.0F);
                }));
    }

    private PlayState flyController(AnimationState<BeeperEntity> beeperEntityAnimationState) {
        beeperEntityAnimationState.getController().setAnimation(FLY_ANIM);
        return PlayState.CONTINUE;
    }

    private PlayState predicate(AnimationState<BeeperEntity> beeperEntityAnimationState) {
        beeperEntityAnimationState.getController().setAnimation(IDLE_ANIM);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
        this.entityData.define(DATA_WOOL_ID, (byte) 0);
        this.entityData.define(DATA_SAVED_CROP_POS, new BlockPos(0, 0, 0));
        this.entityData.define(DATA_TAME, false);
        this.entityData.define(DATA_WILD, false);
    }

    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeeperEntity.BeeperAttackGoal(this, this, (double) 1.4F, true));
        this.goalSelector.addGoal(0, new BeeperEntity.WildBeeperNearestAttackableTargetGoal(this, Player.class, true));
        this.goalSelector.addGoal(1, new BeeperEntity.BeeperEnterHiveGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, (double) 1.0F));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double) 1.25F, Ingredient.of(TagRegistry.Items.BEEPER_FOOD), false));
        this.beePollinateGoal = new BeeperEntity.BeeperPollinateGoal(this);
        this.goalSelector.addGoal(4, this.beePollinateGoal);
        this.goalSelector.addGoal(5, new FollowParentGoal(this, (double) 1.25F));
        this.goalSelector.addGoal(5, new BeeperEntity.BeeperSneezeGoal(this));
        this.goalSelector.addGoal(5, new BeeperEntity.BeeperLocateHiveGoal(this));
        this.goToHiveGoal = new BeeperEntity.BeeperGoToHiveGoal(this);
        this.goalSelector.addGoal(5, this.goToHiveGoal);
        this.goToKnownFlowerGoal = new BeeperEntity.BeeperGoToKnownFlowerGoal(this);
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
        this.goalSelector.addGoal(7, new BeeperEntity.BeeperGrowCropGoal(this));
        this.goalSelector.addGoal(8, new BeeperEntity.BeeperWanderGoal(this));
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.targetSelector.addGoal(1, (new BeeperEntity.BeeperHurtByOtherGoal(this, this)).setAlertOthers(new Class[0]));
        this.targetSelector.addGoal(2, new BeeperEntity.BeeperBecomeAngryTargetGoal(this));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (this.hasHive()) {
            compound.put("HivePos", NbtUtils.writeBlockPos(this.getHivePos()));
        }

        if (this.hasSavedFlowerPos()) {
            compound.put("FlowerPos", NbtUtils.writeBlockPos(this.getSavedCropPos()));
        }

        compound.putByte("Color", (byte) this.getColor().getId());
        compound.putBoolean("HasNectar", this.hasNectar());
        compound.putBoolean("HasStung", this.hasStung());
        compound.putBoolean("IsNaked", this.isNaked());
        compound.putBoolean("HasColor", this.hasColor());
        compound.putBoolean("IsWild", this.isWild());
        compound.putBoolean("IsTame", this.isTame());
        compound.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
        compound.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
        compound.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);

        this.addPersistentAngerSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.hivePos = null;
        if (compound.contains("HivePos")) {
            this.hivePos = NbtUtils.readBlockPos(compound.getCompound("HivePos"));
        }

        this.savedCropPos = null;
        if (compound.contains("FlowerPos")) {
            this.savedCropPos = NbtUtils.readBlockPos(compound.getCompound("FlowerPos"));
            savedCrop = level().getBlockState(savedCropPos).getBlock();
            entityData.set(DATA_SAVED_CROP_POS, savedCropPos);
        }

        if (compound.contains("Color")) {
            this.setColor(DyeColor.byId(compound.getByte("Color")));
        }

        this.setHasNectar(compound.getBoolean("HasNectar"));
        this.setHasStung(compound.getBoolean("HasStung"));
        this.setIsNaked(compound.getBoolean("IsNaked"));
        this.setHasColor(compound.getBoolean("HasColor"));
        if (compound.contains("IsWild")) {
            this.setIsWild(compound.getBoolean("IsWild"));
        }
        if (compound.contains("IsTame")) {
            this.setIsTame(compound.getBoolean("IsTame"));
        }
        this.ticksWithoutNectarSinceExitingHive = compound.getInt("TicksSincePollination");
        this.stayOutOfHiveCountdown = compound.getInt("CannotEnterHiveTicks");
        this.numCropsGrownSincePollination = compound.getInt("CropsGrownSincePollination");
        this.readPersistentAngerSaveData(this.level(), compound);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_WOOL_ID) & 15);
    }

    public void setColor(DyeColor dyeColor) {
        setHasColor(true);
        byte b = this.entityData.get(DATA_WOOL_ID);
        this.entityData.set(DATA_WOOL_ID, (byte) (b & 240 | dyeColor.getId() & 15));
    }

    public boolean doHurtTarget(Entity target) {
        Set<Item> entityWornArmor = new ObjectOpenHashSet<>();
        boolean bypassArmor = false;
        for (ItemStack stack : target.getArmorSlots()) {
            entityWornArmor.add(stack.getItem());
        }
        if (this.isWild() && level().random.nextFloat() > 0.75) {
            bypassArmor = true;
        }
        if (entityWornArmor.containsAll(ObjectArrayList.of(
                ItemRegistry.BEEKEEPER_BOOTS,
                ItemRegistry.BEEKEEPER_PANTS,
                ItemRegistry.BEEKEEPER_TUNIC,
                ItemRegistry.BEEKEEPER_HAT)) && !bypassArmor) {
            return false;
        } else {
            boolean bl = target.hurt(this.damageSources().sting(this), (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
            if (bl) {
                this.doEnchantDamageEffects(this, target);
                if (target instanceof LivingEntity) {
                    ((LivingEntity) target).setStingerCount(((LivingEntity) target).getStingerCount() + 1);
                    int i = 0;
                    if (this.level().getDifficulty() == Difficulty.NORMAL) {
                        i = 10;
                    } else if (this.level().getDifficulty() == Difficulty.HARD) {
                        i = 18;
                    }

                    if (i > 0) {
                        ((LivingEntity) target).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0), this);
                    }
                }

                this.setHasStung(true);
                this.stopBeingAngry();
                this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
            }

            return bl;
        }
    }

    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for (int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - (double) 0.3F, this.getX() + (double) 0.3F, this.getZ() - (double) 0.3F, this.getZ() + (double) 0.3F, this.getY((double) 0.5F), ParticleTypes.FALLING_NECTAR);
            }
        }

        this.updateRollAmount();
    }

    private void spawnFluidParticle(Level level, double startX, double endX, double startZ, double endZ, double posY, ParticleOptions particleOption) {
        level.addParticle(particleOption, Mth.lerp(level.random.nextDouble(), startX, endX), posY, Mth.lerp(level.random.nextDouble(), startZ, endZ), (double) 0.0F, (double) 0.0F, (double) 0.0F);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS)) {
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                if (this.isWild()) {
                    this.setTarget(player);
                }
                this.gameEvent(GameEvent.SHEAR, player);
                itemStack.hurtAndBreak(1, player, (playerx) -> playerx.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else if (itemStack.is(Items.WATER_BUCKET)) {
            if (!this.level().isClientSide && this.hasColor()) {
                this.setHasColor(false);
                itemStack.shrink(1);
                this.level().playSound(null, this, SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (itemStack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                } else if (!player.getInventory().add(new ItemStack(Items.BUCKET))) {
                    player.drop(new ItemStack(Items.BUCKET), false);
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else if (itemStack.is(Items.FEATHER)) {
            if (!this.level().isClientSide && this.hasNectar()) {
                this.sneeze();
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else if (itemStack.is(Items.GUNPOWDER)) {
            if (!this.level().isClientSide) {
                this.setIsWild(true);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else if (itemStack.is(ItemRegistry.SPICY_HONEYCOMB)) {
            if (!this.level().isClientSide) {
                if (this.isWild() && !this.isTame()) {
                    this.setIsTame(true);
                    this.setPersistentAngerTarget(null);
                    this.setRemainingPersistentAngerTime(0);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.CONSUME;
                }
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
                return super.mobInteract(player, hand);
        }
    }

    public void sneeze() {
        triggerAnim("sneezeController", "sneeze");
        if (BeeperEntity.this.savedCrop != null) {
            this.remainingCooldownBeforeNextSneeze = 100;
            new Thread(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep(2100);
                } catch (InterruptedException ignore) {
                }
                PollenCloud cloud = new PollenCloud(null, level());
                cloud.setPos(BeeperEntity.this.position());
                cloud.sourceCropTags = level().getBlockState(entityData.get(DATA_SAVED_CROP_POS)).getTags().collect(Collectors.toCollection(ArrayList::new));
                cloud.sourceCropPos = savedCropPos;
                level().addFreshEntity(cloud);
            }).start();
        }
    }

    public void shear(@NotNull SoundSource source) {
        this.level().playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.setIsNaked(true);
        if (this.hasNectar()) {
            int i = 1 + this.random.nextInt(2);

            for(int j = 0; j < i; ++j) {
                ItemEntity puffItem = this.spawnAtLocation(ItemRegistry.POLLEN_PUFF, 1);
                if (puffItem != null) {
                    CompoundTag tag = new CompoundTag();
                    if (savedCrop == Blocks.ATTACHED_MELON_STEM) {
                        tag.putString("pollenPlantName", Blocks.MELON_STEM.defaultBlockState().getBlock().asItem().getDescriptionId());
                    }
                    else if (savedCrop == Blocks.ATTACHED_PUMPKIN_STEM) {
                        tag.putString("pollenPlantName", Blocks.PUMPKIN_STEM.defaultBlockState().getBlock().asItem().getDescriptionId());
                    }
                    else {
                        tag.putString("pollenPlantName", savedCrop.defaultBlockState().getBlock().asItem().getDescriptionId());
                    }

                    ArrayList<TagKey<Block>> tags = savedCrop.defaultBlockState().getTags().collect(Collectors.toCollection(ArrayList::new)); // i have the java ecosystem so much this sucks balls
                    JeepersBeepers.LOGGER.info(tags.toString());
                    tag.putIntArray("cropTags", TagRegistry.Blocks.cropTagListToIntegerList(tags));
                    tag.put("cropPos", NbtUtils.writeBlockPos(savedCropPos));
                    puffItem.getItem().setTag(tag);
                    puffItem.setDeltaMovement(puffItem.getDeltaMovement().add(((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (this.random.nextFloat() * 0.05F), ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
                }
            }

            this.setHasNectar(false);
            return;
        }
        int i = 1 + this.random.nextInt(2);

        for(int j = 0; j < i; ++j) {
            if (hasColor()) {
                ItemEntity itemEntity = this.spawnAtLocation((ItemLike)ITEM_BY_DYE.get(this.getColor()), 1);
                if (itemEntity != null) {
                    itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (this.random.nextFloat() * 0.05F), ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
                }
            } else {
                ItemEntity itemEntity = this.spawnAtLocation(BlockRegistry.BEEPER_FLUFF_BLOCK, 1);
                if (itemEntity != null) {
                    itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add(((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (this.random.nextFloat() * 0.05F), ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
                }
            }
        }
    }

    public boolean readyForShearing() {
        return this.isAlive() && !this.isNaked() && !this.isBaby();
    }

    void pathfindRandomlyTowards(BlockPos pos) {
        Vec3 vec3 = Vec3.atBottomCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int)vec3.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3 vec32 = AirRandomPos.getPosTowards(this, k, l, i, vec3, (double)((float)Math.PI / 10F));
        if (vec32 != null) {
            this.navigation.setMaxVisitedNodesMultiplier(0.5F);
            this.navigation.moveTo(vec32.x, vec32.y, vec32.z, (double)1.0F);
        }
    }

    @Nullable
    public BlockPos getSavedCropPos() {
        return this.savedCropPos;
    }

    public boolean hasSavedFlowerPos() {
        return this.savedCropPos != null;
    }

    public void setSavedCropPos(@Nullable BlockPos savedCropPos) {
        this.savedCropPos = savedCropPos;
    }

    private boolean isTiredOfLookingForNectar() {
        return this.ticksWithoutNectarSinceExitingHive > 3600;
    }

    boolean wantsToEnterHive() {
        if (this.stayOutOfHiveCountdown <= 0 && !this.beePollinateGoal.isPollinating() && !this.hasStung() && this.getTarget() == null) {
            boolean bl = this.isTiredOfLookingForNectar() || this.level().isRaining() || this.level().isNight() || this.hasNectar() || this.isNaked();
            return bl && !this.isHiveNearFire();
        } else {
            return false;
        }
    }

    public void setStayOutOfHiveCountdown(int stayOutOfHiveCountdown) {
        this.stayOutOfHiveCountdown = stayOutOfHiveCountdown;
    }

    public float getRollAmount(float partialTick) {
        return Mth.lerp(partialTick, this.rollAmountO, this.rollAmount);
    }

    private void updateRollAmount() {
        this.rollAmountO = this.rollAmount;
        if (this.isRolling()) {
            this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
        } else {
            this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
        }

    }

    protected void customServerAiStep() {
        boolean bl = this.hasStung();
        if (this.isInWaterOrBubble()) {
            ++this.underWaterTicks;
        } else {
            this.underWaterTicks = 0;
        }

        if (this.underWaterTicks > 20) {
            this.hurt(this.damageSources().drown(), 1.0F);
        }

        if (bl) {
            ++this.timeSinceSting;
            if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0) {
                this.hurt(this.damageSources().generic(), this.getHealth());
            }
        }

        if (!this.hasNectar()) {
            ++this.ticksWithoutNectarSinceExitingHive;
        }

        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level(), false);
        }

    }

    public void resetTicksWithoutNectarSinceExitingHive() {
        this.ticksWithoutNectarSinceExitingHive = 0;
    }

    private boolean isHiveNearFire() {
        if (this.hivePos == null) {
            return false;
        } else {
            BlockEntity blockEntity = this.level().getBlockEntity(this.hivePos);
            return blockEntity instanceof BeeperHiveBlockEntity && ((BeeperHiveBlockEntity)blockEntity).isFireNearby();
        }
    }

    public int getRemainingPersistentAngerTime() {
        return (Integer)this.entityData.get(DATA_REMAINING_ANGER_TIME);
    }

    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.entityData.set(DATA_REMAINING_ANGER_TIME, remainingPersistentAngerTime);
    }

    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    private boolean doesHiveHaveSpace(BlockPos hivePos) {
        BlockEntity blockEntity = this.level().getBlockEntity(hivePos);
        if (blockEntity instanceof BeeperHiveBlockEntity) {
            return !((BeeperHiveBlockEntity)blockEntity).isFull();
        } else {
            return false;
        }
    }

    @VisibleForDebug
    public boolean hasHive() {
        return this.hivePos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getHivePos() {
        return this.hivePos;
    }

    @VisibleForDebug
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }

    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.stayOutOfHiveCountdown > 0) {
                --this.stayOutOfHiveCountdown;
            }

            if (this.remainingCooldownBeforeLocatingNewHive > 0) {
                --this.remainingCooldownBeforeLocatingNewHive;
            }

            if (this.remainingCooldownBeforeLocatingNewFlower > 0) {
                --this.remainingCooldownBeforeLocatingNewFlower;
            }

            if (this.remainingCooldownBeforeNextSneeze > 0) {
                --this.remainingCooldownBeforeNextSneeze;
            }

            boolean bl = this.isAngry() && !this.hasStung() && this.getTarget() != null && this.getTarget().distanceToSqr(this) < (double)4.0F;
            this.setRolling(bl);
            if (this.tickCount % 20 == 0 && !this.isHiveValid()) {
                this.hivePos = null;
            }
        }

    }

    boolean isHiveValid() {
        if (!this.hasHive()) {
            return false;
        } else if (this.isTooFarAway(this.hivePos)) {
            return false;
        } else {
            BlockEntity blockEntity = this.level().getBlockEntity(this.hivePos);
            return blockEntity != null && blockEntity.getType() == BlockEntityRegistry.BEEPER_HIVE_BLOCK_ENTITY;
        }
    }

    public boolean isNaked() {
        return this.getFlag(16);
    }

    private void setIsNaked(boolean isNaked) { this.setFlag(16, isNaked); }

    public boolean hasColor() {
        return this.getFlag(32);
    }

    private void setHasColor(boolean isWild) { this.setFlag(32, isWild); }

    public boolean isWild() {
        return entityData.get(DATA_WILD);
    }

    private void setIsWild(boolean isWild) { entityData.set(DATA_WILD, isWild); }


    public boolean isTame() {
        return entityData.get(DATA_TAME);
    }

    private void setIsTame(boolean isTame) { entityData.set(DATA_TAME, isTame); }


    public boolean hasNectar() {
        return this.getFlag(8);
    }

    void setHasNectar(boolean hasNectar) {
        if (hasNectar) {
            this.resetTicksWithoutNectarSinceExitingHive();
            this.remainingCooldownBeforeNextSneeze = 0;
        }

        this.setFlag(8, hasNectar);
    }

    public boolean hasStung() {
        return this.getFlag(4);
    }

    private void setHasStung(boolean hasStung) {
        this.setFlag(4, hasStung);
    }

    private boolean isRolling() {
        return this.getFlag(2);
    }

    private void setRolling(boolean isRolling) {
        this.setFlag(2, isRolling);
    }

    boolean isTooFarAway(BlockPos pos) {
        return !this.closerThan(pos, 32);
    }

    private void setFlag(int flagId, boolean value) {
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) | flagId));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)((Byte)this.entityData.get(DATA_FLAGS_ID) & ~flagId));
        }

    }

    private boolean getFlag(int flagId) {
        return ((Byte)this.entityData.get(DATA_FLAGS_ID) & flagId) != 0;
    }

    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(BlockPos pos) {
                return !this.level.getBlockState(pos.below()).isAir();
            }

            public void tick() {
                if (!BeeperEntity.this.beePollinateGoal.isPollinating()) {
                    super.tick();
                }
            }
        };
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(false);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(TagRegistry.Items.BEEPER_FOOD);
    }

    boolean isCropValid(BlockPos pos) {
        return this.level().isLoaded(pos) && this.level().getBlockState(pos).is(TagRegistry.Blocks.BEEPER_CAN_POLLINATE);
    }

    private void setSavedCrop(BlockPos pos) {
        this.savedCrop = this.level().getBlockState(pos).getBlock();
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.BEE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundRegistry.BEEPER_DIE;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    public BeeperEntity getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        BeeperEntity beeper = EntityRegistry.BEEPER.create(level);
        if (beeper != null && otherParent instanceof BeeperEntity beeper2) {
            if (!this.isWild() || !beeper2.isWild()) {
                beeper.setIsWild(false);
            } else if (level.random.nextFloat() > 0.9) {
                beeper.setIsWild(false);
            } else {
                beeper.setIsWild(true);
            }
            beeper.setIsTame(true);
        }
        return beeper;
    }

    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
    }

    public void regrowFluff() {
        this.setIsNaked(false);
    }

    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            if (!this.level().isClientSide) {
                this.beePollinateGoal.stopPollinating();
            }

            return super.hurt(source, amount);
        }
    }

    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluidTag) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0D, (0.5D * this.getEyeHeight()), (this.getBbWidth() * 0.2D));
    }

    boolean closerThan(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), distance);
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.BYTE);
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(15, 29);
        DATA_WOOL_ID = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.BYTE);
        ITEM_BY_DYE = Util.make(Maps.newEnumMap(DyeColor.class), (enumMap) -> {
            enumMap.put(DyeColor.WHITE, BlockRegistry.WHITE_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.ORANGE, BlockRegistry.ORANGE_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.MAGENTA, BlockRegistry.MAGENTA_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.LIGHT_BLUE, BlockRegistry.LIGHT_BLUE_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.YELLOW, BlockRegistry.YELLOW_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.LIME, BlockRegistry.LIME_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.PINK, BlockRegistry.PINK_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.GRAY, BlockRegistry.GRAY_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.LIGHT_GRAY, BlockRegistry.LIGHT_GRAY_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.CYAN, BlockRegistry.CYAN_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.PURPLE, BlockRegistry.PURPLE_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.BLUE, BlockRegistry.BLUE_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.BROWN, BlockRegistry.BROWN_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.GREEN, BlockRegistry.GREEN_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.RED, BlockRegistry.RED_BEEPER_FLUFF_BLOCK);
            enumMap.put(DyeColor.BLACK, BlockRegistry.BLACK_BEEPER_FLUFF_BLOCK);
        });
        COLORARRAY_BY_COLOR = Util.make(Maps.newEnumMap(DyeColor.class), (enumMap -> {
            enumMap.put(DyeColor.WHITE, 16777215);
            enumMap.put(DyeColor.ORANGE, 16746823);
            enumMap.put(DyeColor.MAGENTA, 11357810);
            enumMap.put(DyeColor.LIGHT_BLUE, 9099502);
            enumMap.put(DyeColor.YELLOW, 16436533);
            enumMap.put(DyeColor.LIME, 10406776);
            enumMap.put(DyeColor.PINK, 16691143);
            enumMap.put(DyeColor.GRAY, 5263439);
            enumMap.put(DyeColor.LIGHT_GRAY, 11184552);
            enumMap.put(DyeColor.CYAN, 4886379);
            enumMap.put(DyeColor.PURPLE, 9791888);
            enumMap.put(DyeColor.BLUE, 6135980);
            enumMap.put(DyeColor.BROWN, 7163978);
            enumMap.put(DyeColor.GREEN, 6653002);
            enumMap.put(DyeColor.RED, 12802130);
            enumMap.put(DyeColor.BLACK, 657930);
        }));
        DATA_SAVED_CROP_POS = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.BLOCK_POS);
        DATA_TAME = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_WILD = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.BOOLEAN);
    }

    class WildBeeperNearestAttackableTargetGoal extends NearestAttackableTargetGoal {

        public WildBeeperNearestAttackableTargetGoal(Mob mob, Class targetType, boolean mustSee) {
            super(mob, targetType, mustSee);
        }

        @Override
        public boolean canUse() {
            if (BeeperEntity.this.isWild()) {
                if (BeeperEntity.this.isTame()) return false;
                else return super.canUse();
            }
            else return false;
        }
    }

    class BeeperHurtByOtherGoal extends HurtByTargetGoal {
        BeeperHurtByOtherGoal(BeeperEntity beeper, BeeperEntity mob) {
            super(mob, new Class[0]);
        }

        public boolean canContinueToUse() {
            return BeeperEntity.this.isAngry() && super.canContinueToUse();
        }

        protected void alertOther(@NotNull Mob mob, @NotNull LivingEntity target) {
            if (mob instanceof BeeperEntity && this.mob.hasLineOfSight(target)) {
                mob.setTarget(target);
            }

        }
    }

    static class BeeperBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
        BeeperBecomeAngryTargetGoal(BeeperEntity mob) {
            super(mob, Player.class, 10, true, false, mob::isAngryAt);
        }

        public boolean canUse() {
            return this.beeCanTarget() && super.canUse();
        }

        public boolean canContinueToUse() {
            boolean bl = this.beeCanTarget();
            if (bl && this.mob.getTarget() != null) {
                return super.canContinueToUse();
            } else {
                this.targetMob = null;
                return false;
            }
        }

        private boolean beeCanTarget() {
            BeeperEntity beeper = (BeeperEntity)this.mob;
            return beeper.isAngry() && !beeper.hasStung();
        }
    }

    abstract class BaseBeeperGoal extends Goal {
        BaseBeeperGoal(BeeperEntity beeper) {
        }

        public abstract boolean canBeeperUse();

        public abstract boolean canBeeperContinueToUse();

        public boolean canUse() {
            return this.canBeeperUse() && !BeeperEntity.this.isAngry();
        }

        public boolean canContinueToUse() {
            return this.canBeeperContinueToUse() && !BeeperEntity.this.isAngry();
        }
    }

    class BeeperWanderGoal extends Goal {
        private static final int WANDER_THRESHOLD = 22;

        BeeperWanderGoal(BeeperEntity beeper) {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            return BeeperEntity.this.navigation.isDone() && BeeperEntity.this.random.nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return BeeperEntity.this.navigation.isInProgress();
        }

        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                BeeperEntity.this.navigation.moveTo(BeeperEntity.this.navigation.createPath(BlockPos.containing(vec3), 1), (double)1.0F);
            }

        }

        @Nullable
        private Vec3 findPos() {
            Vec3 vec32;
            if (BeeperEntity.this.isHiveValid() && !BeeperEntity.this.closerThan(BeeperEntity.this.hivePos, 22)) {
                Vec3 vec3 = Vec3.atCenterOf(BeeperEntity.this.hivePos);
                vec32 = vec3.subtract(BeeperEntity.this.position()).normalize();
            } else {
                vec32 = BeeperEntity.this.getViewVector(0.0F);
            }

            int i = 8;
            Vec3 vec33 = HoverRandomPos.getPos(BeeperEntity.this, 8, 7, vec32.x, vec32.z, ((float)Math.PI / 2F), 3, 1);
            return vec33 != null ? vec33 : AirAndWaterRandomPos.getPos(BeeperEntity.this, 8, 4, -2, vec32.x, vec32.z, (double)((float)Math.PI / 2F));
        }
    }

    @VisibleForDebug
    public class BeeperGoToHiveGoal extends BeeperEntity.BaseBeeperGoal {
        public static final int MAX_TRAVELLING_TICKS = 600;
        int travellingTicks;
        private static final int MAX_BLACKLISTED_TARGETS = 3;
        final List<BlockPos> blacklistedTargets;
        @Nullable
        private Path lastPath;
        private static final int TICKS_BEFORE_HIVE_DROP = 60;
        private int ticksStuck;

        BeeperGoToHiveGoal(BeeperEntity beeper) {
            super(beeper);
            this.travellingTicks = BeeperEntity.this.level().random.nextInt(10);
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canBeeperUse() {
            return BeeperEntity.this.hivePos != null && !BeeperEntity.this.hasRestriction() && BeeperEntity.this.wantsToEnterHive() && !this.hasReachedTarget(BeeperEntity.this.hivePos) && BeeperEntity.this.level().getBlockState(BeeperEntity.this.hivePos).is(TagRegistry.Blocks.BEEPER_HIVES);
        }

        public boolean canBeeperContinueToUse() {
            return this.canBeeperUse();
        }

        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            BeeperEntity.this.navigation.stop();
            BeeperEntity.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (BeeperEntity.this.hivePos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(600)) {
                    this.dropAndBlacklistHive();
                } else if (!BeeperEntity.this.navigation.isInProgress()) {
                    if (!BeeperEntity.this.closerThan(BeeperEntity.this.hivePos, 16)) {
                        if (BeeperEntity.this.isTooFarAway(BeeperEntity.this.hivePos)) {
                            this.dropHive();
                        } else {
                            BeeperEntity.this.pathfindRandomlyTowards(BeeperEntity.this.hivePos);
                        }
                    } else {
                        boolean bl = this.pathfindDirectlyTowards(BeeperEntity.this.hivePos);
                        if (!bl) {
                            this.dropAndBlacklistHive();
                        } else if (this.lastPath != null && BeeperEntity.this.navigation.getPath().sameAs(this.lastPath)) {
                            ++this.ticksStuck;
                            if (this.ticksStuck > 60) {
                                this.dropHive();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = BeeperEntity.this.navigation.getPath();
                        }

                    }
                }
            }
        }

        private boolean pathfindDirectlyTowards(BlockPos pos) {
            BeeperEntity.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
            BeeperEntity.this.navigation.moveTo((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)1.0F);
            return BeeperEntity.this.navigation.getPath() != null && BeeperEntity.this.navigation.getPath().canReach();
        }

        boolean isTargetBlacklisted(BlockPos pos) {
            return this.blacklistedTargets.contains(pos);
        }

        private void blacklistTarget(BlockPos pos) {
            this.blacklistedTargets.add(pos);

            while(this.blacklistedTargets.size() > 3) {
                this.blacklistedTargets.remove(0);
            }

        }

        void clearBlacklist() {
            this.blacklistedTargets.clear();
        }

        private void dropAndBlacklistHive() {
            if (BeeperEntity.this.hivePos != null) {
                this.blacklistTarget(BeeperEntity.this.hivePos);
            }

            this.dropHive();
        }

        private void dropHive() {
            BeeperEntity.this.hivePos = null;
            BeeperEntity.this.remainingCooldownBeforeLocatingNewHive = 200;
        }

        private boolean hasReachedTarget(BlockPos pos) {
            if (BeeperEntity.this.closerThan(pos, 2)) {
                return true;
            } else {
                Path path = BeeperEntity.this.navigation.getPath();
                return path != null && path.getTarget().equals(pos) && path.canReach() && path.isDone();
            }
        }
    }

    public class BeeperGoToKnownFlowerGoal extends BeeperEntity.BaseBeeperGoal {
        private static final int MAX_TRAVELLING_TICKS = 600;
        int travellingTicks;

        BeeperGoToKnownFlowerGoal(BeeperEntity beeper) {
            super(beeper);
            this.travellingTicks = BeeperEntity.this.level().random.nextInt(10);
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canBeeperUse() {
            return BeeperEntity.this.savedCropPos != null && !BeeperEntity.this.hasRestriction() && this.wantsToGoToKnownFlower() && BeeperEntity.this.isCropValid(BeeperEntity.this.savedCropPos) && !BeeperEntity.this.closerThan(BeeperEntity.this.savedCropPos, 2);
        }

        public boolean canBeeperContinueToUse() {
            return this.canBeeperUse();
        }

        public void start() {
            this.travellingTicks = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            BeeperEntity.this.navigation.stop();
            BeeperEntity.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (BeeperEntity.this.savedCropPos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(600)) {
                    BeeperEntity.this.savedCropPos = null;
                } else if (!BeeperEntity.this.navigation.isInProgress()) {
                    if (BeeperEntity.this.isTooFarAway(BeeperEntity.this.savedCropPos)) {
                        BeeperEntity.this.savedCropPos = null;
                    } else {
                        BeeperEntity.this.pathfindRandomlyTowards(BeeperEntity.this.savedCropPos);
                    }
                }
            }
        }

        private boolean wantsToGoToKnownFlower() {
            return BeeperEntity.this.ticksWithoutNectarSinceExitingHive > 2400;
        }
    }

    class BeeperLookControl extends LookControl {
        BeeperLookControl(BeeperEntity beeper, Mob mob) {
            super(mob);
        }

        public void tick() {
            if (!BeeperEntity.this.isAngry()) {
                super.tick();
            }
        }

        protected boolean resetXRotOnTick() {
            return !BeeperEntity.this.beePollinateGoal.isPollinating();
        }
    }

    class BeeperPollinateGoal extends BeeperEntity.BaseBeeperGoal {
        private final Predicate<BlockState> VALID_POLLINATION_BLOCKS = (blockState) -> {
            if (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && (Boolean)blockState.getValue(BlockStateProperties.WATERLOGGED)) {
                return false;
            } else return blockState.is(TagRegistry.Blocks.BEEPER_CAN_POLLINATE);
        };
        private int successfulPollinatingTicks;
        private int lastSoundPlayedTick;
        private boolean pollinating;
        @Nullable
        private Vec3 hoverPos;
        private int pollinatingTicks;

        BeeperPollinateGoal(BeeperEntity bee) {
            super(bee);
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canBeeperUse() {
            if (BeeperEntity.this.remainingCooldownBeforeLocatingNewFlower > 0) {
                return false;
            } else if (BeeperEntity.this.hasNectar()) {
                return false;
            } else if (BeeperEntity.this.level().isRaining()) {
                return false;
            } else {
                Optional<BlockPos> optional = this.findNearbyFlower();
                if (optional.isPresent()) {
                    BeeperEntity.this.savedCropPos = (BlockPos)optional.get();
                    BeeperEntity.this.navigation.moveTo((double)BeeperEntity.this.savedCropPos.getX() + (double)0.5F, (double)BeeperEntity.this.savedCropPos.getY() + (double)0.5F, (double)BeeperEntity.this.savedCropPos.getZ() + (double)0.5F, (double)1.2F);
                    return true;
                } else {
                    BeeperEntity.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(BeeperEntity.this.random, 20, 60);
                    return false;
                }
            }
        }

        public boolean canBeeperContinueToUse() {
            if (!this.pollinating) {
                return false;
            } else if (!BeeperEntity.this.hasSavedFlowerPos()) {
                return false;
            } else if (BeeperEntity.this.level().isRaining()) {
                return false;
            } else if (this.hasPollinatedLongEnough()) {
                return BeeperEntity.this.random.nextFloat() < 0.2F;
            } else if (BeeperEntity.this.tickCount % 20 == 0 && !BeeperEntity.this.isCropValid(BeeperEntity.this.savedCropPos)) {
                BeeperEntity.this.savedCropPos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean hasPollinatedLongEnough() {
            if (!isNaked()) {
                return this.successfulPollinatingTicks > 400;
            } else {
                return this.successfulPollinatingTicks > 200;
            }
        }

        boolean isPollinating() {
            return this.pollinating;
        }

        void stopPollinating() {
            this.pollinating = false;
        }

        public void start() {
            this.successfulPollinatingTicks = 0;
            this.pollinatingTicks = 0;
            this.lastSoundPlayedTick = 0;
            this.pollinating = true;
            BeeperEntity.this.resetTicksWithoutNectarSinceExitingHive();
        }

        public void stop() {
            if (this.hasPollinatedLongEnough()) {
                if (!isNaked()) {
                    BeeperEntity.this.setHasNectar(true);
                    BeeperEntity.this.setSavedCrop(savedCropPos);
                    JeepersBeepers.LOGGER.info("Beeper saved crop set to {}", savedCrop);
                }
            }

            this.pollinating = false;
            BeeperEntity.this.navigation.stop();
            BeeperEntity.this.remainingCooldownBeforeLocatingNewFlower = 200;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            ++this.pollinatingTicks;
            if (this.pollinatingTicks > 600) {
                BeeperEntity.this.savedCropPos = null;
            } else {
                Vec3 vec3 = Vec3.atBottomCenterOf(BeeperEntity.this.savedCropPos).add((double)0.0F, (double)0.6F, (double)0.0F);
                if (vec3.distanceTo(BeeperEntity.this.position()) > (double)1.0F) {
                    this.hoverPos = vec3;
                    this.setWantedPos();
                } else {
                    if (this.hoverPos == null) {
                        this.hoverPos = vec3;
                    }

                    boolean bl = BeeperEntity.this.position().distanceTo(this.hoverPos) <= 0.1;
                    boolean bl2 = true;
                    if (!bl && this.pollinatingTicks > 600) {
                        BeeperEntity.this.savedCropPos = null;
                    } else {
                        if (bl) {
                            boolean bl3 = BeeperEntity.this.random.nextInt(25) == 0;
                            if (bl3) {
                                this.hoverPos = new Vec3(vec3.x() + (double)this.getOffset(), vec3.y(), vec3.z() + (double)this.getOffset());
                                BeeperEntity.this.navigation.stop();
                            } else {
                                bl2 = false;
                            }

                            BeeperEntity.this.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
                        }

                        if (bl2) {
                            this.setWantedPos();
                        }

                        ++this.successfulPollinatingTicks;
                        if (BeeperEntity.this.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
                            this.lastSoundPlayedTick = this.successfulPollinatingTicks;
                            BeeperEntity.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
                        }

                    }
                }
            }
        }

        private void setWantedPos() {
            BeeperEntity.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), (double)0.35F);
        }

        private float getOffset() {
            return (BeeperEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> findNearbyFlower() {
            return this.findNearestBlock(this.VALID_POLLINATION_BLOCKS, (double)5.0F);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate, double distance) {
            BlockPos blockPos = BeeperEntity.this.blockPosition();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for(int i = 0; (double)i <= distance; i = i > 0 ? -i : 1 - i) {
                for(int j = 0; (double)j < distance; ++j) {
                    for(int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                        for(int l = k < j && k > -j ? j : 0; l <= j; l = l > 0 ? -l : 1 - l) {
                            mutableBlockPos.setWithOffset(blockPos, k, i - 1, l);
                            if (blockPos.closerThan(mutableBlockPos, distance) && predicate.test(BeeperEntity.this.level().getBlockState(mutableBlockPos))) {
                                return Optional.of(mutableBlockPos);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }
    }

    class BeeperLocateHiveGoal extends BeeperEntity.BaseBeeperGoal {
        BeeperLocateHiveGoal(BeeperEntity beeper) {
            super(beeper);
        }

        public boolean canBeeperUse() {
            return BeeperEntity.this.remainingCooldownBeforeLocatingNewHive == 0 && !BeeperEntity.this.hasHive() && BeeperEntity.this.wantsToEnterHive();
        }

        public boolean canBeeperContinueToUse() {
            return false;
        }

        public void start() {
            BeeperEntity.this.remainingCooldownBeforeLocatingNewHive = 200;
            List<BlockPos> list = this.findNearbyHivesWithSpace();
            if (!list.isEmpty()) {
                for(BlockPos blockPos : list) {
                    if (!BeeperEntity.this.goToHiveGoal.isTargetBlacklisted(blockPos)) {
                        BeeperEntity.this.hivePos = blockPos;
                        return;
                    }
                }

                BeeperEntity.this.goToHiveGoal.clearBlacklist();
                BeeperEntity.this.hivePos = (BlockPos)list.get(0);
            }
        }

        private List<BlockPos> findNearbyHivesWithSpace() {
            BlockPos blockPos = BeeperEntity.this.blockPosition();
            PoiManager poiManager = ((ServerLevel)BeeperEntity.this.level()).getPoiManager();
            Stream<PoiRecord> stream = poiManager.getInRange((holder) -> holder.is(TagRegistry.Misc.BEEPER_HOME), blockPos, 20, PoiManager.Occupancy.ANY);
            return (List<BlockPos>)stream.map(PoiRecord::getPos).filter(BeeperEntity.this::doesHiveHaveSpace).sorted(Comparator.comparingDouble((blockPos2) -> blockPos2.distSqr(blockPos))).collect(Collectors.toList());
        }
    }

    class BeeperGrowCropGoal extends BeeperEntity.BaseBeeperGoal {
        static final int GROW_CHANCE = 30;

        BeeperGrowCropGoal(BeeperEntity beeper) {
            super(beeper);
        }

        public boolean canBeeperUse() {
            if (BeeperEntity.this.getCropsGrownSincePollination() >= 10) {
                return false;
            } else if (BeeperEntity.this.random.nextFloat() < 0.3F) {
                return false;
            } else {
                return BeeperEntity.this.hasNectar() && BeeperEntity.this.isHiveValid();
            }
        }

        public boolean canBeeperContinueToUse() {
            return this.canBeeperUse();
        }

        public void tick() {
            if (BeeperEntity.this.random.nextInt(this.adjustedTickDelay(30)) == 0) {
                for(int i = 1; i <= 2; ++i) {
                    BlockPos blockPos = BeeperEntity.this.blockPosition().below(i);
                    BlockState blockState = BeeperEntity.this.level().getBlockState(blockPos);
                    Block block = blockState.getBlock();
                    BlockState blockState2 = null;
                    if (blockState.is(BlockTags.BEE_GROWABLES)) {
                        if (block instanceof CropBlock) {
                            CropBlock cropBlock = (CropBlock)block;
                            if (!cropBlock.isMaxAge(blockState)) {
                                blockState2 = cropBlock.getStateForAge(cropBlock.getAge(blockState) + 1);
                            }
                        } else if (block instanceof StemBlock) {
                            int j = (Integer)blockState.getValue(StemBlock.AGE);
                            if (j < 7) {
                                blockState2 = (BlockState)blockState.setValue(StemBlock.AGE, j + 1);
                            }
                        } else if (blockState.is(Blocks.SWEET_BERRY_BUSH)) {
                            int j = (Integer)blockState.getValue(SweetBerryBushBlock.AGE);
                            if (j < 3) {
                                blockState2 = (BlockState)blockState.setValue(SweetBerryBushBlock.AGE, j + 1);
                            }
                        } else if (blockState.is(Blocks.CAVE_VINES) || blockState.is(Blocks.CAVE_VINES_PLANT)) {
                            ((BonemealableBlock)blockState.getBlock()).performBonemeal((ServerLevel)BeeperEntity.this.level(), BeeperEntity.this.random, blockPos, blockState);
                        }

                        if (blockState2 != null) {
                            BeeperEntity.this.level().levelEvent(2005, blockPos, 0);
                            BeeperEntity.this.level().setBlockAndUpdate(blockPos, blockState2);
                            BeeperEntity.this.incrementNumCropsGrownSincePollination();
                        }
                    }
                }

            }
        }
    }

    class BeeperSneezeGoal extends BaseBeeperGoal {

        private boolean isSneezing;

        BeeperSneezeGoal(BeeperEntity beeper) {
            super(beeper);
        }

        @Override
        public boolean canBeeperUse() {
            if (BeeperEntity.this.isWild()) return false;
            if (BeeperEntity.this.savedCrop == null) return false;
            if (BeeperEntity.this.getCropsGrownSincePollination() >= 30) {
                return false;
            } else if (BeeperEntity.this.random.nextFloat() < 0.7F) {
                return false;
            } else {
                return BeeperEntity.this.hasNectar() && BeeperEntity.this.remainingCooldownBeforeNextSneeze == 0;
            }
        }

        @Override
        public boolean canBeeperContinueToUse() {
            if (remainingCooldownBeforeNextSneeze > 0) {
                return false;
            } else {
                return isSneezing;
            }
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            BeeperEntity.this.remainingCooldownBeforeNextSneeze = 200;
            isSneezing = true;
            BeeperEntity.this.readyToSneeze = true;
        }

        @Override
        public void tick() {
            if (BeeperEntity.this.readyToSneeze) {
                BeeperEntity.this.sneeze();
            }
        }
    }

    class BeeperAttackGoal extends MeleeAttackGoal {
        BeeperAttackGoal(BeeperEntity beeper, PathfinderMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        public boolean canUse() {
            return super.canUse() && BeeperEntity.this.isAngry() && !BeeperEntity.this.hasStung();
        }

        public boolean canContinueToUse() {
            return super.canContinueToUse() && BeeperEntity.this.isAngry() && !BeeperEntity.this.hasStung();
        }
    }

    class BeeperEnterHiveGoal extends BeeperEntity.BaseBeeperGoal {
        BeeperEnterHiveGoal(BeeperEntity beeper) {
            super(beeper);
        }

        public boolean canBeeperUse() {
            if (BeeperEntity.this.hasHive() && BeeperEntity.this.wantsToEnterHive() && BeeperEntity.this.hivePos.closerToCenterThan(BeeperEntity.this.position(), (double)2.0F)) {
                BlockEntity blockEntity = BeeperEntity.this.level().getBlockEntity(BeeperEntity.this.hivePos);
                if (blockEntity instanceof BeeperHiveBlockEntity beeperHiveBlockEntity) {
                    if (!beeperHiveBlockEntity.isFull()) {
                        return true;
                    }

                    BeeperEntity.this.hivePos = null;
                }
            }

            return false;
        }

        public boolean canBeeperContinueToUse() {
            return false;
        }

        public void start() {
            BlockEntity blockEntity = BeeperEntity.this.level().getBlockEntity(BeeperEntity.this.hivePos);
            if (blockEntity instanceof BeeperHiveBlockEntity beeperHiveBlockEntity) {
                beeperHiveBlockEntity.addOccupant(BeeperEntity.this, BeeperEntity.this.hasNectar(), isNaked());
            }

        }
    }
}
