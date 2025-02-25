package net.qtpi.jeepersbeepers.registry;


import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.block.*;
import net.qtpi.jeepersbeepers.world.tree.FloweringMignonetteTreeGrower;
import net.qtpi.jeepersbeepers.world.tree.MignonetteTreeGrower;

import java.util.function.Supplier;

public class BlockRegistry {

    public static final Block BEEPER_HIVE = registerBlock("beeper_hive", new BeeperHiveBlock(FabricBlockSettings.copyOf(Blocks.BEEHIVE).mapColor(MapColor.GLOW_LICHEN)), true);
    public static final Block BEEPER_NEST = registerBlock("beeper_nest", new BeeperHiveBlock(FabricBlockSettings.copyOf(Blocks.BEE_NEST).mapColor(MapColor.STONE)), true);

    public static final Block LOAM = registerBlock("loam", new Block(FabricBlockSettings.copyOf(Blocks.DIRT)), true);
    public static final Block LOAM_FARMLAND = registerBlock("loam_farmland", new LoamFarmlandBlock(FabricBlockSettings.copyOf(Blocks.FARMLAND)), true);

    public static final Block BEEPER_FLUFF_BLOCK = registerBlock("beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.GLOW_LICHEN).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block WHITE_BEEPER_FLUFF_BLOCK = registerBlock("white_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.SNOW).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block LIGHT_GRAY_BEEPER_FLUFF_BLOCK = registerBlock("light_gray_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_LIGHT_GRAY).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block GRAY_BEEPER_FLUFF_BLOCK = registerBlock("gray_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_GRAY).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block BLACK_BEEPER_FLUFF_BLOCK = registerBlock("black_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_BLACK).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block BROWN_BEEPER_FLUFF_BLOCK = registerBlock("brown_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_BROWN).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block RED_BEEPER_FLUFF_BLOCK = registerBlock("red_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_RED).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block ORANGE_BEEPER_FLUFF_BLOCK = registerBlock("orange_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_ORANGE).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block YELLOW_BEEPER_FLUFF_BLOCK = registerBlock("yellow_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_YELLOW).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block LIME_BEEPER_FLUFF_BLOCK = registerBlock("lime_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_LIGHT_GREEN).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block GREEN_BEEPER_FLUFF_BLOCK = registerBlock("green_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_GREEN).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block CYAN_BEEPER_FLUFF_BLOCK = registerBlock("cyan_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_CYAN).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block LIGHT_BLUE_BEEPER_FLUFF_BLOCK = registerBlock("light_blue_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_LIGHT_BLUE).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block BLUE_BEEPER_FLUFF_BLOCK = registerBlock("blue_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_BLUE).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block PURPLE_BEEPER_FLUFF_BLOCK = registerBlock("purple_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_PURPLE).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block MAGENTA_BEEPER_FLUFF_BLOCK = registerBlock("magenta_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_MAGENTA).strength(0.6f).sound(SoundType.WOOL)), true);
    public static final Block PINK_BEEPER_FLUFF_BLOCK = registerBlock("pink_beeper_fluff_block", new BeeperFluffBlock(FabricBlockSettings.create()
            .instrument(NoteBlockInstrument.GUITAR).mapColor(MapColor.COLOR_PINK).strength(0.6f).sound(SoundType.WOOL)), true);

    public static final Block MIGNONETTE_LOG = registerBlock("mignonette_log",
            new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_LOG)
            .mapColor(MapColor.COLOR_GREEN)), true);
    public static final Block MIGNONETTE_WOOD = registerBlock("mignonette_wood",
            new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.OAK_WOOD)
            .mapColor(MapColor.COLOR_GREEN)), true);
    public static final Block STRIPPED_MIGNONETTE_LOG = registerBlock("stripped_mignonette_log",
            new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_LOG)
            .mapColor(MapColor.GLOW_LICHEN)), true);
    public static final Block STRIPPED_MIGNONETTE_WOOD = registerBlock("stripped_mignonette_wood",
            new RotatedPillarBlock(FabricBlockSettings.copyOf(Blocks.STRIPPED_OAK_WOOD)
            .mapColor(MapColor.GLOW_LICHEN)), true);

    public static final Block MIGNONETTE_LEAVES = registerBlock("mignonette_leaves",
            new LeavesBlock(FabricBlockSettings.copyOf(Blocks.OAK_LEAVES)
                    .mapColor(MapColor.COLOR_LIGHT_GREEN).nonOpaque()), true);

    public static final Block MIGNONETTE_PLANKS = registerBlock("mignonette_planks",
            new Block(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
            .mapColor(MapColor.GLOW_LICHEN)), true);
    public static final Block MIGNONETTE_STAIRS = registerBlock("mignonette_stairs",
            new StairBlock(BlockRegistry.MIGNONETTE_PLANKS.defaultBlockState(),
                    FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
                    .mapColor(MapColor.GLOW_LICHEN)), true);
    public static final Block MIGNONETTE_SLAB = registerBlock("mignonette_slab",
            new SlabBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
            .mapColor(MapColor.GLOW_LICHEN)), true);

    public static final Block MIGNONETTE_BUTTON = registerBlock("mignonette_button",
            new ButtonBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
            .mapColor(MapColor.GLOW_LICHEN), BlockSetType.OAK, 10, true), true);
    public static final Block MIGNONETTE_PRESSURE_PLATE = registerBlock("mignonette_pressure_plate",
            new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
            .mapColor(MapColor.GLOW_LICHEN), BlockSetType.OAK), true);

    public static final Block MIGNONETTE_FENCE = registerBlock("mignonette_fence",
            new FenceBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
            .mapColor(MapColor.GLOW_LICHEN)), true);
    public static final Block MIGNONETTE_FENCE_GATE = registerBlock("mignonette_fence_gate",
            new FenceGateBlock(FabricBlockSettings.copyOf(Blocks.OAK_PLANKS)
            .mapColor(MapColor.GLOW_LICHEN), WoodType.OAK), true);

    public static final Block MIGNONETTE_DOOR = registerBlock("mignonette_door",
            new DoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_DOOR)
            .mapColor(MapColor.GLOW_LICHEN), BlockSetType.OAK), true);
    public static final Block MIGNONETTE_TRAPDOOR = registerBlock("mignonette_trapdoor",
            new TrapDoorBlock(FabricBlockSettings.copyOf(Blocks.OAK_TRAPDOOR)
            .mapColor(MapColor.GLOW_LICHEN), BlockSetType.OAK), true);

    public static final Block MIGNONETTE_SAPLING = registerBlock("mignonette_sapling",
            new SaplingBlock(new MignonetteTreeGrower(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)), true);
    public static final Block FLOWERING_MIGNONETTE_SAPLING = registerBlock("flowering_mignonette_sapling",
            new SaplingBlock(new FloweringMignonetteTreeGrower(), FabricBlockSettings.copyOf(Blocks.OAK_SAPLING)), true);

    public static final Block MIGNONETTE_FLOWER = registerBlock("mignonette_flower",
            new WallFlowerBlock(MobEffects.REGENERATION, 30,
                    FabricBlockSettings.copyOf(Blocks.LILY_OF_THE_VALLEY).offsetType(BlockBehaviour.OffsetType.NONE)), true);

    public static final Block BUTTERDEW_SQUASH = registerBlock("butterdew_squash",
            new ButterdewSquashBlock(FabricBlockSettings.copyOf(Blocks.PUMPKIN)), true);
    public static final Block BUTTERDEW_SQUASH_STEM = registerBlock("butterdew_squash_stem",
            new StemBlock((StemGrownBlock) BUTTERDEW_SQUASH, () -> ItemRegistry.BUTTERDEW_SQUASH_SEEDS,
                    FabricBlockSettings.copyOf(Blocks.PUMPKIN_STEM)), false);
    public static final Block ATTACHED_BUTTERDEW_SQUASH_STEM = registerBlock("attached_butterdew_squash_stem",
            new AttachedStemBlock((StemGrownBlock) BUTTERDEW_SQUASH, () -> ItemRegistry.BUTTERDEW_SQUASH_SEEDS,
                    FabricBlockSettings.copyOf(Blocks.ATTACHED_PUMPKIN_STEM)), false);

    public static final Block CARVED_BUTTERDEW_SQUASH = registerBlock("carved_butterdew_squash",
            new EquipableCarvedButterdewSquashBlock(FabricBlockSettings.copyOf(Blocks.CARVED_PUMPKIN)), true);
    public static final Block BUTTERDEW_LANTERN = registerBlock("butterdew_lantern",
            new CarvedButterdewSquashBlock(FabricBlockSettings.copyOf(Blocks.JACK_O_LANTERN)), true);
    public static final Block CAWVED_BUTTEWDEW_SQUASH = registerBlock("cawved_buttewdew_squash",
            new EquipableCarvedButterdewSquashBlock(FabricBlockSettings.copyOf(Blocks.CARVED_PUMPKIN)), true);
    public static final Block BUTTEWDEW_LANTEWN = registerBlock("buttewdew_lantewn",
            new CarvedButterdewSquashBlock(FabricBlockSettings.copyOf(Blocks.JACK_O_LANTERN)), true);

    public static final Block DRAGONFRUIT_TREE = registerBlock("dragonfruit_tree",
            new DragonFruitTreeBlock(FabricBlockSettings.create().mapColor(MapColor.PLANT).sound(SoundType.AZALEA)
                            .requiresCorrectToolForDrops().strength(0.2F).pushReaction(PushReaction.DESTROY).forceSolidOff()), true);

    public static final BlockFamily MIGNONETTE_FAMILY = new BlockFamily.Builder(MIGNONETTE_PLANKS)
            .stairs(MIGNONETTE_STAIRS)
            .slab(MIGNONETTE_SLAB)
            .button(MIGNONETTE_BUTTON)
            .pressurePlate(MIGNONETTE_PRESSURE_PLATE)
            .fence(MIGNONETTE_FENCE)
            .fenceGate(MIGNONETTE_FENCE_GATE)
            .door(MIGNONETTE_DOOR)
            .trapdoor(MIGNONETTE_TRAPDOOR)
            .getFamily();

    private static Block registerBlock(String name, Block block, Boolean registerItem) {
        if (registerItem) {
            registerBlockItem(name, block);
        }
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
