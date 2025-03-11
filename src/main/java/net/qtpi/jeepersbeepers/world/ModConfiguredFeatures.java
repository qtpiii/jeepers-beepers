package net.qtpi.jeepersbeepers.world;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.AlterGroundDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.AttachedToLeavesDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.block.WallFlowerBlock;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.world.tree.custom.BeeperNestDecorator;
import net.qtpi.jeepersbeepers.world.tree.custom.MignonetteFoliagePlacer;
import net.qtpi.jeepersbeepers.world.tree.custom.MignonetteTrunkPlacer;
import net.qtpi.jeepersbeepers.world.tree.custom.PetrifiedMignonetteStumpPlacer;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> MIGNONETTE_KEY = registerKey("mignonette");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWERING_MIGNONETTE_KEY = registerKey("flowering_mignonette");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_MIGNONETTE_KEY = registerKey("small_mignonette");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_MIGNONETTE_STUMP_KEY = registerKey("petrified_mignonette_stump");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Block> holderGetter = context.lookup(Registries.BLOCK);
        register(context, MIGNONETTE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_LOG),
                new MignonetteTrunkPlacer(5, 2, 3),

                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_LEAVES),
                new MignonetteFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)),

                new TwoLayersFeatureSize(1, 0, 2))
                .build());

        register(context, FLOWERING_MIGNONETTE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_LOG),
                new MignonetteTrunkPlacer(5, 2, 3),

                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_LEAVES),
                new MignonetteFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)),

                new TwoLayersFeatureSize(1, 0, 2))
                .decorators(List.of(
                        new AttachedToLeavesDecorator(0.25F, 1, 1,
                                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_FLOWER.defaultBlockState().setValue(WallFlowerBlock.FACING, Direction.EAST)),
                                5, List.of(Direction.EAST)),
                        new AttachedToLeavesDecorator(0.25F, 1, 1,
                                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_FLOWER.defaultBlockState().setValue(WallFlowerBlock.FACING, Direction.WEST)),
                                5, List.of(Direction.WEST)),
                        new AttachedToLeavesDecorator(0.25F, 1, 1,
                                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_FLOWER.defaultBlockState().setValue(WallFlowerBlock.FACING, Direction.SOUTH)),
                                5, List.of(Direction.SOUTH)),
                        new AttachedToLeavesDecorator(0.25F, 1, 1,
                                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_FLOWER.defaultBlockState().setValue(WallFlowerBlock.FACING, Direction.NORTH)),
                                5, List.of(Direction.NORTH)))).build());

        register(context, SMALL_MIGNONETTE_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_LOG),
                new ForkingTrunkPlacer(4, 1, 2),

                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_LEAVES),
                new MignonetteFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)),

                new TwoLayersFeatureSize(1, 0, 2))
                .decorators(List.of(new AlterGroundDecorator(BlockStateProvider.simple(BlockRegistry.LOAM)))).build());

        register(context, PETRIFIED_MIGNONETTE_STUMP_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(BlockRegistry.PETRIFIED_MIGNONETTE_LOG),
                new PetrifiedMignonetteStumpPlacer(0, 1, 3),

                BlockStateProvider.simple(BlockRegistry.MIGNONETTE_LEAVES),
                new MignonetteFoliagePlacer(ConstantInt.of(1), ConstantInt.of(0)),

                new TwoLayersFeatureSize(1, 0, 1))
                .decorators(List.of(new AlterGroundDecorator(BlockStateProvider.simple(BlockRegistry.LOAM)))).build());
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(JeepersBeepers.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
