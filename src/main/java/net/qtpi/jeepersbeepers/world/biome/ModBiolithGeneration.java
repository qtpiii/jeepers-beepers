package net.qtpi.jeepersbeepers.world.biome;

import com.terraformersmc.biolith.api.biome.BiomePlacement;
import net.minecraft.world.level.biome.Biomes;
import net.qtpi.jeepersbeepers.registry.BiomeRegistry;

public class ModBiolithGeneration {

    public static void addBiomes() {
        BiomePlacement.replaceOverworld(Biomes.FOREST, BiomeRegistry.MIGNONETTE_FOREST, 0.3D);
        BiomePlacement.replaceOverworld(Biomes.BIRCH_FOREST, BiomeRegistry.MIGNONETTE_FOREST, 0.3D);
    }

    public static void init() {
        addBiomes();
    }
}
