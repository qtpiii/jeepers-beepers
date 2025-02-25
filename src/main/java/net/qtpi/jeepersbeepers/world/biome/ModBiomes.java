package net.qtpi.jeepersbeepers.world.biome;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Musics;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.qtpi.jeepersbeepers.JeepersBeepers;

import static net.minecraft.data.worldgen.biome.OverworldBiomes.calculateSkyColor;

public class ModBiomes {
    public static final ResourceKey<Biome> MIGNONETTE_FOREST = ResourceKey.create(Registries.BIOME,
            new ResourceLocation(JeepersBeepers.MOD_ID, "mignonette_forest"));

    public static void bootstrap(BootstapContext<Biome> context) {
        context.register(MIGNONETTE_FOREST, mignonetteForest(context));
    }

    public static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    public static Biome mignonetteForest(BootstapContext<Biome> context) {
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 4, 1, 2));

        BiomeDefaultFeatures.farmAnimals(spawnBuilder);
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);

        BiomeGenerationSettings.Builder biomeBuilder =
                new BiomeGenerationSettings.Builder(context.lookup(Registries.PLACED_FEATURE),
                        context.lookup(Registries.CONFIGURED_CARVER));

        globalOverworldGeneration(biomeBuilder);

        BiomeDefaultFeatures.addFerns(biomeBuilder);
        BiomeDefaultFeatures.addForestFlowers(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);

        BiomeDefaultFeatures.addBirchTrees(biomeBuilder);

        BiomeDefaultFeatures.addForestGrass(biomeBuilder);

        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeBuilder);


        return new Biome.BiomeBuilder()
                .hasPrecipitation(true)
                .downfall(0.5f)
                .temperature(0.4f)
                .generationSettings(biomeBuilder.build())
                .mobSpawnSettings(spawnBuilder.build())
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(4566514)
                        .waterFogColor(267827)
                        .fogColor(12638463)
                        .skyColor(calculateSkyColor(0.4f))
                        .grassColorOverride(8100713)
                        .foliageColorOverride(8100713)
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(Musics.GAME).build())
                .build();
    }
}
