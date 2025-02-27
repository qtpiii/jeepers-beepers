package net.qtpi.jeepersbeepers.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.qtpi.jeepersbeepers.JeepersBeepers;

public class CompatibilityTagRegistry {

    public static final TagKey<Block> FARMLAND = conventionalBlockTag("blocks/farmland");

    public static final TagKey<Item> CROPS = conventionalItemTag("crops");


    private static TagKey<Block> conventionalBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation("c", name));
    }
    private static TagKey<Block> externalBlockTag(String modId, String name) {
        return TagKey.create(Registries.BLOCK, new ResourceLocation(modId, name));
    }

    private static TagKey<Item> conventionalItemTag(String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation("c", name));
    }
    private static TagKey<Item> externalItemTag(String modId, String name) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(modId, name));
    }

    public static void registerCompatibilityTags() {
        JeepersBeepers.LOGGER.info("Registering Compatibility Tags for " + JeepersBeepers.MOD_ID);
    }
}
