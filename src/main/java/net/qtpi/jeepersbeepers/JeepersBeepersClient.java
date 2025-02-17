package net.qtpi.jeepersbeepers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.qtpi.jeepersbeepers.entity.client.BeeperRenderer;
import net.qtpi.jeepersbeepers.registry.EntityRegistry;

public class JeepersBeepersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.BEEPER, BeeperRenderer::new);
    }
}
