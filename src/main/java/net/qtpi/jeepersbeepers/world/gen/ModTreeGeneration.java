package net.qtpi.jeepersbeepers.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.qtpi.jeepersbeepers.world.ModPlacedFeatures;
import net.qtpi.jeepersbeepers.world.biome.ModBiomes;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MIGNONETTE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FLOWERING_MIGNONETTE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(ModBiomes.MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SMALL_MIGNONETTE_PLACED_KEY);
    }
}
