package net.qtpi.jeepersbeepers.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class ButterdewSquashBlock extends StemGrownBlock {
    protected static final VoxelShape BOTTOM_SHAPE;
    protected static final VoxelShape TOP_SHAPE;
    protected static final VoxelShape SHAPE;
    public ButterdewSquashBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS)) {
            if (!level.isClientSide) {
                Direction direction = hit.getDirection();
                Direction direction2 = direction.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : direction;
                level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (level.random.nextFloat() < 0.002F) {
                    level.setBlock(pos, BlockRegistry.CAWVED_BUTTEWDEW_SQUASH.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction2), 11);
                } else {
                    level.setBlock(pos, BlockRegistry.CARVED_BUTTERDEW_SQUASH.defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction2), 11);
                }
                ItemEntity itemEntity = new ItemEntity(level, (double)pos.getX() + (double)0.5F + (double)direction2.getStepX() * 0.65, (double)pos.getY() + 0.1, (double)pos.getZ() + (double)0.5F + (double)direction2.getStepZ() * 0.65, new ItemStack(ItemRegistry.BUTTERDEW_SQUASH_SEEDS, 4));
                itemEntity.setDeltaMovement(0.05 * (double)direction2.getStepX() + level.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction2.getStepZ() + level.random.nextDouble() * 0.02);
                level.addFreshEntity(itemEntity);
                itemStack.hurtAndBreak(1, player, (playerx) -> playerx.broadcastBreakEvent(hand));
                level.gameEvent(player, GameEvent.SHEAR, pos);
                player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.use(state, level, pos, player, hand, hit);
        }
    }

    @Override
    public @NotNull StemBlock getStem() {
        return (StemBlock) BlockRegistry.BUTTERDEW_SQUASH_STEM;
    }

    @Override
    public @NotNull AttachedStemBlock getAttachedStem() {
        return (AttachedStemBlock) BlockRegistry.ATTACHED_BUTTERDEW_SQUASH_STEM;
    }

    static {
        BOTTOM_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D);
        TOP_SHAPE = Block.box(2.0D, 10.0D, 2.0D, 14.0D, 16.0D, 14.0D);
        SHAPE = Shapes.or(BOTTOM_SHAPE, TOP_SHAPE);
    }
}
