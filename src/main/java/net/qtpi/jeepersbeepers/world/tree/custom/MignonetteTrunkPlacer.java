package net.qtpi.jeepersbeepers.world.tree.custom;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.qtpi.jeepersbeepers.registry.TrunkPlacerRegistry;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MignonetteTrunkPlacer extends TrunkPlacer {
    public static final Codec<MignonetteTrunkPlacer> CODEC = RecordCodecBuilder.create(objectInstance ->
            trunkPlacerParts(objectInstance).apply(objectInstance, MignonetteTrunkPlacer::new));

    public MignonetteTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerRegistry.MIGNONETTE_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        int initialHeight_ = baseHeight;
        int height_ = baseHeight + random.nextIntBetweenInclusive(heightRandA, heightRandB);

        list.add(new FoliagePlacer.FoliageAttachment(pos.above(height_), 1, false));

        for (int i = 0; i < height_; i++) {
            placeLog(level, blockSetter, random, pos.above(i), config);
        }

        for (int i = 0; i < initialHeight_; i++) {
            if (i > 2 && i % 2 == 1) {
                if (random.nextFloat() > 0.25) {
                    int offsetA = random.nextBoolean() ? 1 : -1;
                    int offsetB = random.nextBoolean() ? 1 : -1;
                    int j = random.nextInt(2);
                    for (int k = 0; k <= j; k++) {
                        int offsetAddA = offsetA < 0 ? k * -1 : k;
                        int offsetAddB = offsetB < 0 ? k * -1 : k;
                        blockSetter.accept(pos.above(i).offset(offsetA + offsetAddA, k, offsetB + offsetAddB),
                                (BlockState) Function.identity().apply(config.trunkProvider
                                        .getState(random, pos.above(i).offset(offsetA + offsetAddA, k, offsetB + offsetAddB))
                                        .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)));
                        if (k == j) {
                            list.add(new FoliagePlacer.FoliageAttachment(pos.above(i).offset(offsetA + offsetAddA, k, offsetB + offsetAddB), 0, false));
                        }
                    }
                }
            }
        }

        return list;
    }
}
