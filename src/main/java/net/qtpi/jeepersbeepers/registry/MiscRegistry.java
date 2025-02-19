package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.qtpi.jeepersbeepers.JeepersBeepers;

public class MiscRegistry {

    public static final ResourceKey<PoiType> BEEPER_HIVE_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.tryBuild(JeepersBeepers.MOD_ID, "beeper_hive"));
    public static final PoiType BEEPER_HIVE_POI = PointOfInterestHelper.register(ResourceLocation.tryBuild(JeepersBeepers.MOD_ID, "beeper_hive"), 0, 1, BlockRegistry.BEEPER_HIVE);

    public static void registerMisc() {
        JeepersBeepers.LOGGER.info("Registering Misc for " + JeepersBeepers.MOD_ID);
    }
}
