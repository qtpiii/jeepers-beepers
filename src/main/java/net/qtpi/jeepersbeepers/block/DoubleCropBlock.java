package net.qtpi.jeepersbeepers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import org.jetbrains.annotations.Nullable;

public class DoubleCropBlock extends DoublePlantBlock implements BonemealableBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    public static final int MAX_AGE = 3;
    public static final int DOUBLE_PLANT_AGE_INTERSECTION = 2;
    private static final VoxelShape[] UPPER_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(1.0, 0.0, 1.0, 15.0, 6.0, 15.0),
            Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0)};
    private static final VoxelShape[] LOWER_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(1.0, -1.0, 1.0, 15.0, 4.0, 15.0),
            Block.box(1.0, -1.0, 1.0, 15.0, 12.0, 15.0),
            Block.box(1.0, -1.0, 1.0, 15.0, 16.0, 15.0),
            Block.box(1.0, -1.0, 1.0, 15.0, 16.0, 15.0)
    };
    public DoubleCropBlock(Properties properties) {
        super(properties);
    }


    private boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= MAX_AGE;
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !this.isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        float f = getGrowthSpeed(this, level, pos);
        boolean bl = random.nextInt((int)(25.0F / f) + 1) == 0;
        if (bl) {
            this.grow(level, state, pos, 1);
        }
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND) || state.is(BlockRegistry.LOAM_FARMLAND);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER
                ? UPPER_SHAPE_BY_AGE[Math.max(state.getValue(AGE) - 2, 0)]
                : LOWER_SHAPE_BY_AGE[state.getValue(AGE)];
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            level.destroyBlock(pos, true, entity);
        }

        super.entityInside(state, level, pos, entity);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos);
    }

    private static boolean sufficientLight(LevelReader level, BlockPos pos) {
        return level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos);
    }

    private boolean isLower(BlockState state) {
        return state.is(this) && state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    private boolean isUpper(BlockState state) {
        return state.is(this) && state.getValue(HALF) == DoubleBlockHalf.UPPER;
    }

    private boolean canGrowInto(LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        return blockState.isAir() || blockState.is(this);
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
        return !this.isMaxAge(state) && sufficientLight(reader, pos) && (state.getValue(AGE) > DOUBLE_PLANT_AGE_INTERSECTION || canGrowInto(reader, pos.above()));
    }

    @Nullable
    private PosAndState getLowerHalf(LevelReader level, BlockPos pos, BlockState state) {
        if (isLower(state)) {
            return new PosAndState(pos, state);
        } else {
            BlockPos blockPos = pos.below();
            BlockState blockState = level.getBlockState(blockPos);
            return isLower(blockState) ? new PosAndState(blockPos, blockState) : null;
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        PosAndState posAndState = this.getLowerHalf(level, pos, state);
        return posAndState != null && this.canGrow(level, posAndState.pos, posAndState.state, posAndState.state.getValue(AGE) + 1);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        PosAndState posAndState = this.getLowerHalf(level, pos, state);
        if (posAndState != null) {
            this.grow(level, posAndState.state, posAndState.pos, 1);
        }
    }

    public static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockPos = pos.below();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                float g = 0.0F;
                BlockState blockState = level.getBlockState(blockPos.offset(i, 0, j));
                if (blockState.is(Blocks.FARMLAND) || blockState.is(BlockRegistry.LOAM_FARMLAND)) {
                    g = 1.0F;
                    if (blockState.getValue(FarmBlock.MOISTURE) > 0) {
                        g = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    g /= 4.0F;
                }

                f += g;
            }
        }

        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = level.getBlockState(blockPos4).is(block) || level.getBlockState(blockPos5).is(block);
        boolean bl2 = level.getBlockState(blockPos2).is(block) || level.getBlockState(blockPos3).is(block);
        if (bl && bl2) {
            f /= 2.0F;
        } else {
            boolean bl3 = level.getBlockState(blockPos4.north()).is(block)
                    || level.getBlockState(blockPos5.north()).is(block)
                    || level.getBlockState(blockPos5.south()).is(block)
                    || level.getBlockState(blockPos4.south()).is(block);
            if (bl3) {
                f /= 2.0F;
            }
        }

        return f;
    }

    static record PosAndState(BlockPos pos, BlockState state) {
    }
}
