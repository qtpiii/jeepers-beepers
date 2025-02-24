package net.qtpi.jeepersbeepers.registry;

import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.mixin.TrunkPlacerTypeInvoker;
import net.qtpi.jeepersbeepers.world.tree.custom.MignonetteTrunkPlacer;

public class TrunkPlacerRegistry {
    public static final TrunkPlacerType<?> MIGNONETTE_TRUNK_PLACER = TrunkPlacerTypeInvoker.callRegister("mignonette_trunk_placer", MignonetteTrunkPlacer.CODEC);

    public static void register() {
        JeepersBeepers.LOGGER.info("Registering Trunk Placers for " + JeepersBeepers.MOD_ID);
    }
}
