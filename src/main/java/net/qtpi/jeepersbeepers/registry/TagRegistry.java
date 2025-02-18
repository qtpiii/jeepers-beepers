package net.qtpi.jeepersbeepers.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.qtpi.jeepersbeepers.JeepersBeepers;


public class TagRegistry {
    public static class Blocks {
        public static final TagKey<PoiType> BEEPER_HOME = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(JeepersBeepers.MOD_ID, "beeper_home"));

        public static final TagKey<Block> BEEPER_HIVES = createTag("beeper_hives");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(JeepersBeepers.MOD_ID, name));
        }

    }

    public static class Items {

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(JeepersBeepers.MOD_ID, name));
        }
    }
}
