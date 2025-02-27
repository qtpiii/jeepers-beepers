package net.qtpi.jeepersbeepers.entity.client;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.qtpi.jeepersbeepers.entity.PollenCloud;

public class PollenCloudRenderer extends EntityRenderer<PollenCloud> {
    public PollenCloudRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(PollenCloud entity) {
        return null;
    }

    @Override
    public boolean shouldRender(PollenCloud livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return false;
    }
}
