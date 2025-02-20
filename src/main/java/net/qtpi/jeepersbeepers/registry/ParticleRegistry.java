package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.JeepersBeepers;

public class ParticleRegistry {

    public static final SimpleParticleType BEEPER_SNEEZE_POOF = registerParticle("beeper_sneeze_poof", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particle) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(JeepersBeepers.MOD_ID, name), particle);
    }
}
