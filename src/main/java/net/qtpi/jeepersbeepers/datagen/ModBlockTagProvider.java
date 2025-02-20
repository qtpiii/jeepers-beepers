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
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
                .add(BlockRegistry.BEEPER_HIVE);
        getOrCreateTagBuilder(BlockTags.OCCLUDES_VIBRATION_SIGNALS)
                .add(BlockRegistry.BEEPER_FLUFF_BLOCK);
        getOrCreateTagBuilder(BlockTags.DAMPENS_VIBRATIONS)
                .add(BlockRegistry.BEEPER_FLUFF_BLOCK);

        getOrCreateTagBuilder(TagRegistry.Blocks.SEED_CROPS)
                .add(Blocks.WHEAT)
                .add(Blocks.BEETROOTS)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP);
        getOrCreateTagBuilder(TagRegistry.Blocks.SEEDLESS_CROPS)
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS);
        getOrCreateTagBuilder(TagRegistry.Blocks.BLOCK_CROPS)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM);
        getOrCreateTagBuilder(TagRegistry.Blocks.MULTI_YIELD_CROPS)
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS)
                .add(Blocks.BEETROOTS);
        getOrCreateTagBuilder(TagRegistry.Blocks.SINGLE_YIELD_CROPS)
                .add(Blocks.WHEAT)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP);
        getOrCreateTagBuilder(TagRegistry.Blocks.FRUIT_CROPS)
                .add(Blocks.MELON_STEM)
                .add(Blocks.PUMPKIN_STEM);
        getOrCreateTagBuilder(TagRegistry.Blocks.VEGETABLE_CROPS)
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS)
                .add(Blocks.BEETROOTS);
        getOrCreateTagBuilder(TagRegistry.Blocks.ANCIENT_CROPS)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP);
        getOrCreateTagBuilder(TagRegistry.Blocks.GRAIN_CROPS)
                .add(Blocks.WHEAT);

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

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(BlockRegistry.MIGNONETTE_LOG)
                .add(BlockRegistry.MIGNONETTE_WOOD)
                .add(BlockRegistry.STRIPPED_MIGNONETTE_LOG)
                .add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);

        getOrCreateTagBuilder(BlockTags.FENCES)
                .add(BlockRegistry.MIGNONETTE_FENCE);
        getOrCreateTagBuilder(BlockTags.FENCE_GATES)
                .add(BlockRegistry.MIGNONETTE_FENCE_GATE);
    }
}
