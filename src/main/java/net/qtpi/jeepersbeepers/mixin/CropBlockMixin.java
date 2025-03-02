package net.qtpi.jeepersbeepers.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {

    @Inject(at = @At("HEAD"), method = "mayPlaceOn", cancellable = true)
    private void mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(BlockRegistry.LOAM_FARMLAND)) {
            cir.setReturnValue(true);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"), method = "getGrowthSpeed")
    private static boolean getGrowthSpeed(BlockState instance, Block block) {
        if (block == Blocks.FARMLAND) {
            return instance.is(Blocks.FARMLAND) || instance.is(BlockRegistry.LOAM_FARMLAND);
        }
        return instance.is(block);
    }
}
