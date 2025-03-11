package net.qtpi.jeepersbeepers.world;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> MIGNONETTE_PLACED_KEY = registerKey("mignonette_placed");
    public static final ResourceKey<PlacedFeature> FLOWERING_MIGNONETTE_PLACED_KEY = registerKey("flowering_mignonette_placed");
    public static final ResourceKey<PlacedFeature> SMALL_MIGNONETTE_PLACED_KEY = registerKey("small_mignonette_placed");
    public static final ResourceKey<PlacedFeature> PETRIFIED_MIGNONETTE_STUMP_PLACED_KEY = registerKey("petrified_mignonette_stump_placed");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        var configuredFeatureRegistryLookup = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, MIGNONETTE_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.MIGNONETTE_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(14, 0.1f, 2),
                        BlockRegistry.MIGNONETTE_SAPLING));
        register(context, FLOWERING_MIGNONETTE_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.FLOWERING_MIGNONETTE_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(8, 0.1f, 2),
                        BlockRegistry.FLOWERING_MIGNONETTE_SAPLING));
        register(context, SMALL_MIGNONETTE_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.SMALL_MIGNONETTE_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(6, 0.1f, 2),
                        BlockRegistry.MIGNONETTE_SAPLING));
        register(context, PETRIFIED_MIGNONETTE_STUMP_PLACED_KEY, configuredFeatureRegistryLookup.getOrThrow(ModConfiguredFeatures.PETRIFIED_MIGNONETTE_STUMP_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(1, 0.1f, 2),
                        BlockRegistry.MIGNONETTE_SAPLING));
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(JeepersBeepers.MOD_ID, name));
    }

    private static void register(BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> config,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(config, List.copyOf(modifiers)));
    }
}
