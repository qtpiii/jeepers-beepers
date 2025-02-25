package net.qtpi.jeepersbeepers.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttachedStemBlock.class)
public class AttachedStemBlockMixin {

    @Inject(at = @At("HEAD"), method = "mayPlaceOn", cancellable = true)
    private void mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(BlockRegistry.LOAM_FARMLAND)) {
            cir.setReturnValue(true);
        }
    }
}
