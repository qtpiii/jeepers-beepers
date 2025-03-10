package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.CompatibilityTagRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(TagRegistry.Items.BEEPER_FOOD)
                .add(Items.WHEAT)
                .add(Items.POTATO)
                .add(Items.BEETROOT)
                .add(Items.CARROT)
                .add(Items.PITCHER_PLANT)
                .add(Items.TORCHFLOWER)
                .add(ItemRegistry.DRAGONFRUIT)
                .add(ItemRegistry.AMARANTH)
                .add(ItemRegistry.BRADDISH)
                .add(ItemRegistry.GINGER);
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(BlockRegistry.MIGNONETTE_PLANKS.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_PLANKS.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
                .add(BlockRegistry.MIGNONETTE_BUTTON.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_BUTTON.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
                .add(BlockRegistry.MIGNONETTE_DOOR.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_DOOR.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_TRAPDOOR.asItem())
                .add(BlockRegistry.MIGNONETTE_TRAPDOOR.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
                .add(BlockRegistry.MIGNONETTE_STAIRS.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_STAIRS.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
                .add(BlockRegistry.MIGNONETTE_SLAB.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_SLAB.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
                .add(BlockRegistry.MIGNONETTE_FENCE.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_FENCE.asItem());
        getOrCreateTagBuilder(ItemTags.FENCE_GATES)
                .add(BlockRegistry.MIGNONETTE_FENCE_GATE.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_FENCE_GATE.asItem());
        getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
                .add(BlockRegistry.MIGNONETTE_PRESSURE_PLATE.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_PRESSURE_PLATE.asItem());
        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .add(BlockRegistry.MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.MIGNONETTE_WOOD.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD.asItem());
        getOrCreateTagBuilder(ItemTags.LOGS)
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_WOOD.asItem())
                .add(BlockRegistry.STRIPPED_PETRIFIED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.STRIPPED_PETRIFIED_MIGNONETTE_WOOD.asItem());
        getOrCreateTagBuilder(TagRegistry.Items.MIGNONETTE_LOGS)
                .add(BlockRegistry.MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.MIGNONETTE_WOOD.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD.asItem());
        getOrCreateTagBuilder(TagRegistry.Items.PETRIFIED_MIGNONETTE_LOGS)
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.PETRIFIED_MIGNONETTE_WOOD.asItem())
                .add(BlockRegistry.STRIPPED_PETRIFIED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.STRIPPED_PETRIFIED_MIGNONETTE_WOOD.asItem());

        getOrCreateTagBuilder(CompatibilityTagRegistry.CROPS)
                .add(BlockRegistry.BUTTERDEW_SQUASH.asItem())
                .add(ItemRegistry.DRAGONFRUIT)
                .add(ItemRegistry.AMARANTH)
                .add(ItemRegistry.BRADDISH)
                .add(ItemRegistry.GINGER);
    }
}
