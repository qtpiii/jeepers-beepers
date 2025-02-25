package net.qtpi.jeepersbeepers.mixin;

import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.qtpi.jeepersbeepers.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectUtil.class)
public class MobEffectUtilMixin {

    @Inject(at = @At("HEAD"), method = "hasDigSpeed", cancellable = true)
    private static void hasDigSpeed(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasEffect(EffectRegistry.INVIGORATION))  cir.setReturnValue(true);
    }

    @Inject(at = @At("HEAD"), method = "getDigSpeedAmplification", cancellable = true)
    private static void getDigSpeedAmplification(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity.hasEffect(MobEffects.DIG_SPEED) || entity.hasEffect(MobEffects.CONDUIT_POWER)) return;
        if (entity.hasEffect(EffectRegistry.INVIGORATION)) {
            cir.setReturnValue(entity.getEffect(EffectRegistry.INVIGORATION).getAmplifier() + 1);
        }
    }

}
