package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.CompatibilityTagRegistry;
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
                .add(Items.CARROT);
        getOrCreateTagBuilder(ItemTags.PLANKS)
                .add(BlockRegistry.MIGNONETTE_PLANKS.asItem());
        getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
                .add(BlockRegistry.MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.MIGNONETTE_WOOD.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD.asItem());
        getOrCreateTagBuilder(TagRegistry.Items.MIGNONETTE_LOGS)
                .add(BlockRegistry.MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.MIGNONETTE_WOOD.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_LOG.asItem())
                .add(BlockRegistry.STRIPPED_MIGNONETTE_WOOD.asItem());
    }
}
