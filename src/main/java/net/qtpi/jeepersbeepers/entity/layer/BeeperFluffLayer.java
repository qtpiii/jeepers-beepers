package net.qtpi.jeepersbeepers.entity.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class BeeperFluffLayer extends GeoRenderLayer<BeeperEntity> {
    private static final ResourceLocation TEXTURE1 = new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper_fluff.png");
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper_fluff_nectar.png");
    public BeeperFluffLayer(GeoRenderer<BeeperEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, BeeperEntity entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (entity.hasFluff()) {
            RenderType renderLayer;
            if (entity.hasNectar()) {
                renderLayer = RenderType.entityCutoutNoCull(TEXTURE2);
            } else {
                renderLayer = RenderType.entityCutoutNoCull(TEXTURE1);
            }
            getRenderer().reRender(getDefaultBakedModel(entity), poseStack, bufferSource, entity, renderLayer,
                    bufferSource.getBuffer(renderLayer), partialTick, packedLight, packedOverlay, 1, 1, 1, 1);
        }
    }
}
