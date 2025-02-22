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

import java.awt.*;

public class BeeperFluffLayer extends GeoRenderLayer<BeeperEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper_fluff.png");
    private static final ResourceLocation COLORABLE_TEXTURE = new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper_fluff_white.png");
    public BeeperFluffLayer(GeoRenderer<BeeperEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, BeeperEntity entity, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!entity.isNaked() && !entity.hasNectar()) {
            if (!entity.hasColor()) {
                RenderType fluffLayer = RenderType.entityCutoutNoCull(TEXTURE);
                getRenderer().reRender(getDefaultBakedModel(entity), poseStack, bufferSource, entity, fluffLayer,
                        bufferSource.getBuffer(fluffLayer), partialTick, packedLight, packedOverlay, 1, 1, 1, 1);
            } else {
                RenderType fluffLayer = RenderType.entityCutoutNoCull(COLORABLE_TEXTURE);
                int color = BeeperEntity.getColorArray(entity.getColor());
                float red = new Color(color).getRed() / 255f;
                float green = new Color(color).getGreen() / 255f;
                float blue = new Color(color).getBlue() / 255f;
                getRenderer().reRender(getDefaultBakedModel(entity), poseStack, bufferSource, entity, fluffLayer,
                        bufferSource.getBuffer(fluffLayer), partialTick, packedLight, packedOverlay, red, green, blue, 1);
            }
        }
    }
}
