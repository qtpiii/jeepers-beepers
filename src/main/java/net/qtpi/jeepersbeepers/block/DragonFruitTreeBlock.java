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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;

import java.util.function.Supplier;

public class DragonFruitTreeBlock extends BushBlock implements BonemealableBlock {
    public static final IntegerProperty AGE;
    public static final BooleanProperty HAS_FRUIT;
    protected static final VoxelShape[] SHAPE_BY_AGE;
    private final Supplier<Item> seedSupplier;
    public DragonFruitTreeBlock(Supplier<Item> seedSupplier, Properties properties) {
        super(properties);
        this.seedSupplier = seedSupplier;
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[(Integer)state.getValue(AGE)];
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.FARMLAND);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (state.getValue(HAS_FRUIT)) {
            int i = 4 + level.random.nextInt(2);
            for (int j = 0; j < i; ++j) {
                popResource(level, pos, new ItemStack(ItemRegistry.DRAGONFRUIT));
            }
            level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS);
            state.setValue(HAS_FRUIT, false);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            float f = getGrowthSpeed(this, level, pos);
            if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                int i = state.getValue(AGE);
                if (i < 7) {
                    state = state.setValue(AGE, i + 1);
                    level.setBlock(pos, state, 2);
                } else {
                    state = state.setValue(HAS_FRUIT, true);
                    level.setBlock(pos, state, 2);
                }
            }

        }
    }

    protected static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockPos = pos.below();

        for(int i = -1; i <= 1; ++i) {
            for(int j = -1; j <= 1; ++j) {
                float g = 0.0F;
                BlockState blockState = level.getBlockState(blockPos.offset(i, 0, j));
                if (blockState.is(Blocks.FARMLAND)) {
                    g = 1.0F;
                    if ((Integer)blockState.getValue(FarmBlock.MOISTURE) > 0) {
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
            boolean bl3 = level.getBlockState(blockPos4.north()).is(block) || level.getBlockState(blockPos5.north()).is(block) || level.getBlockState(blockPos5.south()).is(block) || level.getBlockState(blockPos4.south()).is(block);
            if (bl3) {
                f /= 2.0F;
            }
        }

        return f;
    }

    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this.seedSupplier.get());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) != 7;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = Math.min(7, state.getValue(AGE) + Mth.nextInt(level.random, 1, 3));
        BlockState blockState = state.setValue(AGE, i);
        level.setBlock(pos, blockState, 2);
        if (i == 7) {
            blockState.randomTick(level, pos, level.random);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE, HAS_FRUIT});
    }

    static {
        AGE = BlockStateProperties.AGE_7;
        HAS_FRUIT = BooleanProperty.create("has_fruit");
        SHAPE_BY_AGE = new VoxelShape[]{
                Block.box(6.0D, -1.0D, 6.0D, 10.0D, 3.0D, 10.0D),
                Block.box(6.0D, -1.0D, 6.0D, 10.0D, 3.0D, 10.0D),
                Block.box(4.0D, -1.0D, 4.0D, 12.0D, 11.0D, 12.0D),
                Block.box(4.0D, -1.0D, 4.0D, 12.0D, 11.0D, 12.0D),
                Block.box(6.0D, -1.0D, 6.0D, 10.0D, 19.0D, 10.0D),
                Block.box(6.0D, -1.0D, 6.0D, 10.0D, 19.0D, 10.0D),
                Block.box(4.0D, -1.0D, 4.0D, 12.0D, 21.0D, 12.0D),
                Block.box(4.0D, -1.0D, 4.0D, 12.0D, 21.0D, 12.0D)
        };
    }
}
