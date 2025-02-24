package net.qtpi.jeepersbeepers.world.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.qtpi.jeepersbeepers.registry.FoliagePlacerRegistry;

public class MignonetteFoliagePlacer extends FoliagePlacer {
    public static final Codec<MignonetteFoliagePlacer> CODEC = RecordCodecBuilder.create(mignonetteFoliagePlacerInstance ->
            foliagePlacerParts(mignonetteFoliagePlacerInstance).apply(mignonetteFoliagePlacerInstance, MignonetteFoliagePlacer::new));
    public MignonetteFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return FoliagePlacerRegistry.MIGNONETTE_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        int radius_sample = radius.sample(random);
        int height_ = radius_sample * 2 + attachment.radiusOffset();
        int radius_ = radius_sample + attachment.radiusOffset();
        for (int i = 0; i <= height_ ; i++) {
            if (i == 0) {
                placeLeavesRow(level, blockSetter, random, config, attachment.pos().above(i), radius_ - 1, offset - 1, attachment.doubleTrunk());
            } else if (i == height_) {
                placeLeavesRow(level, blockSetter, random, config, attachment.pos().above(i), radius_ - 1, offset - 1, attachment.doubleTrunk());
            } else {
                placeLeavesRow(level, blockSetter, random, config, attachment.pos().above(i), radius_, offset - 1, attachment.doubleTrunk());
            }

        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return localX == range && localZ == range && range > 0;
    }
}
