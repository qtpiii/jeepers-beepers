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
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;

public class DragonFruitTreeBlock extends CropBlock {
    public static final IntegerProperty AGE;
    protected static final VoxelShape[] SHAPE_BY_AGE;
    public DragonFruitTreeBlock(Properties properties) {
        super(properties);
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
        } else if (state.getValue(AGE) == 7) {
            int i = 4 + level.random.nextInt(2);
            for (int j = 0; j < i; ++j) {
                popResource(level, pos, new ItemStack(ItemRegistry.DRAGONFRUIT));
            }
            level.playSound(player, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.PLAYERS);
            level.setBlock(pos, this.getStateForAge(6), 2);
            return InteractionResult.SUCCESS;
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
                    level.setBlock(pos, this.getStateForAge(i + 1), 2);
                }
            } else if (i < 7) {
                float f = getGrowthSpeed(this, level, pos) / 2;
                if (random.nextInt((int)(25.0F / f) + 1) == 0) {
                    level.setBlock(pos, this.getStateForAge(i + 1), 2);
                }
            }
        }
    }

    protected ItemLike getBaseSeedId() {
        return ItemRegistry.DRAGONFRUIT_SEEDS;
    }

    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getBaseSeedId());
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < 6;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int i = Math.min(6, state.getValue(AGE) + Mth.nextInt(level.random, 1, 3));
        BlockState blockState = state.setValue(AGE, i);
        level.setBlock(pos, blockState, 2);
        if (i == 6) {
            blockState.randomTick(level, pos, level.random);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{AGE});
    }

    static {
        AGE = BlockStateProperties.AGE_7;
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
