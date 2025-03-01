package net.qtpi.jeepersbeepers.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.JeepersBeepers;

public class TabRegistry {

    public static final CreativeModeTab JEEPERSBEEPERS_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(JeepersBeepers.MOD_ID, "jeepersbeepers"),
            FabricItemGroup.builder().title(Component.translatable("itemtab.jeepersbeepers"))
                    .icon(() -> new ItemStack(ItemRegistry.SPICY_HONEY_BOTTLE)).displayItems((displayContext, entries) -> {
                        entries.accept(ItemRegistry.SPICY_HONEY_BOTTLE);
                        entries.accept(ItemRegistry.SPICY_HONEYCOMB);
                        entries.accept(ItemRegistry.BEEPER_SPAWN_EGG);
                        entries.accept(ItemRegistry.SKELETON_BEEKEEPER_SPAWN_EGG);
                        entries.accept(ItemRegistry.BEEPER_FLUFF);
                        entries.accept(ItemRegistry.POLLEN_PUFF);
                        entries.accept(BlockRegistry.BEEPER_FLUFF_BLOCK);
                        entries.accept(BlockRegistry.WHITE_BEEPER_FLUFF_BLOCK);
                        entries.accept(BlockRegistry.LIGHT_GRAY_BEEPER_FLUFF_BLOCK);
                        entries.accept(BlockRegistry.GRAY_BEEPER_FLUFF_BLOCK);
                        entries.accept(BlockRegistry.BLACK_BEEPER_FLUFF_BLOCK);
                        entries.accept(BlockRegistry.BROWN_BEEPER_FLUFF_BLOCK);
                        entries.accept(BlockRegistry.RED_BEEPER_FLUFF_BLOCK);
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
                        entries.accept(BlockRegistry.BEEPER_HIVE);
                        entries.accept(BlockRegistry.BEEPER_NEST);
                        entries.accept(BlockRegistry.MIGNONETTE_LOG);
                        entries.accept(BlockRegistry.MIGNONETTE_WOOD);
                        entries.accept(BlockRegistry.STRIPPED_MIGNONETTE_LOG);
                        entries.accept(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);
                        entries.accept(BlockRegistry.MIGNONETTE_LEAVES);
                        entries.accept(BlockRegistry.MIGNONETTE_PLANKS);
                        entries.accept(BlockRegistry.MIGNONETTE_STAIRS);
                        entries.accept(BlockRegistry.MIGNONETTE_SLAB);
                        entries.accept(BlockRegistry.MIGNONETTE_BUTTON);
                        entries.accept(BlockRegistry.MIGNONETTE_PRESSURE_PLATE);
                        entries.accept(BlockRegistry.MIGNONETTE_FENCE);
                        entries.accept(BlockRegistry.MIGNONETTE_FENCE_GATE);
                        entries.accept(BlockRegistry.MIGNONETTE_DOOR);
                        entries.accept(BlockRegistry.MIGNONETTE_TRAPDOOR);
                        entries.accept(BlockRegistry.MIGNONETTE_FLOWER);
                        entries.accept(ItemRegistry.BEEKEEPER_HAT);
                        entries.accept(ItemRegistry.BEEKEEPER_TUNIC);
                        entries.accept(ItemRegistry.BEEKEEPER_PANTS);
                        entries.accept(ItemRegistry.BEEKEEPER_BOOTS);
                        entries.accept(ItemRegistry.BUTTERDEW_SQUASH_SEEDS);
                        entries.accept(BlockRegistry.BUTTERDEW_SQUASH);
                        entries.accept(BlockRegistry.CARVED_BUTTERDEW_SQUASH);
                        entries.accept(BlockRegistry.BUTTERDEW_LANTERN);
                        entries.accept(ItemRegistry.DRAGONFRUIT_SEEDS);
                        entries.accept(ItemRegistry.DRAGONFRUIT);
                        entries.accept(BlockRegistry.WILD_AMARANTH);
                        entries.accept(ItemRegistry.AMARANTH_SEEDS);
                        entries.accept(ItemRegistry.AMARANTH);
                        entries.accept(ItemRegistry.BRADDISH_SEEDS);
                        entries.accept(ItemRegistry.BRADDISH);
                        entries.accept(ItemRegistry.GINGER);
                        entries.accept(BlockRegistry.MIGNONETTE_SAPLING);
                        entries.accept(BlockRegistry.FLOWERING_MIGNONETTE_SAPLING);
                        entries.accept(BlockRegistry.LOAM);
                        entries.accept(BlockRegistry.LOAM_FARMLAND);
                        entries.accept(BlockRegistry.LOAM_BRICKS);
                        entries.accept(BlockRegistry.LOAM_TILES);
                        entries.accept(BlockRegistry.LOAM_BRICK_STAIRS);
                        entries.accept(BlockRegistry.LOAM_BRICK_SLAB);
                        entries.accept(BlockRegistry.LOAM_BRICK_WALL);
                        entries.accept(BlockRegistry.SPICY_HONEY_BLOCK);
                        entries.accept(BlockRegistry.SPICY_HONEYCOMB_BLOCK);
                        entries.accept(BlockRegistry.HONEY_LAMP);
                        entries.accept(BlockRegistry.SPICY_HONEY_LAMP);
                    }).build());

    public static void registerCreativeModeTabs() {
        JeepersBeepers.LOGGER.info("Registering Creative Mode Tabs for " + JeepersBeepers.MOD_ID);
    }
}
