package net.qtpi.jeepersbeepers;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import net.qtpi.jeepersbeepers.registry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeepersBeepers implements ModInitializer {
	public static final String MOD_ID = "jeepersbeepers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ItemRegistry.registerModItems();
		BlockRegistry.registerModBlocks();
		BlockEntityRegistry.registerModBlockEntities();
		TabRegistry.registerCreativeModeTabs();
		EntityRegistry.registerModEntities();
		EffectRegistry.registerModEffects();
		TagRegistry.registerModTags();
		CompatibilityTagRegistry.registerCompatibilityTags();
		SoundRegistry.registerModSounds();
		MiscRegistry.registerMisc();

		FabricDefaultAttributeRegistry.register(EntityRegistry.BEEPER, BeeperEntity.setAttributes());

		StrippableBlockRegistry.register(BlockRegistry.MIGNONETTE_LOG, BlockRegistry.STRIPPED_MIGNONETTE_LOG);
		StrippableBlockRegistry.register(BlockRegistry.MIGNONETTE_WOOD, BlockRegistry.STRIPPED_MIGNONETTE_WOOD);

		FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.STRIPPED_MIGNONETTE_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_PLANKS, 5, 20);
		FlammableBlockRegistry.getDefaultInstance().add(BlockRegistry.MIGNONETTE_LEAVES, 30, 60);
	}
}