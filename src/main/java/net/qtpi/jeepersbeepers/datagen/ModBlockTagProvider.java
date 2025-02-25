package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.CompatibilityTagRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegistry.LOAM_BRICKS)
                .add(BlockRegistry.LOAM_BRICK_STAIRS)
                .add(BlockRegistry.LOAM_BRICK_SLAB)
                .add(BlockRegistry.LOAM_BRICK_WALL);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(BlockRegistry.LOAM);
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(BlockRegistry.BEEPER_HIVE)
                .add(BlockRegistry.BEEPER_NEST)
                .add(BlockRegistry.BUTTERDEW_SQUASH)
                .add(BlockRegistry.CARVED_BUTTERDEW_SQUASH)
                .add(BlockRegistry.BUTTERDEW_LANTERN)
                .add(BlockRegistry.CAWVED_BUTTEWDEW_SQUASH)
                .add(BlockRegistry.BUTTEWDEW_LANTEWN)
                .add(BlockRegistry.MIGNONETTE_PLANKS)
                .add(BlockRegistry.MIGNONETTE_STAIRS)
                .add(BlockRegistry.MIGNONETTE_SLAB)
                .add(BlockRegistry.MIGNONETTE_BUTTON)
                .add(BlockRegistry.MIGNONETTE_PRESSURE_PLATE)
                .add(BlockRegistry.MIGNONETTE_FENCE)
                .add(BlockRegistry.MIGNONETTE_FENCE_GATE)
                .add(BlockRegistry.MIGNONETTE_DOOR)
                .add(BlockRegistry.MIGNONETTE_TRAPDOOR)
                .add(BlockRegistry.DRAGONFRUIT_TREE);
//        getOrCreateTagBuilder(BlockTags.OCCLUDES_VIBRATION_SIGNALS)
//                .addTag(TagRegistry.Blocks.BEEPER_FLUFF_BLOCKS);
//        getOrCreateTagBuilder(BlockTags.DAMPENS_VIBRATIONS)
//                .addTag(TagRegistry.Blocks.BEEPER_FLUFF_BLOCKS);

        getOrCreateTagBuilder(BlockTags.DIRT)
                .add(BlockRegistry.LOAM);

        getOrCreateTagBuilder(CompatibilityTagRegistry.FARMLAND)
                .add(BlockRegistry.LOAM_FARMLAND);

        getOrCreateTagBuilder(TagRegistry.Blocks.SEED_CROPS)
                .add(Blocks.WHEAT)
                .add(Blocks.BEETROOTS)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP)
                .add(BlockRegistry.DRAGONFRUIT_TREE);
        getOrCreateTagBuilder(TagRegistry.Blocks.SEEDLESS_CROPS)
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS);
        getOrCreateTagBuilder(TagRegistry.Blocks.BLOCK_CROPS)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(BlockRegistry.BUTTERDEW_SQUASH_STEM);
        getOrCreateTagBuilder(TagRegistry.Blocks.MULTI_YIELD_CROPS)
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS)
                .add(Blocks.BEETROOTS)
                .add(BlockRegistry.DRAGONFRUIT_TREE);
        getOrCreateTagBuilder(TagRegistry.Blocks.SINGLE_YIELD_CROPS)
                .add(Blocks.WHEAT)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP);
        getOrCreateTagBuilder(TagRegistry.Blocks.FRUIT_CROPS)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(BlockRegistry.BUTTERDEW_SQUASH_STEM)
                .add(BlockRegistry.DRAGONFRUIT_TREE);
        getOrCreateTagBuilder(TagRegistry.Blocks.VEGETABLE_CROPS)
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS)
                .add(Blocks.BEETROOTS);
        getOrCreateTagBuilder(TagRegistry.Blocks.ANCIENT_CROPS)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP);
        getOrCreateTagBuilder(TagRegistry.Blocks.GRAIN_CROPS)
                .add(Blocks.WHEAT);
//        getOrCreateTagBuilder(TagRegistry.Blocks.HYBRID_CROPS)
//                .add(BlockRegistry.BUTTERDEW_SQUASH_STEM)
//                .add(BlockRegistry.DRAGONFRUIT_TREE);

        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_WHITE)
                .add(BlockRegistry.WHITE_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_LIGHT_GRAY)
                .add(BlockRegistry.LIGHT_GRAY_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_GRAY)
                .add(BlockRegistry.GRAY_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_BLACK)
                .add(BlockRegistry.BLACK_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_BROWN)
                .add(BlockRegistry.BROWN_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_RED)
                .add(BlockRegistry.RED_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_ORANGE)
                .add(BlockRegistry.ORANGE_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_YELLOW)
                .add(BlockRegistry.YELLOW_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_LIME)
                .add(BlockRegistry.LIME_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_GREEN)
                .add(BlockRegistry.GREEN_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_CYAN)
                .add(BlockRegistry.CYAN_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_LIGHT_BLUE)
                .add(BlockRegistry.LIGHT_BLUE_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_BLUE)
                .add(BlockRegistry.BLUE_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_PURPLE)
                .add(BlockRegistry.PURPLE_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_MAGENTA)
                .add(BlockRegistry.MAGENTA_BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(CompatibilityTagRegistry.DYED_PINK)
                .add(BlockRegistry.PINK_BEEPER_FLUFF_BLOCK);

//        getOrCreateTagBuilder(TagRegistry.Blocks.BEEPER_FLUFF_BLOCKS)
//                .add(BlockRegistry.BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.WHITE_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.LIGHT_GRAY_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.GRAY_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.BLACK_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.BROWN_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.RED_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.ORANGE_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.YELLOW_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.LIME_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.GREEN_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.CYAN_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.LIGHT_BLUE_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.BLUE_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.PURPLE_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.MAGENTA_BEEPER_FLUFF_BLOCK)
//                .add(BlockRegistry.PINK_BEEPER_FLUFF_BLOCK);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(BlockRegistry.MIGNONETTE_LOG)
                .add(BlockRegistry.MIGNONETTE_WOOD)
                .add(BlockRegistry.STRIPPED_MIGNONETTE_LOG)
                .add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);

        getOrCreateTagBuilder(BlockTags.FENCES)
                .add(BlockRegistry.MIGNONETTE_FENCE);
        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(BlockRegistry.MIGNONETTE_FENCE_GATE);

        getOrCreateTagBuilder(BlockTags.WALLS)
                .add(BlockRegistry.LOAM_BRICK_WALL);

        getOrCreateTagBuilder(BlockTags.CROPS)
                .add(BlockRegistry.BUTTERDEW_SQUASH_STEM)
                .add(BlockRegistry.DRAGONFRUIT_TREE);

        getOrCreateTagBuilder(BlockTags.LEAVES)
                .add(BlockRegistry.MIGNONETTE_LEAVES);

        getOrCreateTagBuilder(BlockTags.FLOWERS)
                .add(BlockRegistry.MIGNONETTE_FLOWER);
    }
}
