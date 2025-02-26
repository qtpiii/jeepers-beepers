package net.qtpi.jeepersbeepers.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraft.world.level.block.state.BlockState;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PistonStructureResolver.class)
public class PistonStructureResolverMixin {

    @Inject(at = @At("HEAD"), method = "isSticky", cancellable = true)
    private static void isSticky(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.is(BlockRegistry.SPICY_HONEY_BLOCK)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(at = @At("HEAD"), method = "canStickToEachOther", cancellable = true)
    private static void canStickToEachOther(BlockState state1, BlockState state2, CallbackInfoReturnable<Boolean> cir) {
        if (state1.is(Blocks.SLIME_BLOCK) && state2.is(BlockRegistry.SPICY_HONEY_BLOCK)) {
            cir.setReturnValue(false);
        }
        if (state1.is(BlockRegistry.SPICY_HONEY_BLOCK) && state2.is(Blocks.SLIME_BLOCK)) {
            cir.setReturnValue(false);
        }
        if (state1.is(Blocks.HONEY_BLOCK) && state2.is(BlockRegistry.SPICY_HONEY_BLOCK)) {
            cir.setReturnValue(false);
        }
        if (state1.is(BlockRegistry.SPICY_HONEY_BLOCK) && state2.is(Blocks.HONEY_BLOCK)) {
            cir.setReturnValue(false);
        }
    }
}
