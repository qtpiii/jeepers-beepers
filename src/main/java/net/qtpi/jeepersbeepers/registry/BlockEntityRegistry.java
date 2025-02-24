package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.block.entity.BeeperHiveBlockEntity;

public class BlockEntityRegistry {
    public static final BlockEntityType<BeeperHiveBlockEntity> BEEPER_HIVE_BLOCK_ENTITY =
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(JeepersBeepers.MOD_ID, "beeper_hive_be"),
                    FabricBlockEntityTypeBuilder.create(BeeperHiveBlockEntity::new,
                            BlockRegistry.BEEPER_HIVE, BlockRegistry.BEEPER_NEST).build());

    public static void registerModBlockEntities() {
        JeepersBeepers.LOGGER.info("Registering Mod Block Entities for " + JeepersBeepers.MOD_ID);
    }
}
