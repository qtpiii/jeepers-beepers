package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.world.level.block.Block;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createTrivialCube(BlockRegistry.BEEPER_FLUFF_BLOCK);
    }


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ItemRegistry.SPICY_HONEY_BOTTLE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.SPICY_HONEYCOMB, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BEEPER_FLUFF, ModelTemplates.FLAT_ITEM);
    }
}
