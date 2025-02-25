package net.qtpi.jeepersbeepers.world.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.qtpi.jeepersbeepers.world.ModConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

public class MignonetteTreeGrower extends AbstractTreeGrower {
    @Override
    protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
        if (random.nextFloat() < 0.1) {
            return ModConfiguredFeatures.SMALL_MIGNONETTE_KEY;
        }
        return ModConfiguredFeatures.MIGNONETTE_KEY;
    }
}
