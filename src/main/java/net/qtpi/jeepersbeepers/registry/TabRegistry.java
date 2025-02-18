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
                        entries.accept(ItemRegistry.BEEPER_FLUFF);
                        entries.accept(BlockRegistry.BEEPER_HIVE);
                    }).build());

    public static void registerCreativeModeTabs() {
        JeepersBeepers.LOGGER.info("Registering Item Groups for " + JeepersBeepers.MOD_ID);
    }
}
