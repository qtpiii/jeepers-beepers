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
import net.minecraft.world.phys.Vec2;
import net.qtpi.jeepersbeepers.registry.WorldGenRegistry;

import java.util.*;
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
        return WorldGenRegistry.MIGNONETTE_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random, int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        setDirtAt(level, blockSetter, random, pos.below(), config);
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        int initialHeight_ = baseHeight;
        int height_ = baseHeight + random.nextIntBetweenInclusive(heightRandA, heightRandB);

        list.add(new FoliagePlacer.FoliageAttachment(pos.above(height_ - 1), 1, false));

        for (int i = 0; i < height_; i++) {
            placeLog(level, blockSetter, random, pos.above(i), config);
        }
        HashMap<Vec2, Boolean> directionsChosen = new HashMap<>();
        List<Vec2> directions = new ArrayList<>();
        directions.add(new Vec2(1, 1));
        directions.add(new Vec2(-1, 1));
        directions.add(new Vec2(1, -1));
        directions.add(new Vec2(-1, -1));
        for (Vec2 direction : directions) {
            directionsChosen.put(direction, false);
        }
        Collections.shuffle(directions);
        for (int i = 0; i < initialHeight_; i++) {
            if (i < 2) continue;
            if (random.nextFloat() > 0.25) {
                Vec2 offset = Vec2.ZERO;
                for (Vec2 direction : directions) {
                    if (!directionsChosen.get(direction)) {
                        offset = direction;
                        directionsChosen.put(direction, true);
                        break;
                    }
                }
                for (int k = 0; k <= 1; k++) {
                    int offsetAddX = offset.x < 0 ? k * -1 : k;
                    int offsetAddY = offset.y < 0 ? k * -1 : k;
                    blockSetter.accept(pos.above(i).offset((int) (offset.x + offsetAddX), k, (int) (offset.y + offsetAddY)),
                            (BlockState) Function.identity().apply(config.trunkProvider
                                    .getState(random, pos.above(i).offset((int) (offset.x + offsetAddX), k, (int) (offset.y + offsetAddY)))
                                    .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)));
                    if (k == 1) {
                        list.add(new FoliagePlacer.FoliageAttachment(pos.above(i).offset((int) (offset.x + offsetAddX), k, (int) (offset.y + offsetAddY)), 0, false));
                    }
                }
            }
        }
        return list;
    }
}
