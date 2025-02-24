package net.qtpi.jeepersbeepers.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.qtpi.jeepersbeepers.JeepersBeepers;
import net.qtpi.jeepersbeepers.entity.BeeperEntity;


public class TagRegistry {
    public static class Blocks {
        public static final TagKey<Block> SEED_CROPS = createTag("seed_crops");
        public static final TagKey<Block> SEEDLESS_CROPS = createTag("seedless_crops");
        public static final TagKey<Block> BLOCK_CROPS = createTag("block_crops");
        public static final TagKey<Block> MULTI_YIELD_CROPS = createTag("multi_yield_crops");
        public static final TagKey<Block> SINGLE_YIELD_CROPS = createTag("single_yield_crops");
        public static final TagKey<Block> FRUIT_CROPS = createTag("fruit_crops");
        public static final TagKey<Block> VEGETABLE_CROPS = createTag("vegetable_crops");
        public static final TagKey<Block> ANCIENT_CROPS = createTag("ancient_crops");
        public static final TagKey<Block> GRAIN_CROPS = createTag("grain_crops");
        public static final TagKey<Block> CROSSBRED_CROPS = createTag("crossbred_crops");

        public static final TagKey<Block> BEEPER_HIVES = createTag("beeper_hives");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(JeepersBeepers.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> BEEPER_FOOD = createTag("beeper_food");

        public static final TagKey<Item> MIGNONETTE_LOGS = createTag("mignonette_logs");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(JeepersBeepers.MOD_ID, name));
        }
    }

    public static class Misc {
        public static final TagKey<PoiType> BEEPER_HOME = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(JeepersBeepers.MOD_ID, "beeper_home"));
        public static final TagKey<EntityType<?>> BEEPER_HIVE_INHABITORS = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(JeepersBeepers.MOD_ID, "beeper_hive_inhabitors"));
    }

    public static void registerModTags() {
        JeepersBeepers.LOGGER.info("Registering Mod Tags for " + JeepersBeepers.MOD_ID);
    }
}
