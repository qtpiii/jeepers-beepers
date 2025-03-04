package net.qtpi.jeepersbeepers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.block.entity.MignonetteRootBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MignonetteRootBlock extends BaseEntityBlock implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.create(0, 0.9, 0, 1, 1.0, 1);
    public static final IntegerProperty ROOTS = IntegerProperty.create("roots", 1, 8);

    public MignonetteRootBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState().setValue(ROOTS, 1));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MignonetteRootBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROOTS);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().getItem() == this.asItem() && state.getValue(ROOTS) < 7
                ? true
                : super.canBeReplaced(state, useContext);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.is(this)) {
            return blockState.cycle(ROOTS);
        }
        else {
            return super.getStateForPlacement(context);
        }
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!canSurvive(state, level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        else {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState blockState = level.getBlockState(blockPos);
        return blockState.isCollisionShapeFullBlock(level, blockPos) && !blockState.is(this);
    }
}
