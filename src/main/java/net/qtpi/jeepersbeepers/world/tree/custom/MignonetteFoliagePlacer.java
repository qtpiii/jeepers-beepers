package net.qtpi.jeepersbeepers.world.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.qtpi.jeepersbeepers.registry.WorldGenRegistry;

public class MignonetteFoliagePlacer extends FoliagePlacer {
    public static final Codec<MignonetteFoliagePlacer> CODEC = RecordCodecBuilder.create(mignonetteFoliagePlacerInstance ->
            foliagePlacerParts(mignonetteFoliagePlacerInstance).apply(mignonetteFoliagePlacerInstance, MignonetteFoliagePlacer::new));
    public MignonetteFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return WorldGenRegistry.MIGNONETTE_FOLIAGE_PLACER;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        int radius_ = radius.sample(random) + attachment.radiusOffset();
        placeLeafSphere(level, blockSetter, random, config, attachment.pos(), radius_, attachment.doubleTrunk());
    }

    protected void placeLeafSphere(
            LevelSimulatedReader level,
            FoliagePlacer.FoliageSetter foliageSetter,
            RandomSource random,
            TreeConfiguration treeConfiguration,
            BlockPos pos,
            int range,
            boolean large
    ) {
        int i = large ? 1 : 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

        for (int j = -range; j <= range + i; j++) {
            for (int k = -range; k <= range + i; k++) {
                for (int l = -range; l <= range + i; l++) {
                    if (!this.shouldSkipLocationSigned(random, j, k, l, range, large)) {
                        mutableBlockPos.setWithOffset(pos, j, k, l);
                        if (mutableBlockPos.distManhattan(new Vec3i(pos.getX(), pos.getY(), pos.getZ())) <= range + 1) {
                            tryPlaceLeaf(level, foliageSetter, random, treeConfiguration, mutableBlockPos);
                        }
                    }
                }
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}
