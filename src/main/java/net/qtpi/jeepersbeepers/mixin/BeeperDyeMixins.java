package net.qtpi.jeepersbeepers.mixin;


import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DyeItem.class)
public abstract class BeeperDyeMixins {

    @Shadow @Final private DyeColor dyeColor;

    @Inject(at = @At("HEAD"), method = "interactLivingEntity", cancellable = true)
    private void interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand, CallbackInfoReturnable<InteractionResult> cir) {
        if (interactionTarget instanceof BeeperEntity beeper) {
            if (beeper.isAlive() && !beeper.isNaked()) {
                if (!beeper.hasColor() || beeper.getColor() != this.dyeColor) {
                    beeper.level().playSound(player, beeper, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.level().isClientSide) {
                        beeper.setColor(this.dyeColor);
                        stack.shrink(1);
                    }

                    cir.setReturnValue(InteractionResult.sidedSuccess(player.level().isClientSide));
                }
            }
        }
    }

}
