package net.qtpi.jeepersbeepers.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.effect.InvigorationEffect;

public class EffectRegistry {

    public static final MobEffect INVIGORATION = registerEffect("invigoration", new InvigorationEffect());

    private static MobEffect registerEffect(String name, MobEffect effect) {
        return Registry.register(BuiltInRegistries.MOB_EFFECT, new ResourceLocation(JeepersBeepers.MOD_ID, name), effect);
    }

    public static void registerModEffects() {
        JeepersBeepers.LOGGER.info("Registering Mod Effects for" + JeepersBeepers.MOD_ID);
    }
}
