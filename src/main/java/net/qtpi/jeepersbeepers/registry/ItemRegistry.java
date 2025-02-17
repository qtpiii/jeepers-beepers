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
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.item.SpicyHoneyBottleItem;

public class ItemRegistry {

    public static final Item SPICY_HONEY_BOTTLE = registerItem("spicy_honey_bottle", new SpicyHoneyBottleItem(
            new FabricItemSettings().maxCount(16).food(new FoodProperties.Builder().nutrition(6).saturationMod(0.1f).build())));

    private static void addItemsToIngredientTabItemGroup(FabricItemGroupEntries entries) {

    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(JeepersBeepers.MOD_ID, name), item);
    }

    public static void registerModItems() {
        JeepersBeepers.LOGGER.info("Registering Mod Items for " + JeepersBeepers.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(ItemRegistry::addItemsToIngredientTabItemGroup);
    }
}
