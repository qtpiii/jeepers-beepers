package net.qtpi.jeepersbeepers.registry;


import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.block.BeeperFluffBlock;
import net.qtpi.jeepersbeepers.block.BeeperHiveBlock;

public class BlockRegistry {

    public static final Block BEEPER_HIVE = registerBlock("beeper_hive", new BeeperHiveBlock(FabricBlockSettings.copyOf(Blocks.BEEHIVE)));

    public static final Block BEEPER_FLUFF_BLOCK = registerBlock("beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.GLOW_LICHEN).strength(0.6f).sound(SoundType.WOOL)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(JeepersBeepers.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(JeepersBeepers.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks() {
        JeepersBeepers.LOGGER.info("Registering Mod Blocks for " + JeepersBeepers.MOD_ID);
    }
}
