package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.qtpi.jeepersbeepers.registry.BiomeRegistry;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends FabricTagProvider<Biome> {
    public ModBiomeTagProvider(FabricDataOutput output, ResourceKey<? extends Registry<Biome>> registryKey, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registryKey, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BiomeTags.IS_FOREST)
                .add(BiomeRegistry.MIGNONETTE_FOREST);
        getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
                .add(BiomeRegistry.MIGNONETTE_FOREST);

        getOrCreateTagBuilder(ConventionalBiomeTags.CLIMATE_TEMPERATE)
                .add(BiomeRegistry.MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.FLORAL)
                .add(BiomeRegistry.MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.FOREST)
                .add(BiomeRegistry.MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.IN_OVERWORLD)
                .add(BiomeRegistry.MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.TREE_DECIDUOUS)
                .add(BiomeRegistry.MIGNONETTE_FOREST);
        getOrCreateTagBuilder(ConventionalBiomeTags.VEGETATION_DENSE)
                .add(BiomeRegistry.MIGNONETTE_FOREST);
    }
}
