package net.qtpi.jeepersbeepers.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WallFlowerBlock extends Block implements BonemealableBlock, SuspiciousEffectHolder {
    public static final DirectionProperty FACING;
    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE;
    MobEffect suspiciousEffect;
    int suspiciousEffectDuration;
    public WallFlowerBlock(MobEffect suspiciousEffect, int suspiciousEffectDuration, Properties properties) {
        super(properties);
        this.suspiciousEffect = suspiciousEffect;
        if (suspiciousEffect.isInstantenous()) {
            this.suspiciousEffectDuration = suspiciousEffectDuration;
        } else {
            this.suspiciousEffectDuration = suspiciousEffectDuration * 20;
        }
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return FACING_TO_SHAPE.get(state.getValue(FACING));
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (!state.canSurvive(level, currentPos)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        }
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockPos = pos.relative(direction.getOpposite());
        BlockState blockState = level.getBlockState(blockPos);
        return blockState.isCollisionShapeFullBlock(level, blockPos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = super.getStateForPlacement(ctx);
        LevelReader levelReader = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();

        Direction[] directions = ctx.getNearestLookingDirections();
        Direction[] var6 = directions;
        int var7 = directions.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            Direction direction = var6[var8];
            if (blockState != null && direction.getAxis().isHorizontal()) {
                blockState = blockState.setValue(FACING, direction.getOpposite());
                if (blockState.canSurvive(levelReader, blockPos)) {
                    return blockState;
                }
            }
        }
        return null;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        FACING_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(
                Direction.NORTH, Block.box(2.0D, 2.0D, 5.0D, 14.0D, 14.0D, 16.0D),
                Direction.SOUTH, Block.box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 11.0D),
                Direction.WEST, Block.box(5.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D),
                Direction.EAST, Block.box(0.0D, 2.0D, 2.0D, 11.0D, 14.0D, 14.0D)));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        popResource(level, pos, new ItemStack(this));
    }

    @Override
    public MobEffect getSuspiciousEffect() {
        return suspiciousEffect;
    }

    @Override
    public int getEffectDuration() {
        return suspiciousEffectDuration;
    }
}
