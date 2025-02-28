package net.qtpi.jeepersbeepers.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import net.qtpi.jeepersbeepers.entity.layer.BeeperFluffLayer;
import net.qtpi.jeepersbeepers.entity.layer.BeeperNectarLayer;
import net.qtpi.jeepersbeepers.entity.layer.BeeperStingerLayer;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BeeperRenderer extends GeoEntityRenderer<BeeperEntity> {
    public BeeperRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new ResourceLocation(JeepersBeepers.MOD_ID, "beeper")));

        this.addRenderLayer(new BeeperFluffLayer(this));
        this.addRenderLayer(new BeeperStingerLayer(this));
        this.addRenderLayer(new BeeperNectarLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BeeperEntity entity) {
        String string = ChatFormatting.stripFormatting(entity.getName().getString());
        if ("Glommer".equals(string) && entity.hasColor() && entity.getColor() == DyeColor.PURPLE) {
            return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper_glommer.png");
        } else if (entity.isAngry()) {
            if (entity.isWild()) { return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/wild_beeper_angry.png"); }
            else { return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper_angry.png"); }
        } else {
            if (entity.isWild()) { return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/wild_beeper.png"); }
            else { return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/beeper/beeper.png"); }
        }
    }

    @Override
    public void preRender(PoseStack poseStack, BeeperEntity entity, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!isReRender) {
            if (entity.isBaby()) {
                poseStack.scale(0.5f, 0.5f, 0.5f);
            } else {
                poseStack.scale(1.0f, 1.0f, 1.0f);
            }
        }
        super.preRender(poseStack, entity, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
