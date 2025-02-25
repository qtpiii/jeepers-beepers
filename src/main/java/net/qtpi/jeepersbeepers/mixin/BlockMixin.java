package net.qtpi.jeepersbeepers.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.PitcherCropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.Block.dropResources;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(at = @At("TAIL"), method = "playerDestroy")
    private void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (state.getBlock() instanceof BushBlock || state.getBlock() instanceof PitcherCropBlock) {
            boolean shouldDropDouble = false;
            if (state.getBlock() instanceof CropBlock || state.getBlock() instanceof PitcherCropBlock) {
                CropBlock block = (CropBlock) state.getBlock();
                if (block.isMaxAge(state)) {
                    shouldDropDouble = true;
                }
            } else { shouldDropDouble = true; }
            BlockPos blockBelow = pos.below();
            if (level.getBlockState(blockBelow).getBlock() == BlockRegistry.LOAM_FARMLAND && shouldDropDouble) {
                dropResources(state, level, pos, blockEntity, player, tool);
            }
        }
    }
}
