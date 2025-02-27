package net.qtpi.jeepersbeepers.registry;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.phys.Vec2;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;
import net.qtpi.jeepersbeepers.entity.PollenCloud;
import net.qtpi.jeepersbeepers.entity.ThrownPollenPuff;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityRegistry {
    public static final EntityType<BeeperEntity> BEEPER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(JeepersBeepers.MOD_ID, "beeper"),
            FabricEntityTypeBuilder.create(MobCategory.CREATURE, BeeperEntity::new)
                    .dimensions(EntityDimensions.fixed(0.7f, 0.7f)).build());

    public static final EntityType<ThrownPollenPuff> THROWN_POLLEN_PUFF = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(JeepersBeepers.MOD_ID, "pollen_puff"),
            FabricEntityTypeBuilder.<ThrownPollenPuff>create(MobCategory.MISC, ThrownPollenPuff::new).build()
    );

    public static final EntityType<PollenCloud> POLLEN_CLOUD = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            new ResourceLocation(JeepersBeepers.MOD_ID, "pollen_cloud"),
            FabricEntityTypeBuilder.create(MobCategory.MISC, PollenCloud::new).build()
    );

    public static void registerModEntities() {
        JeepersBeepers.LOGGER.info("Registering Mod Entities for " + JeepersBeepers.MOD_ID);
    }
}
