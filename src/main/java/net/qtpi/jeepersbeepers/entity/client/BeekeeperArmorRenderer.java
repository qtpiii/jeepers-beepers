package net.qtpi.jeepersbeepers.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.item.BeekeeperArmorItem;
import net.qtpi.jeepersbeepers.registry.EntityRegistry;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BeekeeperArmorRenderer extends GeoArmorRenderer<BeekeeperArmorItem> {
    public BeekeeperArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(JeepersBeepers.MOD_ID, "armor/beekeeper_armor")));
    }

    @Override
    public ResourceLocation getTextureLocation(BeekeeperArmorItem animatable) {
        if (this.currentEntity.getType() == EntityRegistry.SKELETON_BEEKEEPER) {
            return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/item/armor/beekeeper_armor_tattered.png");
        }
        return new ResourceLocation(JeepersBeepers.MOD_ID, "textures/item/armor/beekeeper_armor.png");
    }
}
