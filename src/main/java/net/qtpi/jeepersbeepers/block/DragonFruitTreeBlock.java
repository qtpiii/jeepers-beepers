package net.qtpi.jeepersbeepers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"NullableProblems", "deprecation", "RedundantArrayCreation"})
public class DragonFruitTreeBlock extends DoubleCropBlock {
    private static final VoxelShape[] UPPER_TRUNK_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0, 0.0, 6.0, 10.0, 7.0, 10.0),
            Block.box(6.0, 0.0, 6.0, 10.0, 7.0, 10.0),
            Block.box(6.0, 0.0, 6.0, 10.0, 15.0, 10.0),
            Block.box(6.0, 0.0, 6.0, 10.0, 15.0, 10.0)
    };
    private static final VoxelShape[] LOWER_TRUNK_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0D, -1.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(6.0D, -1.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(6.0D, -1.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(6.0D, -1.0D, 6.0D, 10.0D, 16.0D, 10.0D)
    };
    private static final VoxelShape[] UPPER_LEAVES_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0),
            Block.box(2.0, 0.0, 2.0, 14.0, 14.0, 14.0)
    };
    private static final VoxelShape[] LOWER_LEAVES_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0D, -1.0D, 6.0D, 10.0D, 3.0D, 10.0D),
            Block.box(6.0D, -1.0D, 6.0D, 10.0D, 3.0D, 10.0D),
            Block.box(4.0D, -1.0D, 4.0D, 12.0D, 11.0D, 12.0D),
            Block.box(4.0D, -1.0D, 4.0D, 12.0D, 11.0D, 12.0D),
            Block.box(1.0D, 6.0D, 1.0D, 15.0D, 16.0D, 15.0D),
            Block.box(1.0D, 6.0D, 1.0D, 15.0D, 16.0D, 15.0D)
    };
    private static final VoxelShape[] LOWER_SHAPE_BY_AGE = new VoxelShape[]{
            LOWER_LEAVES_SHAPE_BY_AGE[0],
            LOWER_LEAVES_SHAPE_BY_AGE[1],
            LOWER_LEAVES_SHAPE_BY_AGE[2],
            LOWER_LEAVES_SHAPE_BY_AGE[3],
            Shapes.or(LOWER_LEAVES_SHAPE_BY_AGE[4], LOWER_TRUNK_SHAPE_BY_AGE[0]),
            Shapes.or(LOWER_LEAVES_SHAPE_BY_AGE[5], LOWER_TRUNK_SHAPE_BY_AGE[1]),
            LOWER_TRUNK_SHAPE_BY_AGE[2],
            LOWER_TRUNK_SHAPE_BY_AGE[3]
    };
    private static final VoxelShape[] UPPER_SHAPE_BY_AGE = new VoxelShape[]{
            Shapes.or(UPPER_LEAVES_SHAPE_BY_AGE[0], UPPER_TRUNK_SHAPE_BY_AGE[0]),
            Shapes.or(UPPER_LEAVES_SHAPE_BY_AGE[1], UPPER_TRUNK_SHAPE_BY_AGE[1]),
            Shapes.or(UPPER_LEAVES_SHAPE_BY_AGE[2], UPPER_TRUNK_SHAPE_BY_AGE[2]),
            Shapes.or(UPPER_LEAVES_SHAPE_BY_AGE[3], UPPER_TRUNK_SHAPE_BY_AGE[3])
    };
    public DragonFruitTreeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER
                ? UPPER_TRUNK_SHAPE_BY_AGE[Math.max(state.getValue(AGE) - 4, 0)]
                : LOWER_TRUNK_SHAPE_BY_AGE[Math.max(state.getValue(AGE) - 4, 0)];
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER
                ? UPPER_SHAPE_BY_AGE[Math.max(state.getValue(AGE) - 4, 0)]
                : LOWER_SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (state.is(this) && state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            if (itemStack.is(Items.BONE_MEAL)) {
                return InteractionResult.PASS;
            } else if (state.getValue(AGE) == MAX_AGE) {
                int i = 2 + level.random.nextInt(2);
                for (int j = 0; j < i; ++j) {
                    popResource(level, pos, new ItemStack(ItemRegistry.DRAGONFRUIT));
                    if (level.getBlockState(pos.below(2)).is(BlockRegistry.LOAM_FARMLAND)) {
                        popResource(level, pos, new ItemStack(ItemRegistry.DRAGONFRUIT));
                    }
                }
                level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS);
                level.setBlock(
                        pos, copyWaterloggedFrom(level, pos, this.defaultBlockState().setValue(AGE, 6).setValue(HALF, DoubleBlockHalf.UPPER)), 3
                );
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.CONSUME;
            }
        } else {
            return InteractionResult.CONSUME;
        }
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < 6) {
                float f = getGrowthSpeed(this, level, pos);
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    this.grow(level, state, pos, 1);
                }
            } else if (i < 7) {
                float f = getGrowthSpeed(this, level, pos) / 2;
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    this.grow(level, state, pos, 1);
                }
            }
        }
    }

    private void grow(ServerLevel level, BlockState state, BlockPos pos, int ageIncrement) {
        int i = Math.min(state.getValue(AGE) + ageIncrement, MAX_AGE);
        if (this.canGrow(level, pos, state, i)) {
            level.setBlock(pos, state.setValue(AGE, i), 2);
            if (i >= DOUBLE_PLANT_AGE_INTERSECTION) {
                BlockPos blockPos = pos.above();
                level.setBlock(
                        blockPos, copyWaterloggedFrom(level, pos, this.defaultBlockState().setValue(AGE, i).setValue(HALF, DoubleBlockHalf.UPPER)), 3
                );
            }
        }
    }

    private boolean canGrow(LevelReader reader, BlockPos pos, BlockState state, int age) {
        return !(state.getValue(AGE) == MAX_AGE) && sufficientLight(reader, pos) && (state.getValue(AGE) > DOUBLE_PLANT_AGE_INTERSECTION || canGrowInto(reader, pos.above()));
    }

    private static boolean sufficientLight(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
    }

    private boolean canGrowInto(LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        return blockState.isAir() || blockState.is(this);
    }

    protected ItemLike getBaseSeedId() {
        return ItemRegistry.DRAGONFRUIT_SEEDS;
    }

    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getBaseSeedId());
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }
}
