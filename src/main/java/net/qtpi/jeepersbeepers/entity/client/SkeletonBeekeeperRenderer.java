package net.qtpi.jeepersbeepers.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.qtpi.jeepersbeepers.JeepersBeepers;

public class SkeletonBeekeeperRenderer extends SkeletonRenderer {
    public SkeletonBeekeeperRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
        return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/entity/skeleton_beekeeper.png");
    }
}
