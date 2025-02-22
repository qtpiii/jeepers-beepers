package net.qtpi.jeepersbeepers.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.qtpi.jeepersbeepers.JeepersBeepers;

public class SoundRegistry {

    public static final SoundEvent BEEPER_SNEEZE = registerSoundEvent("beeper_sneeze");
    public static final SoundEvent OPTIMUS_PRIME = registerSoundEvent("optimus_prime");
    public static final SoundEvent BEEPER_DIE = registerSoundEvent("beeper_die");

    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(JeepersBeepers.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerModSounds() {
        JeepersBeepers.LOGGER.info("Registering Mod Items for " + JeepersBeepers.MOD_ID);
    }
}
