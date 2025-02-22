package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;

public class EntityRegistry {
    public static final EntityType<BeeperEntity> BEEPER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(JeepersBeepers.MOD_ID, "beeper"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, BeeperEntity::new)
                    .dimensions(EntityDimensions.fixed(0.7f, 0.7f)).build());

    public static void registerModEntities() {
        JeepersBeepers.LOGGER.info("Registering Mod Entities for " + JeepersBeepers.MOD_ID);
    }
}
