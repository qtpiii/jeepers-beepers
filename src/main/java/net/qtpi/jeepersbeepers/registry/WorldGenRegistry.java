package net.qtpi.jeepersbeepers.registry;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.mixin.FoliagePlacerTypeInvoker;
import net.qtpi.jeepersbeepers.mixin.TreeDecoratorTypeInvoker;
import net.qtpi.jeepersbeepers.mixin.TrunkPlacerTypeInvoker;
import net.qtpi.jeepersbeepers.world.tree.custom.BeeperNestDecorator;
import net.qtpi.jeepersbeepers.world.tree.custom.MignonetteFoliagePlacer;
import net.qtpi.jeepersbeepers.world.tree.custom.MignonetteTrunkPlacer;

public class WorldGenRegistry {
    public static final TrunkPlacerType<?> MIGNONETTE_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("mignonette_trunk_placer", MignonetteTrunkPlacer.CODEC);
    public static final FoliagePlacerType<?> MIGNONETTE_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("mignonette_foliage_placer", MignonetteFoliagePlacer.CODEC);
    public static final TreeDecoratorType<?> BEEPER_NEST_DECORATOR = TreeDecoratorTypeInvoker.callRegister("beeper_nest_decorator", BeeperNestDecorator.CODEC);

    public static void register() {
        JeepersBeepers.LOGGER.info("Registering World Gen Items for " + JeepersBeepers.MOD_ID);
    }
}
