package net.qtpi.jeepersbeepers.registry;

import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.mixin.FoliagePlacerTypeInvoker;
import net.qtpi.jeepersbeepers.world.tree.custom.MignonetteFoliagePlacer;

public class FoliagePlacerRegistry {
    public static final FoliagePlacerType<?> MIGNONETTE_FOLIAGE_PLACER = FoliagePlacerTypeInvoker.callRegister("mignonette_foliage_placer", MignonetteFoliagePlacer.CODEC);

    public static void register() {
        JeepersBeepers.LOGGER.info("Registering Foliage Placers for " + JeepersBeepers.MOD_ID);
    }
}
