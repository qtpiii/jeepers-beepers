package net.qtpi.jeepersbeepers.world.tree.custom;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.qtpi.jeepersbeepers.registry.WorldGenRegistry;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PetrifiedMignonetteRootPlacer extends TrunkPlacer {
    public static final Codec<PetrifiedMignonetteRootPlacer> CODEC = RecordCodecBuilder.create(objectInstance ->
            trunkPlacerParts(objectInstance).apply(objectInstance, PetrifiedMignonetteRootPlacer::new));

    public PetrifiedMignonetteRootPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return WorldGenRegistry.PETRIFIED_MIGNONETTE_ROOT_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.above(), config);
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        int height_ = baseHeight + random.nextIntBetweenInclusive(heightRandA, heightRandB);

        for (int i = 0; i < height_; i++) {
            placeLog(level, blockSetter, random, pos.below(i), config);
        }
        return list;
    }
}
