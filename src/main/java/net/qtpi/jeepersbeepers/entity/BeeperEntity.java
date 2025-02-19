package net.qtpi.jeepersbeepers.entity;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.qtpi.jeepersbeepers.block.entity.BeeperHiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.qtpi.jeepersbeepers.registry.BlockEntityRegistry;
import net.qtpi.jeepersbeepers.registry.EntityRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeeperEntity extends Animal implements GeoEntity, NeutralMob, FlyingAnimal {
    public static final float FLAP_DEGREES_PER_TICK = 120.32113F;
    public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID;
    private static final EntityDataAccessor<Integer> DATA_REMAINING_ANGER_TIME;
    private static final int FLAG_ROLL = 2;
    private static final int FLAG_HAS_STUNG = 4;
    private static final int FLAG_HAS_NECTAR = 8;
    private static final int FLAG_HAS_FLUFF = 16;
    private static final int STING_DEATH_COUNTDOWN = 1200;
    private static final int TICKS_BEFORE_GOING_TO_KNOWN_FLOWER = 2400;
    private static final int TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME = 3600;
    private static final int MIN_ATTACK_DIST = 4;
    private static final int MAX_CROPS_GROWABLE = 10;
    private static final int POISON_SECONDS_NORMAL = 10;
    private static final int POISON_SECONDS_HARD = 18;
    private static final int TOO_FAR_DISTANCE = 32;
    private static final int HIVE_CLOSE_ENOUGH_DISTANCE = 2;
    private static final int PATHFIND_TO_HIVE_WHEN_CLOSER_THAN = 16;
    private static final int HIVE_SEARCH_DISTANCE = 20;
    public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";
    public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";
    public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";
    public static final String TAG_HAS_STUNG = "HasStung";
    public static final String TAG_HAS_NECTAR = "HasNectar";
    public static final String TAG_IS_NAKED = "IsNaked";
    public static final String TAG_FLOWER_POS = "FlowerPos";
    public static final String TAG_HIVE_POS = "HivePos";
    private static final UniformInt PERSISTENT_ANGER_TIME;
    @Nullable
    private UUID persistentAngerTarget;
    private float rollAmount;
    private float rollAmountO;
    private int timeSinceSting;
    int ticksWithoutNectarSinceExitingHive;
    private int stayOutOfHiveCountdown;
    private int numCropsGrownSincePollination;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
    int remainingCooldownBeforeLocatingNewHive;
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;
    int remainingCooldownBeforeLocatingNewFlower;
    private static final int COOLDOWN_BEFORE_NEXT_SNEEZE = 200;
    int remainingCooldownBeforeNextSneeze;
    boolean readyToSneeze;
    int readyToSneezeCountdown;
    @Nullable
    BlockPos savedFlowerPos;
    @Nullable
    BlockPos hivePos;
    BeeperEntity.BeeperPollinateGoal beePollinateGoal;
    BeeperEntity.BeeperGoToHiveGoal goToHiveGoal;
    private BeeperEntity.BeeperGoToKnownFlowerGoal goToKnownFlowerGoal;
    private int underWaterTicks;

    protected static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.beeper.idle");
    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("animation.beeper.fly");
    protected static final RawAnimation SNEEZE_ANIM = RawAnimation.begin().then("animation.beeper.sneeze", Animation.LoopType.PLAY_ONCE);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

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
    }

    private PlayState flyController(AnimationState<BeeperEntity> beeperEntityAnimationState) {
        beeperEntityAnimationState.getController().setAnimation(FLY_ANIM);
        return PlayState.CONTINUE;
    }

    private PlayState predicate(AnimationState<BeeperEntity> beeperEntityAnimationState) {
        if (this.readyToSneeze) {
            beeperEntityAnimationState.getController().setAnimation(SNEEZE_ANIM);
        } else {
            beeperEntityAnimationState.getController().setAnimation(IDLE_ANIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte)0);
        this.entityData.define(DATA_REMAINING_ANGER_TIME, 0);
    }

    public float getWalkTargetValue(@NotNull BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BeeperEntity.BeeperAttackGoal(this, this, (double)1.4F, true));
        this.goalSelector.addGoal(1, new BeeperEntity.BeeperEnterHiveGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, (double)1.0F));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.25F, Ingredient.of(ItemTags.FLOWERS), false));
        this.beePollinateGoal = new BeeperEntity.BeeperPollinateGoal(this);
        this.goalSelector.addGoal(4, this.beePollinateGoal);
        this.goalSelector.addGoal(5, new FollowParentGoal(this, (double)1.25F));
        this.goalSelector.addGoal(5, new BeeperEntity.BeeperLocateHiveGoal(this));
        this.goToHiveGoal = new BeeperEntity.BeeperGoToHiveGoal(this);
        this.goalSelector.addGoal(5, this.goToHiveGoal);
        this.goToKnownFlowerGoal = new BeeperEntity.BeeperGoToKnownFlowerGoal(this);
        this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
        this.goalSelector.addGoal(7, new BeeperEntity.BeeperGrowCropGoal(this));
        this.goalSelector.addGoal(8, new BeeperEntity.BeeperSneezeGoal(this));
        this.goalSelector.addGoal(9, new BeeperEntity.BeeperWanderGoal(this));
        this.goalSelector.addGoal(10, new FloatGoal(this));
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
            compound.put("FlowerPos", NbtUtils.writeBlockPos(this.getSavedFlowerPos()));
        }

        compound.putBoolean("HasNectar", this.hasNectar());
        compound.putBoolean("HasStung", this.hasStung());
        compound.putBoolean("IsNaked", this.isNaked());
        compound.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
        compound.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
        compound.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
        this.addPersistentAngerSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        this.hivePos = null;
        if (compound.contains("HivePos")) {
            this.hivePos = NbtUtils.readBlockPos(compound.getCompound("HivePos"));
        }

        this.savedFlowerPos = null;
        if (compound.contains("FlowerPos")) {
            this.savedFlowerPos = NbtUtils.readBlockPos(compound.getCompound("FlowerPos"));
        }

        super.readAdditionalSaveData(compound);
        this.setHasNectar(compound.getBoolean("HasNectar"));
        this.setHasStung(compound.getBoolean("HasStung"));
        this.setIsNaked(compound.getBoolean("IsNaked"));
        this.ticksWithoutNectarSinceExitingHive = compound.getInt("TicksSincePollination");
        this.stayOutOfHiveCountdown = compound.getInt("CannotEnterHiveTicks");
        this.numCropsGrownSincePollination = compound.getInt("CropsGrownSincePollination");
        this.readPersistentAngerSaveData(this.level(), compound);
    }

    public boolean doHurtTarget(Entity target) {
        boolean bl = target.hurt(this.damageSources().sting(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (bl) {
            this.doEnchantDamageEffects(this, target);
            if (target instanceof LivingEntity) {
                ((LivingEntity)target).setStingerCount(((LivingEntity)target).getStingerCount() + 1);
                int i = 0;
                if (this.level().getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.level().getDifficulty() == Difficulty.HARD) {
                    i = 18;
                }

                if (i > 0) {
                    ((LivingEntity)target).addEffect(new MobEffectInstance(MobEffects.POISON, i * 20, 0), this);
                }
            }

            this.setHasStung(true);
            this.stopBeingAngry();
            this.playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
        }

        return bl;
    }

    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.level(), this.getX() - (double)0.3F, this.getX() + (double)0.3F, this.getZ() - (double)0.3F, this.getZ() + (double)0.3F, this.getY((double)0.5F), ParticleTypes.FALLING_NECTAR);
            }
        }

        this.updateRollAmount();
    }

    private void spawnFluidParticle(Level level, double startX, double endX, double startZ, double endZ, double posY, ParticleOptions particleOption) {
        level.addParticle(particleOption, Mth.lerp(level.random.nextDouble(), startX, endX), posY, Mth.lerp(level.random.nextDouble(), startZ, endZ), (double)0.0F, (double)0.0F, (double)0.0F);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS)) {
            if (!this.level().isClientSide && this.readyForShearing()) {
                this.shear(SoundSource.PLAYERS);
                this.gameEvent(GameEvent.SHEAR, player);
                itemStack.hurtAndBreak(1, player, (playerx) -> playerx.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    public void shear(SoundSource source) {
        this.level().playSound((Player)null, this, SoundEvents.SHEEP_SHEAR, source, 1.0F, 1.0F);
        this.setIsNaked(true);
        int i = 1 + this.random.nextInt(2);

        for(int j = 0; j < i; ++j) {
            ItemEntity itemEntity = this.spawnAtLocation(ItemRegistry.BEEPER_FLUFF, 1);
            if (itemEntity != null) {
                itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F), (double)(this.random.nextFloat() * 0.05F), (double)((this.random.nextFloat() - this.random.nextFloat()) * 0.1F)));
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
    public BlockPos getSavedFlowerPos() {
        return this.savedFlowerPos;
    }

    public boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    public void setSavedFlowerPos(@Nullable BlockPos savedFlowerPos) {
        this.savedFlowerPos = savedFlowerPos;
    }

    @VisibleForDebug
    public int getTravellingTicks() {
        return Math.max(this.goToHiveGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks);
    }

    @VisibleForDebug
    public List<BlockPos> getBlacklistedHives() {
        return this.goToHiveGoal.blacklistedTargets;
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
        return stack.is(ItemTags.FLOWERS);
    }

    boolean isFlowerValid(BlockPos pos) {
        return this.level().isLoaded(pos) && this.level().getBlockState(pos).is(BlockTags.FLOWERS);
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    protected SoundEvent getAmbientSound() {
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BEE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.BEE_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    @Nullable
    public BeeperEntity getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return EntityRegistry.BEEPER.create(level);
    }

    protected float getStandingEyeHeight(@NotNull Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    public void dropOffNectar() {
        this.setHasNectar(false);
        this.resetNumCropsGrownSincePollination();
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
        this.setDeltaMovement(this.getDeltaMovement().add((double)0.0F, 0.01, (double)0.0F));
    }

    public @NotNull Vec3 getLeashOffset() {
        return new Vec3((double)0.0F, (double)(0.5F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.2F));
    }

    boolean closerThan(BlockPos pos, int distance) {
        return pos.closerThan(this.blockPosition(), (double)distance);
    }

    static {
        DATA_FLAGS_ID = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.BYTE);
        DATA_REMAINING_ANGER_TIME = SynchedEntityData.defineId(BeeperEntity.class, EntityDataSerializers.INT);
        PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(15, 29);
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
            return BeeperEntity.this.savedFlowerPos != null && !BeeperEntity.this.hasRestriction() && this.wantsToGoToKnownFlower() && BeeperEntity.this.isFlowerValid(BeeperEntity.this.savedFlowerPos) && !BeeperEntity.this.closerThan(BeeperEntity.this.savedFlowerPos, 2);
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
            if (BeeperEntity.this.savedFlowerPos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(600)) {
                    BeeperEntity.this.savedFlowerPos = null;
                } else if (!BeeperEntity.this.navigation.isInProgress()) {
                    if (BeeperEntity.this.isTooFarAway(BeeperEntity.this.savedFlowerPos)) {
                        BeeperEntity.this.savedFlowerPos = null;
                    } else {
                        BeeperEntity.this.pathfindRandomlyTowards(BeeperEntity.this.savedFlowerPos);
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
        private static final int MIN_POLLINATION_TICKS = 400;
        private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;
        private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;
        private final Predicate<BlockState> VALID_POLLINATION_BLOCKS = (blockState) -> {
            if (blockState.hasProperty(BlockStateProperties.WATERLOGGED) && (Boolean)blockState.getValue(BlockStateProperties.WATERLOGGED)) {
                return false;
            } else if (blockState.is(BlockTags.FLOWERS)) {
                if (blockState.is(Blocks.SUNFLOWER)) {
                    return blockState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        };
        private static final double ARRIVAL_THRESHOLD = 0.1;
        private static final int POSITION_CHANGE_CHANCE = 25;
        private static final float SPEED_MODIFIER = 0.35F;
        private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
        private static final float HOVER_POS_OFFSET = 0.33333334F;
        private int successfulPollinatingTicks;
        private int lastSoundPlayedTick;
        private boolean pollinating;
        @Nullable
        private Vec3 hoverPos;
        private int pollinatingTicks;
        private static final int MAX_POLLINATING_TICKS = 600;

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
                    BeeperEntity.this.savedFlowerPos = (BlockPos)optional.get();
                    BeeperEntity.this.navigation.moveTo((double)BeeperEntity.this.savedFlowerPos.getX() + (double)0.5F, (double)BeeperEntity.this.savedFlowerPos.getY() + (double)0.5F, (double)BeeperEntity.this.savedFlowerPos.getZ() + (double)0.5F, (double)1.2F);
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
            } else if (BeeperEntity.this.isNaked()) {
                return false;
            } else if (this.hasPollinatedLongEnough()) {
                return BeeperEntity.this.random.nextFloat() < 0.2F;
            } else if (BeeperEntity.this.tickCount % 20 == 0 && !BeeperEntity.this.isFlowerValid(BeeperEntity.this.savedFlowerPos)) {
                BeeperEntity.this.savedFlowerPos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean hasPollinatedLongEnough() {
            return this.successfulPollinatingTicks > 400;
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
                BeeperEntity.this.setHasNectar(true);
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
                BeeperEntity.this.savedFlowerPos = null;
            } else {
                Vec3 vec3 = Vec3.atBottomCenterOf(BeeperEntity.this.savedFlowerPos).add((double)0.0F, (double)0.6F, (double)0.0F);
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
                        BeeperEntity.this.savedFlowerPos = null;
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

        private int sneezeTicks;

        BeeperSneezeGoal(BeeperEntity beeper) {
            super(beeper);
        }

        @Override
        public boolean canBeeperUse() {
            if (BeeperEntity.this.getCropsGrownSincePollination() >= 30) {
                return false;
            } else if (BeeperEntity.this.random.nextFloat() < 0.3F) {
                return false;
            } else {
                return BeeperEntity.this.hasNectar() && BeeperEntity.this.remainingCooldownBeforeNextSneeze == 0;
            }
        }

        @Override
        public boolean canBeeperContinueToUse() {
            return this.canBeeperUse();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            BeeperEntity.this.remainingCooldownBeforeNextSneeze = 200;
            sneezeTicks = 0;
            BeeperEntity.this.readyToSneeze = true;
        }

        @Override
        public void tick() {
            if (BeeperEntity.this.readyToSneeze) {
                sneezeTicks++;
                if (sneezeTicks == 80) {
                    readyToSneeze = false;
                }
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
                beeperHiveBlockEntity.addOccupant(BeeperEntity.this, BeeperEntity.this.hasNectar());
            }

        }
    }
}
