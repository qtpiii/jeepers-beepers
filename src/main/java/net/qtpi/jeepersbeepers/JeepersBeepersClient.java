package net.qtpi.jeepersbeepers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.qtpi.jeepersbeepers.block.client.MignonetteRootBlockEntityRenderer;
import net.qtpi.jeepersbeepers.block.client.MignonetteRootBlockModel;
import net.qtpi.jeepersbeepers.entity.client.BeeperRenderer;
import net.qtpi.jeepersbeepers.entity.client.PollenCloudRenderer;
import net.qtpi.jeepersbeepers.entity.client.SkeletonBeekeeperRenderer;
import net.qtpi.jeepersbeepers.registry.*;

public class JeepersBeepersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityRegistry.BEEPER, BeeperRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.SKELETON_BEEKEEPER, SkeletonBeekeeperRenderer::new);
        EntityRendererRegistry.register(EntityRegistry.THROWN_POLLEN_PUFF, (context) -> new ThrownItemRenderer<>(context, 1, true));
        EntityRendererRegistry.register(EntityRegistry.POLLEN_CLOUD, PollenCloudRenderer::new);

        BlockEntityRenderers.register(BlockEntityRegistry.MIGNONETTE_ROOT_BLOCK_ENTITY, MignonetteRootBlockEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MignonetteRootBlockModel.LAYER_LOCATION, MignonetteRootBlockModel::create);

        ParticleFactoryRegistry.getInstance().register(ParticleRegistry.BEEPER_SNEEZE_POOF, ExplodeParticle.Provider::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_LEAVES, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_ROOT, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_FLOWER, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_TRAPDOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.PETRIFIED_MIGNONETTE_DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.PETRIFIED_MIGNONETTE_TRAPDOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.MIGNONETTE_SAPLING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.FLOWERING_MIGNONETTE_SAPLING, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.BUTTERDEW_SQUASH_STEM, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.ATTACHED_BUTTERDEW_SQUASH_STEM, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.DRAGONFRUIT_TREE, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.AMARANTH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.WILD_AMARANTH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.BRADDISH, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.GINGER, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.HONEY_LAMP, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.SPICY_HONEY_LAMP, RenderType.cutout());

        BlockRenderLayerMap.INSTANCE.putBlock(BlockRegistry.SPICY_HONEY_BLOCK, RenderType.translucent());
    }
}
