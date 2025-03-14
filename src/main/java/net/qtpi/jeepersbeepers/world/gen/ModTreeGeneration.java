package net.qtpi.jeepersbeepers.world.gen;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.qtpi.jeepersbeepers.world.ModPlacedFeatures;
import net.qtpi.jeepersbeepers.registry.BiomeRegistry;

public class ModTreeGeneration {
    public static void generateTrees() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeRegistry.MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MIGNONETTE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeRegistry.MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FLOWERING_MIGNONETTE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeRegistry.MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SMALL_MIGNONETTE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeRegistry.MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.PETRIFIED_MIGNONETTE_STUMP_PLACED_KEY);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeRegistry.BIRCH_MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.MIGNONETTE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeRegistry.BIRCH_MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.FLOWERING_MIGNONETTE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeRegistry.BIRCH_MIGNONETTE_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.SMALL_MIGNONETTE_PLACED_KEY);
    }
}
