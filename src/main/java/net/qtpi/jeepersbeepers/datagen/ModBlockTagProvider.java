package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.CompatibilityTagRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class  ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegistry.LOAM_BRICKS)
                .add(BlockRegistry.LOAM_BRICK_STAIRS)
                .add(BlockRegistry.LOAM_BRICK_SLAB)
                .add(BlockRegistry.LOAM_BRICK_WALL)
                .add(BlockRegistry.SHALE)
                .add(BlockRegistry.SHALE_STAIRS)
                .add(BlockRegistry.SHALE_SLAB);
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
        getOrCreateTagBuilder(BlockTags.OCCLUDES_VIBRATION_SIGNALS)
                .addTag(TagRegistry.Blocks.BEEPER_FLUFF_BLOCKS);
        getOrCreateTagBuilder(BlockTags.DAMPENS_VIBRATIONS)
                .addTag(TagRegistry.Blocks.BEEPER_FLUFF_BLOCKS);

        getOrCreateTagBuilder(BlockTags.DIRT)
                .add(BlockRegistry.LOAM);

        getOrCreateTagBuilder(BlockTags.BASE_STONE_OVERWORLD);

        getOrCreateTagBuilder(CompatibilityTagRegistry.FARMLAND)
                .add(BlockRegistry.LOAM_FARMLAND);

        getOrCreateTagBuilder(TagRegistry.Blocks.SEED_CROPS) // 4
                .add(Blocks.WHEAT)
                .add(Blocks.BEETROOTS)
                .add(Blocks.MELON_STEM)
                .add(Blocks.ATTACHED_MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.ATTACHED_PUMPKIN_STEM)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP)
                .add(BlockRegistry.DRAGONFRUIT_TREE)
                .add(BlockRegistry.AMARANTH)
                .add(BlockRegistry.WILD_AMARANTH)
                .add(BlockRegistry.BRADDISH);
        getOrCreateTagBuilder(TagRegistry.Blocks.SEEDLESS_CROPS) // 8
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS)
                .add(BlockRegistry.GINGER);
        getOrCreateTagBuilder(TagRegistry.Blocks.BLOCK_CROPS) // 8
                .add(Blocks.MELON_STEM)
                .add(Blocks.ATTACHED_MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.ATTACHED_PUMPKIN_STEM)
                .add(BlockRegistry.BUTTERDEW_SQUASH_STEM);
        getOrCreateTagBuilder(TagRegistry.Blocks.MULTI_YIELD_CROPS) // 6
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS)
                .add(Blocks.BEETROOTS)
                .add(BlockRegistry.DRAGONFRUIT_TREE)
                .add(BlockRegistry.GINGER);
        getOrCreateTagBuilder(TagRegistry.Blocks.SINGLE_YIELD_CROPS) // 4
                .add(Blocks.WHEAT)
                .add(Blocks.MELON_STEM)
                .add(Blocks.ATTACHED_MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.ATTACHED_PUMPKIN_STEM)
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP)
                .add(BlockRegistry.AMARANTH)
                .add(BlockRegistry.WILD_AMARANTH)
                .add(BlockRegistry.BRADDISH);
        getOrCreateTagBuilder(TagRegistry.Blocks.FRUIT_CROPS) // 8
                .add(Blocks.MELON_STEM)
                .add(Blocks.ATTACHED_MELON_STEM)
                .add(Blocks.PUMPKIN_STEM)
                .add(Blocks.ATTACHED_PUMPKIN_STEM)
                .add(BlockRegistry.BUTTERDEW_SQUASH_STEM)
                .add(BlockRegistry.ATTACHED_BUTTERDEW_SQUASH_STEM)
                .add(BlockRegistry.DRAGONFRUIT_TREE);
        getOrCreateTagBuilder(TagRegistry.Blocks.VEGETABLE_CROPS) // 4
                .add(Blocks.POTATOES)
                .add(Blocks.CARROTS)
                .add(Blocks.BEETROOTS)
                .add(BlockRegistry.BRADDISH)
                .add(BlockRegistry.GINGER);
        getOrCreateTagBuilder(TagRegistry.Blocks.ANCIENT_CROPS) // 6
                .add(Blocks.TORCHFLOWER_CROP)
                .add(Blocks.PITCHER_CROP);
        getOrCreateTagBuilder(TagRegistry.Blocks.GRAIN_CROPS) // 8
                .add(Blocks.WHEAT)
                .add(BlockRegistry.AMARANTH)
                .add(BlockRegistry.WILD_AMARANTH);
        getOrCreateTagBuilder(TagRegistry.Blocks.HYBRID_CROPS) // 2
                .add(BlockRegistry.BUTTERDEW_SQUASH_STEM)
                .add(BlockRegistry.DRAGONFRUIT_TREE)
                .add(BlockRegistry.BRADDISH)
                .add(BlockRegistry.GINGER);

        FabricTagBuilder beeperCanPollinateBuilder = getOrCreateTagBuilder(TagRegistry.Blocks.BEEPER_CAN_POLLINATE);
        for (var tag : TagRegistry.Blocks.ALL_CROP_TAGS) {
            beeperCanPollinateBuilder.addTag(tag);
        }

        getOrCreateTagBuilder(TagRegistry.Blocks.BEEPER_FLUFF_BLOCKS)
                .add(BlockRegistry.BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.WHITE_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.LIGHT_GRAY_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.GRAY_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.BLACK_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.BROWN_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.RED_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.ORANGE_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.YELLOW_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.LIME_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.GREEN_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.CYAN_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.LIGHT_BLUE_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.BLUE_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.PURPLE_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.MAGENTA_BEEPER_FLUFF_BLOCK)
                .add(BlockRegistry.PINK_BEEPER_FLUFF_BLOCK);

        getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
                .add(BlockRegistry.MIGNONETTE_LOG)
                .add(BlockRegistry.MIGNONETTE_WOOD)
                .add(BlockRegistry.STRIPPED_MIGNONETTE_LOG)
                .add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);

        getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(BlockRegistry.MIGNONETTE_PRESSURE_PLATE);

        getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
                .add(BlockRegistry.MIGNONETTE_BUTTON);

        getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
                .add(BlockRegistry.MIGNONETTE_DOOR);

        getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
                .add(BlockRegistry.MIGNONETTE_TRAPDOOR);

        getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
                .add(BlockRegistry.MIGNONETTE_STAIRS);

        getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
                .add(BlockRegistry.MIGNONETTE_SLAB);

        getOrCreateTagBuilder(BlockTags.STAIRS)
                .add(BlockRegistry.LOAM_BRICK_STAIRS)
                .add(BlockRegistry.SHALE_STAIRS);

        getOrCreateTagBuilder(BlockTags.SLABS)
                .add(BlockRegistry.LOAM_BRICK_SLAB)
                .add(BlockRegistry.SHALE_SLAB);

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
