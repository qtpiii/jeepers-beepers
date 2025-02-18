package net.qtpi.jeepersbeepers;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import net.qtpi.jeepersbeepers.registry.*;
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
		BlockRegistry.registerModBlocks();
		BlockEntityRegistry.registerModBlockEntities();
		EntityRegistry.registerModEntities();

		FabricDefaultAttributeRegistry.register(EntityRegistry.BEEPER, BeeperEntity.setAttributes());
	}
}