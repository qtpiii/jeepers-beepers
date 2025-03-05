package net.qtpi.jeepersbeepers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.block.entity.BeeperNestBlockEntity;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import net.qtpi.jeepersbeepers.registry.BlockEntityRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BeeperNestBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING;
    public static final IntegerProperty HONEY_LEVEL;
    public BeeperNestBlock(Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(HONEY_LEVEL, 0)).setValue(FACING, Direction.NORTH));
    }

    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return (Integer)state.getValue(HONEY_LEVEL);
    }

    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);
        if (!level.isClientSide && blockEntity instanceof BeeperNestBlockEntity beeperNestBlockEntity) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) == 0) {
                beeperNestBlockEntity.emptyAllLivingFromHive(player, state, BeeperNestBlockEntity.BeeperReleaseStatus.EMERGENCY);
                level.updateNeighbourForOutputSignal(pos, this);
                this.angerNearbyBeepers(level, pos);
            }
        }

    }

    private void angerNearbyBeepers(Level level, BlockPos pos) {
        List<BeeperEntity> list = level.getEntitiesOfClass(BeeperEntity.class, (new AABB(pos)).inflate((double)8.0F, (double)6.0F, (double)8.0F));
        if (!list.isEmpty()) {
            List<Player> list2 = level.getEntitiesOfClass(Player.class, (new AABB(pos)).inflate((double)8.0F, (double)6.0F, (double)8.0F));
            int i = list2.size();

            for(BeeperEntity beeper : list) {
                if (beeper.getTarget() == null && beeper.isWild()) {
                    beeper.setTarget((LivingEntity)list2.get(level.random.nextInt(i)));
                }
            }
        }

    }

    public static void dropHoneycomb(Level level, BlockPos pos) {
        popResource(level, pos, new ItemStack(ItemRegistry.SPICY_HONEYCOMB, 3));
    }

    public static void dropExtra(Level level, BlockPos pos) {
        for(int i = 0; i < level.random.nextInt(2) + 1; ++i) {
            popResource(level, pos, new ItemStack(ItemRegistry.BEEPER_FLUFF));
        }
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        int i = (Integer)state.getValue(HONEY_LEVEL);
        boolean bl = false;
        if (i >= 5) {
            Item item = itemStack.getItem();
            if (itemStack.is(Items.SHEARS)) {
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                dropHoneycomb(level, pos);
                itemStack.hurtAndBreak(1, player, (playerx) -> playerx.broadcastBreakEvent(hand));
                bl = true;
                level.gameEvent(player, GameEvent.SHEAR, pos);
            } else if (itemStack.is(Items.GLASS_BOTTLE)) {
                itemStack.shrink(1);
                level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (itemStack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(ItemRegistry.SPICY_HONEY_BOTTLE));
                } else if (!player.getInventory().add(new ItemStack(ItemRegistry.SPICY_HONEY_BOTTLE))) {
                    player.drop(new ItemStack(ItemRegistry.SPICY_HONEY_BOTTLE), false);
                }

                bl = true;
                level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
            }
            dropExtra(level, pos);
            if (!level.isClientSide() && bl) {
                player.awardStat(Stats.ITEM_USED.get(item));
            }
        }

        if (bl) {
            if (!CampfireBlock.isSmokeyPos(level, pos)) {
                if (this.hiveContainsBeepers(level, pos)) {
                    this.angerNearbyBeepers(level, pos);
                }

                this.releaseBeesAndResetHoneyLevel(level, state, pos, player, BeeperNestBlockEntity.BeeperReleaseStatus.EMERGENCY);
            } else {
                this.resetHoneyLevel(level, state, pos);
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }

    private boolean hiveContainsBeepers(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BeeperNestBlockEntity beehiveBlockEntity) {
            return !beehiveBlockEntity.isEmpty();
        } else {
            return false;
        }
    }

    public void releaseBeesAndResetHoneyLevel(Level level, BlockState state, BlockPos pos, @Nullable Player player, BeeperNestBlockEntity.BeeperReleaseStatus beeperReleaseStatus) {
        this.resetHoneyLevel(level, state, pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BeeperNestBlockEntity beehiveBlockEntity) {
            beehiveBlockEntity.emptyAllLivingFromHive(player, state, beeperReleaseStatus);
        }

    }

    public void resetHoneyLevel(Level level, BlockState state, BlockPos pos) {
        level.setBlock(pos, (BlockState)state.setValue(HONEY_LEVEL, 0), 3);
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if ((Integer)state.getValue(HONEY_LEVEL) >= 5) {
            for(int i = 0; i < random.nextInt(1) + 1; ++i) {
                this.trySpawnDripParticles(level, pos, state);
            }
        }

    }

    private void trySpawnDripParticles(Level level, BlockPos pos, BlockState state) {
        if (state.getFluidState().isEmpty() && !(level.random.nextFloat() < 0.3F)) {
            VoxelShape voxelShape = state.getCollisionShape(level, pos);
            double d = voxelShape.max(Direction.Axis.Y);
            if (d >= (double)1.0F && !state.is(BlockTags.IMPERMEABLE)) {
                double e = voxelShape.min(Direction.Axis.Y);
                if (e > (double)0.0F) {
                    this.spawnParticle(level, pos, voxelShape, (double)pos.getY() + e - 0.05);
                } else {
                    BlockPos blockPos = pos.below();
                    BlockState blockState = level.getBlockState(blockPos);
                    VoxelShape voxelShape2 = blockState.getCollisionShape(level, blockPos);
                    double f = voxelShape2.max(Direction.Axis.Y);
                    if ((f < (double)1.0F || !blockState.isCollisionShapeFullBlock(level, blockPos)) && blockState.getFluidState().isEmpty()) {
                        this.spawnParticle(level, pos, voxelShape, (double)pos.getY() - 0.05);
                    }
                }
            }

        }
    }

    private void spawnParticle(Level level, BlockPos pos, VoxelShape shape, double y) {
        this.spawnFluidParticle(level, (double)pos.getX() + shape.min(Direction.Axis.X), (double)pos.getX() + shape.max(Direction.Axis.X), (double)pos.getZ() + shape.min(Direction.Axis.Z), (double)pos.getZ() + shape.max(Direction.Axis.Z), y);
    }

    private void spawnFluidParticle(Level particleData, double x1, double x2, double z1, double z2, double y) {
        particleData.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(particleData.random.nextDouble(), x1, x2), y, Mth.lerp(particleData.random.nextDouble(), z1, z2), (double)0.0F, (double)0.0F, (double)0.0F);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{HONEY_LEVEL, FACING});
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BeeperNestBlockEntity(pos, state);
    }

    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, BlockEntityRegistry.BEEPER_NEST_BLOCK_ENTITY, BeeperNestBlockEntity::serverTick);
    }

    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BeeperNestBlockEntity beeperNestBlockEntity) {
                ItemStack itemStack = new ItemStack(this);
                int i = (Integer)state.getValue(HONEY_LEVEL);
                boolean bl = !beeperNestBlockEntity.isEmpty();
                if (bl || i > 0) {
                    if (bl) {
                        CompoundTag compoundTag = new CompoundTag();
                        compoundTag.put("Beepers", beeperNestBlockEntity.writeBeepers());
                        BlockItem.setBlockEntityData(itemStack, BlockEntityType.BEEHIVE, compoundTag);
                    }

                    CompoundTag compoundTag = new CompoundTag();
                    compoundTag.putInt("honey_level", i);
                    itemStack.addTagElement("BlockStateTag", compoundTag);
                    ItemEntity itemEntity = new ItemEntity(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), itemStack);
                    itemEntity.setDefaultPickUpDelay();
                    level.addFreshEntity(itemEntity);
                }
            }
        }

        super.playerWillDestroy(level, pos, state, player);
    }

    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        Entity entity = (Entity)params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if (entity instanceof PrimedTnt || entity instanceof Creeper || entity instanceof WitherSkull || entity instanceof WitherBoss || entity instanceof MinecartTNT) {
            BlockEntity blockEntity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
            if (blockEntity instanceof BeeperNestBlockEntity beeperNestBlockEntity) {
                beeperNestBlockEntity.emptyAllLivingFromHive((Player)null, state, BeeperNestBlockEntity.BeeperReleaseStatus.EMERGENCY);
            }
        }

        return super.getDrops(state, params);
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (level.getBlockState(neighborPos).getBlock() instanceof FireBlock) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof BeeperNestBlockEntity beeperNestBlockEntity) {
                beeperNestBlockEntity.emptyAllLivingFromHive((Player)null, state, BeeperNestBlockEntity.BeeperReleaseStatus.EMERGENCY);
            }
        }

        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY;
    }
}
