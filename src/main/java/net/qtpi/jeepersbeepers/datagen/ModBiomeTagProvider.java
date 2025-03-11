package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.qtpi.jeepersbeepers.registry.BiomeRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends FabricTagProvider<Biome> {
    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BIOME, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BiomeTags.IS_FOREST)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);
        getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);

        getOrCreateTagBuilder(ConventionalBiomeTags.CLIMATE_TEMPERATE)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.FLORAL)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.FOREST)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.IN_OVERWORLD)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.TREE_DECIDUOUS)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.VEGETATION_DENSE)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);

        getOrCreateTagBuilder(TagRegistry.Misc.MIGNONETTE_FORESTS)
                .add(BiomeRegistry.MIGNONETTE_FOREST)
                .add(BiomeRegistry.BIRCH_MIGNONETTE_FOREST);
    }
}
