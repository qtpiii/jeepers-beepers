package net.qtpi.jeepersbeepers.world.tree.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.qtpi.jeepersbeepers.block.BeeperHiveBlock;
import net.qtpi.jeepersbeepers.registry.BlockEntityRegistry;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.EntityRegistry;
import net.qtpi.jeepersbeepers.registry.WorldGenRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BeeperNestDecorator extends TreeDecorator {
    public static final Codec<BeeperNestDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
            .fieldOf("probability")
            .<BeeperNestDecorator>xmap(BeeperNestDecorator::new, beeperNestDecorator -> beeperNestDecorator.probability).codec();
    private static final Direction WORLDGEN_FACING = Direction.SOUTH;
    private static final Direction[] SPAWN_DIRECTIONS = (Direction[])Direction.Plane.HORIZONTAL
            .stream()
            .filter(direction -> direction != WORLDGEN_FACING.getOpposite())
            .toArray(Direction[]::new);
    private final float probability;

    public BeeperNestDecorator(float probability) {
        this.probability = probability;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return WorldGenRegistry.BEEPER_NEST_DECORATOR;
    }

    @Override
    public void place(Context context) {
        RandomSource randomSource = context.random();
        if (!(randomSource.nextFloat() >= this.probability)) {
            List<BlockPos> list = context.leaves();
            List<BlockPos> list2 = context.logs();
            int i = !list.isEmpty()
                    ? Math.max(list.get(0).getY() - 1, (list2.get(0)).getY() + 1)
                    : Math.min((list2.get(0)).getY() + 1 + randomSource.nextInt(3), (list2.get(list2.size() - 1)).getY());
            List<BlockPos> list3 = list2.stream()
                    .filter(blockPos -> blockPos.getY() == i)
                    .flatMap(blockPos -> Stream.of(SPAWN_DIRECTIONS).map(blockPos::relative))
                    .collect(Collectors.toList());
            if (!list3.isEmpty()) {
                Collections.shuffle(list3);
                Optional<BlockPos> optional = list3.stream().filter(blockPos -> context.isAir(blockPos) && context.isAir(blockPos.relative(WORLDGEN_FACING))).findFirst();
                if (!optional.isEmpty()) {
                    context.setBlock(optional.get(), BlockRegistry.BEEPER_NEST.defaultBlockState().setValue(BeeperHiveBlock.FACING, WORLDGEN_FACING));
                    context.level().getBlockEntity(optional.get(), BlockEntityRegistry.BEEPER_HIVE_BLOCK_ENTITY).ifPresent(beeperHiveBlockEntity -> {
                        int ix = 2 + randomSource.nextInt(2);

                        for (int j = 0; j < ix; j++) {
                            CompoundTag compoundTag = new CompoundTag();
                            compoundTag.putString("id", "jeepersbeepers:beeper");
                            beeperHiveBlockEntity.storeBeeper(compoundTag, randomSource.nextInt(599), false, false);
                        }
                    });
                }
            }
        }
    }
}
