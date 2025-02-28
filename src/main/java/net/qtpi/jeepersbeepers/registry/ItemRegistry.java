package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.item.BeekeeperArmorItem;
import net.qtpi.jeepersbeepers.item.PollenPuff;
import net.qtpi.jeepersbeepers.item.SpicyHoneyBottleItem;

public class ItemRegistry {

    public static final Item SPICY_HONEY_BOTTLE = registerItem("spicy_honey_bottle", new SpicyHoneyBottleItem(
            new FabricItemSettings().maxCount(16).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.1f).build())));
    public static final Item SPICY_HONEYCOMB = registerItem("spicy_honeycomb", new Item(new FabricItemSettings()));
    public static final Item BEEPER_SPAWN_EGG = registerItem("beeper_spawn_egg", new SpawnEggItem(EntityRegistry.BEEPER,
            6651479, 12567454, new FabricItemSettings()));
    public static final Item SKELETON_BEEKEEPER_SPAWN_EGG = registerItem("skeleton_beekeeper_spawn_egg", new SpawnEggItem(EntityRegistry.SKELETON_BEEKEEPER,
            0x1f201c, 0x835b41, new FabricItemSettings()));
    public static final Item BEEPER_FLUFF = registerItem("beeper_fluff", new Item(new FabricItemSettings()));
    public static final Item POLLEN_PUFF = registerItem("pollen_puff", new PollenPuff(new FabricItemSettings()));

    public static final Item BEEKEEPER_HAT = registerItem("beekeeper_hat", new BeekeeperArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item BEEKEEPER_TUNIC = registerItem("beekeeper_tunic", new BeekeeperArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item BEEKEEPER_PANTS = registerItem("beekeeper_pants", new BeekeeperArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item BEEKEEPER_BOOTS = registerItem("beekeeper_boots", new BeekeeperArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.BOOTS, new FabricItemSettings()));

    public static final Item BUTTERDEW_SQUASH_SEEDS = registerItem("butterdew_squash_seeds", new ItemNameBlockItem(BlockRegistry.BUTTERDEW_SQUASH_STEM, new FabricItemSettings()));

    public static final Item DRAGONFRUIT_SEEDS = registerItem("dragonfruit_seeds", new ItemNameBlockItem(BlockRegistry.DRAGONFRUIT_TREE, new FabricItemSettings()));
    public static final Item DRAGONFRUIT = registerItem("dragonfruit", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f).build())));
    public static final Item AMARANTH_SEEDS = registerItem("amaranth_seeds", new ItemNameBlockItem(BlockRegistry.AMARANTH, new FabricItemSettings()));
    public static final Item AMARANTH = registerItem("amaranth", new Item(new FabricItemSettings().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.2f).build())));

    private static void addItemsToColoredBlocksTab(FabricItemGroupEntries entries) {
        entries.accept(BlockRegistry.WHITE_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.LIGHT_GRAY_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.GRAY_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.BLACK_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.BROWN_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.RED_BEEPER_FLUFF_BLOCK );
        entries.accept(BlockRegistry.ORANGE_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.YELLOW_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.LIME_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.GREEN_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.CYAN_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.LIGHT_BLUE_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.BLUE_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.PURPLE_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.MAGENTA_BEEPER_FLUFF_BLOCK);
        entries.accept(BlockRegistry.PINK_BEEPER_FLUFF_BLOCK);
    }

    private static void addItemsToCombatTab(FabricItemGroupEntries entries) {
        entries.accept(ItemRegistry.BEEKEEPER_HAT);
        entries.accept(ItemRegistry.BEEKEEPER_TUNIC);
        entries.accept(ItemRegistry.BEEKEEPER_PANTS);
        entries.accept(ItemRegistry.BEEKEEPER_BOOTS);
    }

    private static void addItemsToBuildingBlocksTab(FabricItemGroupEntries entries) {
        entries.accept(BlockRegistry.MIGNONETTE_LOG);
        entries.accept(BlockRegistry.MIGNONETTE_WOOD);
        entries.accept(BlockRegistry.STRIPPED_MIGNONETTE_LOG);
        entries.accept(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);
        entries.accept(BlockRegistry.MIGNONETTE_PLANKS);
        entries.accept(BlockRegistry.MIGNONETTE_STAIRS);
        entries.accept(BlockRegistry.MIGNONETTE_SLAB);
        entries.accept(BlockRegistry.MIGNONETTE_FENCE);
        entries.accept(BlockRegistry.MIGNONETTE_FENCE_GATE);
        entries.accept(BlockRegistry.MIGNONETTE_DOOR);
        entries.accept(BlockRegistry.MIGNONETTE_TRAPDOOR);
        entries.accept(BlockRegistry.MIGNONETTE_PRESSURE_PLATE);
        entries.accept(BlockRegistry.MIGNONETTE_BUTTON);
        entries.accept(BlockRegistry.LOAM_BRICKS);
        entries.accept(BlockRegistry.LOAM_BRICK_STAIRS);
        entries.accept(BlockRegistry.LOAM_BRICK_SLAB);
        entries.accept(BlockRegistry.LOAM_BRICK_WALL);
    }

    private static void addItemsToSpawnEggsTab(FabricItemGroupEntries entries) {
        entries.accept(ItemRegistry.BEEPER_SPAWN_EGG);
        entries.accept(ItemRegistry.SKELETON_BEEKEEPER_SPAWN_EGG);
    }

    private static void addItemsToFoodAndDrinksTab(FabricItemGroupEntries entries) {
        entries.accept(ItemRegistry.SPICY_HONEY_BOTTLE);
        entries.accept(ItemRegistry.DRAGONFRUIT);
        entries.accept(ItemRegistry.AMARANTH);
    }

    private static void addItemsToIngredientsTab(FabricItemGroupEntries entries) {
        entries.accept(ItemRegistry.SPICY_HONEYCOMB);
        entries.accept(ItemRegistry.BEEPER_FLUFF);
    }

    private static void addItemsToFunctionalBlocksTab(FabricItemGroupEntries entries) {
        entries.accept(BlockRegistry.BEEPER_HIVE);
    }

    private static void addItemsToNaturalBlocksTab(FabricItemGroupEntries entries) {
        entries.accept(BlockRegistry.LOAM);
        entries.accept(BlockRegistry.MIGNONETTE_LEAVES);
        entries.accept(ItemRegistry.BUTTERDEW_SQUASH_SEEDS);
        entries.accept(BlockRegistry.BUTTERDEW_SQUASH);
        entries.accept(BlockRegistry.CARVED_BUTTERDEW_SQUASH);
        entries.accept(BlockRegistry.BUTTERDEW_LANTERN);
        entries.accept(BlockRegistry.CAWVED_BUTTEWDEW_SQUASH);
        entries.accept(BlockRegistry.BUTTEWDEW_LANTEWN);
        entries.accept(ItemRegistry.DRAGONFRUIT_SEEDS);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(JeepersBeepers.MOD_ID, name), item);
    }

    public static void registerModItems() {
        JeepersBeepers.LOGGER.info("Registering Mod Items for " + JeepersBeepers.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(ItemRegistry::addItemsToColoredBlocksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COMBAT).register(ItemRegistry::addItemsToCombatTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(ItemRegistry::addItemsToBuildingBlocksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(ItemRegistry::addItemsToSpawnEggsTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(ItemRegistry::addItemsToFoodAndDrinksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(ItemRegistry::addItemsToIngredientsTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(ItemRegistry::addItemsToFunctionalBlocksTab);
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(ItemRegistry::addItemsToNaturalBlocksTab);

    }
}
