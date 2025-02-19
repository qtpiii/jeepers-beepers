package net.qtpi.jeepersbeepers.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class InvigorationEffect extends MobEffect {
    public InvigorationEffect() {
        super(MobEffectCategory.BENEFICIAL, 16726022);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.getCommandSenderWorld().isClientSide && entity instanceof Player player) {
            super.addAttributeModifier(Attributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 0.3D * (float)(amplifier + 1), AttributeModifier.Operation.MULTIPLY_TOTAL);
            super.addAttributeModifier(Attributes.MOVEMENT_SPEED, "91AEAA56-376B-4498-935B-2F7F68070635", 0.3D * (float)(amplifier + 1), AttributeModifier.Operation.MULTIPLY_TOTAL);
            player.causeFoodExhaustion(0.025F * (float)(amplifier + 1));
        }
    }
}
