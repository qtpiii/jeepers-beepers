package net.qtpi.jeepersbeepers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.qtpi.jeepersbeepers.entity.client.BeeperRenderer;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.EntityRegistry;
import net.qtpi.jeepersbeepers.registry.ParticleRegistry;

public class JeepersBeepersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.BEEPER, BeeperRenderer::new);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BEEPER_SNEEZE_POOF, ExplodeParticle.Provider::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_LEAVES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_FLOWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_TRAPDOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_SAPLING, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.BUTTERDEW_SQUASH_STEM, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ATTACHED_BUTTERDEW_SQUASH_STEM, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.DRAGONFRUIT_TREE, RenderType.cutout());
    }
}
