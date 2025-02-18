package net.qtpi.jeepersbeepers.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BeeperNectarLayer extends GeoRenderLayer<BeeperEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper_nectar.png");
    public BeeperNectarLayer(GeoRenderer<BeeperEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, BeeperEntity entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (entity.hasNectar()) {
            RenderType renderLayer = RenderType.entityCutoutNoCull(TEXTURE);
            getRenderer().reRender(getDefaultBakedModel(entity), poseStack, bufferSource, entity, renderLayer,
                    bufferSource.getBuffer(renderLayer), partialTick, packedLight, packedOverlay, 1, 1, 1, 1);
        }
    }
}
