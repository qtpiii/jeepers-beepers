package net.qtpi.jeepersbeepers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;

public class BraddishBlock extends CropBlock {
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] COLLISION_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(5.0, -1.0, 5.0, 11.0, 3.0, 11.0),
            Block.box(4.0, -1.0, 4.0, 12.0, 4.0, 12.0),
            Block.box(4.0, -1.0, 4.0, 12.0, 4.0, 12.0),
            Block.box(3.0, -1.0, 3.0, 13.0, 5.0, 13.0)
    };
    private static final VoxelShape[] TOP_SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0, 3.0, 6.0, 10.0, 10.0, 10.0),
            Block.box(5.0, 4.0, 5.0, 11.0, 12.0, 11.0),
            Block.box(4.0, 4.0, 4.0, 12.0, 14.0, 12.0),
            Block.box(3.0, 5.0, 3.0, 13.0, 16.0, 13.0)
    };
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Shapes.or(COLLISION_SHAPE_BY_AGE[0], TOP_SHAPE_BY_AGE[0]),
            Shapes.or(COLLISION_SHAPE_BY_AGE[1], TOP_SHAPE_BY_AGE[1]),
            Shapes.or(COLLISION_SHAPE_BY_AGE[2], TOP_SHAPE_BY_AGE[2]),
            Shapes.or(COLLISION_SHAPE_BY_AGE[3], TOP_SHAPE_BY_AGE[3])
    };
    public BraddishBlock(Properties properties) {
        super(properties);
    }

    @Override
    public int getMaxAge() {
        return MAX_AGE;
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ItemRegistry.BRADDISH_SEEDS;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}
