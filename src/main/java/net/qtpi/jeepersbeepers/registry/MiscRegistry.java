package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.HoeItem;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import net.qtpi.jeepersbeepers.entity.SkeletonBeekeeperEntity;

public class MiscRegistry {

    public static final ResourceKey<PoiType> BEEPER_HIVE_POI_KEY = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, ResourceLocation.tryBuild(JeepersBeepers.MOD_ID, "beeper_hive"));
    public static final PoiType BEEPER_HIVE_POI = PointOfInterestHelper.register(ResourceLocation.tryBuild(JeepersBeepers.MOD_ID, "beeper_hive"), 0, 1, BlockRegistry.BEEPER_HIVE, BlockRegistry.BEEPER_NEST);

    public static void registerMisc() {
        JeepersBeepers.LOGGER.info("Registering Misc for " + JeepersBeepers.MOD_ID);

        FabricDefaultAttributeRegistry.register(EntityRegistry.BEEPER, BeeperEntity.setAttributes());
        FabricDefaultAttributeRegistry.register(EntityRegistry.SKELETON_BEEKEEPER, SkeletonBeekeeperEntity.setAttributes());

        StrippableBlockRegistry.register(BlockRegistry.MIGNONETTE_LOG, BlockRegistry.STRIPPED_MIGNONETTE_LOG);
        StrippableBlockRegistry.register(BlockRegistry.MIGNONETTE_WOOD, BlockRegistry.STRIPPED_MIGNONETTE_WOOD);

        FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.STRIPPED_MIGNONETTE_LOG, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD, 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_PLANKS, 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_LEAVES, 30, 60);

        TillableBlockRegistry.register(BlockRegistry.LOAM, HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(BlockRegistry.LOAM_FARMLAND.defaultBlockState()));
    }
}
