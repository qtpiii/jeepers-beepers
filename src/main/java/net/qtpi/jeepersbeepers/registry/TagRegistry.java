package net.qtpi.jeepersbeepers.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.qtpi.jeepersbeepers.JeepersBeepers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class TagRegistry {
    public static class Blocks {
        public static final HashMap<TagKey<Block>, Integer> CROP_TAG_TO_INTEGER = new HashMap<>();
        public static final HashMap<Integer, TagKey<Block>> INTEGER_TO_CROP_TAG = new HashMap<>();
        public static final HashMap<TagKey<Block>, BlockState> CROP_TAG_TO_HYBRID_BLOCKSTATE = new HashMap<>();
        public static final HashMap<TagKey<Block>, Double> CROP_TAG_TO_WEIGHT = new HashMap<>();
        public static final ArrayList<TagKey<Block>> ALL_CROP_TAGS = new ArrayList<>();

        public static ArrayList<Integer> cropTagListToIntegerList(ArrayList<TagKey<Block>> list) {
            ArrayList<Integer> ret = new ArrayList<>();
            for (var tag : list) {
                if (ALL_CROP_TAGS.contains(tag)) {
                    ret.add(CROP_TAG_TO_INTEGER.get(tag));
                }
            }

            Collections.sort(ret);

            return ret;
        }

        public static ArrayList<TagKey<Block>> integerListToCropTagList(ArrayList<Integer> list) {
            Collections.sort(list);

            ArrayList<TagKey<Block>> ret = new ArrayList<>();
            for (Integer num : list) {
                ret.add(INTEGER_TO_CROP_TAG.get(num));
            }
            return ret;
        }

        public static ArrayList<TagKey<Block>> integerListToCropTagList(int[] list) {
            ArrayList<TagKey<Block>> ret = new ArrayList<>();
            for (Integer num : list) {
                ret.add(INTEGER_TO_CROP_TAG.get(num));
            }
            return ret;
        }

        public static BlockState pickHybridFromTags(ArrayList<TagKey<Block>> tags, RandomSource random) {
            ArrayList<Double> weights = new ArrayList<>();
            for (var tag : tags) {
                if (CROP_TAG_TO_WEIGHT.get(tag) == null) {
                    weights.add(0.0);
                    continue;
                }
                weights.add(CROP_TAG_TO_WEIGHT.get(tag));
            }
            double sum = weights.stream().mapToDouble(x -> x).sum();
            // scale all weights so their sum is equal to one
            double scaleFac = 1 / sum;
            weights.replaceAll(x -> x * scaleFac);

            double rand = random.nextDouble();
            for (int i = 0; i < weights.size(); i++) {
                double prev;
                if (i == 0) {
                    prev = 0.0;
                }
                else {
                    prev = weights.get(i - 1);
                }

                if (prev < rand && rand < weights.get(i)) {
                    return CROP_TAG_TO_HYBRID_BLOCKSTATE.get(tags.get(i));
                }
            }

            return net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
        }

        public static final TagKey<Block> SEED_CROPS = createTag("seed_crops", true, 4);
        public static final TagKey<Block> SEEDLESS_CROPS = createTag("seedless_crops", true, BlockRegistry.GINGER, 8);
        public static final TagKey<Block> BLOCK_CROPS = createTag("block_crops", true, BlockRegistry.BUTTERDEW_SQUASH_STEM, 8);
        public static final TagKey<Block> MULTI_YIELD_CROPS = createTag("multi_yield_crops", true, BlockRegistry.DRAGONFRUIT_TREE, 6);
        public static final TagKey<Block> SINGLE_YIELD_CROPS = createTag("single_yield_crops", true, 4);
        public static final TagKey<Block> FRUIT_CROPS = createTag("fruit_crops", true, 8);
        public static final TagKey<Block> VEGETABLE_CROPS = createTag("vegetable_crops", true, BlockRegistry.BRADDISH, 4);
        public static final TagKey<Block> ANCIENT_CROPS = createTag("ancient_crops", true, 6);
        public static final TagKey<Block> GRAIN_CROPS = createTag("grain_crops", true, 8);
        public static final TagKey<Block> HYBRID_CROPS = createTag("hybrid_crops", true, 2);

        public static final TagKey<Block> BEEPER_HIVES = createTag("beeper_hives", false);

        public static final TagKey<Block> BEEPER_CAN_POLLINATE = createTag("beeper_can_pollinate", false);

        public static final TagKey<Block> BEEPER_FLUFF_BLOCKS = createTag("beeper_fluff_blocks", false);

        private static TagKey<Block> createTag(String name, boolean cropTag) {
            return createTag(name, cropTag, net.minecraft.world.level.block.Blocks.AIR, 0.0);
        }

        private static TagKey<Block> createTag(String name, boolean cropTag, double weight) {
            return createTag(name, cropTag, net.minecraft.world.level.block.Blocks.AIR, weight);
        }

        private static TagKey<Block> createTag(String name, boolean cropTag, Block associatedCrop, double weight) {
            var tag = TagKey.create(Registries.BLOCK, new ResourceLocation(JeepersBeepers.MOD_ID, name));

            if (cropTag) {
                CROP_TAG_TO_INTEGER.put(tag, CROP_TAG_TO_INTEGER.size());
                INTEGER_TO_CROP_TAG.put(INTEGER_TO_CROP_TAG.size(), tag);
                ALL_CROP_TAGS.add(tag);
                CROP_TAG_TO_HYBRID_BLOCKSTATE.put(tag, associatedCrop.defaultBlockState());
                CROP_TAG_TO_WEIGHT.put(tag, weight);
            }

            return tag;
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
