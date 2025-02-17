package net.qtpi.jeepersbeepers;

import net.fabricmc.api.ModInitializer;

import net.qtpi.jeepersbeepers.registry.EffectRegistry;
import net.qtpi.jeepersbeepers.registry.TabRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeepersBeepers implements ModInitializer {
	public static final String MOD_ID = "jeepersbeepers";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		TabRegistry.registerCreativeModeTabs();
		ItemRegistry.registerModItems();
		EffectRegistry.registerModEffects();
	}
}