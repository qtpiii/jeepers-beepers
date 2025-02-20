package net.qtpi.jeepersbeepers.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.qtpi.jeepersbeepers.JeepersBeepers;

public class CompatibilityTagRegistry {

    public static final TagKey<Block> DYED_WHITE = conventionalBlockTag("dyed/white");
    public static final TagKey<Block> DYED_LIGHT_GRAY = conventionalBlockTag("dyed/light_gray");
    public static final TagKey<Block> DYED_GRAY = conventionalBlockTag("dyed/gray");
    public static final TagKey<Block> DYED_BLACK = conventionalBlockTag("dyed/black");
    public static final TagKey<Block> DYED_BROWN = conventionalBlockTag("dyed/brown");
    public static final TagKey<Block> DYED_RED = conventionalBlockTag("dyed/red");
    public static final TagKey<Block> DYED_ORANGE = conventionalBlockTag("dyed/orange");
    public static final TagKey<Block> DYED_YELLOW = conventionalBlockTag("dyed/yellow");
    public static final TagKey<Block> DYED_LIME = conventionalBlockTag("dyed/lime");
    public static final TagKey<Block> DYED_GREEN = conventionalBlockTag("dyed/green");
    public static final TagKey<Block> DYED_CYAN = conventionalBlockTag("dyed/cyan");
    public static final TagKey<Block> DYED_LIGHT_BLUE = conventionalBlockTag("dyed/light_blue");
    public static final TagKey<Block> DYED_BLUE = conventionalBlockTag("dyed/blue");
    public static final TagKey<Block> DYED_PURPLE = conventionalBlockTag("dyed/purple");
    public static final TagKey<Block> DYED_MAGENTA = conventionalBlockTag("dyed/magenta");
    public static final TagKey<Block> DYED_PINK = conventionalBlockTag("dyed/pink");


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
