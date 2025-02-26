package net.qtpi.jeepersbeepers;

import net.fabricmc.api.ModInitializer;

import net.qtpi.jeepersbeepers.registry.*;
import net.qtpi.jeepersbeepers.world.biome.ModBiolithGeneration;
import net.qtpi.jeepersbeepers.world.gen.ModWorldGeneration;
import net.qtpi.jeepersbeepers.registry.WorldGenRegistry;
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
		WorldGenRegistry.register();

		ModWorldGeneration.generateModWorldGen();

		ModBiolithGeneration.init();
	}
}