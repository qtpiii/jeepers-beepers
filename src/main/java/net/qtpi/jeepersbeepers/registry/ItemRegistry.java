package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SpawnEggItem;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.item.SpicyHoneyBottleItem;

public class ItemRegistry {

    public static final Item SPICY_HONEY_BOTTLE = registerItem("spicy_honey_bottle", new SpicyHoneyBottleItem(
            new FabricItemSettings().maxCount(16).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.1f).build())));
    public static final Item SPICY_HONEYCOMB = registerItem("spicy_honeycomb", new Item(new FabricItemSettings()));
    public static final Item BEEPER_SPAWN_EGG = registerItem("beeper_spawn_egg", new SpawnEggItem(EntityRegistry.BEEPER,
            6651479, 12567454, new FabricItemSettings()));
    public static final Item BEEPER_FLUFF = registerItem("beeper_fluff", new Item(new FabricItemSettings()));

    private static void addItemsToColoredBlocksTabItemGroup(FabricItemGroupEntries entries) {
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

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(JeepersBeepers.MOD_ID, name), item);
    }

    public static void registerModItems() {
        JeepersBeepers.LOGGER.info("Registering Mod Items for " + JeepersBeepers.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.COLORED_BLOCKS).register(ItemRegistry::addItemsToColoredBlocksTabItemGroup);
    }
}
