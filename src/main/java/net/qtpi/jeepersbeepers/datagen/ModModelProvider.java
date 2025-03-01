package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.resources.ResourceLocation;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;

public class ModModelProvider extends FabricModelProvider {

    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {
        blockModelGenerators.createTrivialCube(BlockRegistry.BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.WHITE_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.LIGHT_GRAY_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.GRAY_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.BLACK_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.BROWN_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.RED_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.ORANGE_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.YELLOW_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.LIME_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.GREEN_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.CYAN_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.LIGHT_BLUE_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.BLUE_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.PURPLE_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.MAGENTA_BEEPER_FLUFF_BLOCK);
        blockModelGenerators.createTrivialCube(BlockRegistry.PINK_BEEPER_FLUFF_BLOCK);

        blockModelGenerators.createTrivialCube(BlockRegistry.LOAM);

        blockModelGenerators.createTrivialCube(BlockRegistry.SPICY_HONEYCOMB_BLOCK);

        blockModelGenerators.woodProvider(BlockRegistry.MIGNONETTE_LOG)
                .log(BlockRegistry.MIGNONETTE_LOG).wood(BlockRegistry.MIGNONETTE_WOOD);
        blockModelGenerators.woodProvider(BlockRegistry.STRIPPED_MIGNONETTE_LOG)
                .log(BlockRegistry.STRIPPED_MIGNONETTE_LOG).wood(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);
        blockModelGenerators.createTrivialCube(BlockRegistry.MIGNONETTE_LEAVES);

        BlockModelGenerators.BlockFamilyProvider mignonette_family = blockModelGenerators.family(BlockRegistry.MIGNONETTE_PLANKS);
        mignonette_family.stairs(BlockRegistry.MIGNONETTE_STAIRS);
        mignonette_family.slab(BlockRegistry.MIGNONETTE_SLAB);
        mignonette_family.button(BlockRegistry.MIGNONETTE_BUTTON);
        mignonette_family.pressurePlate(BlockRegistry.MIGNONETTE_PRESSURE_PLATE);
        mignonette_family.fence(BlockRegistry.MIGNONETTE_FENCE);
        mignonette_family.fenceGate(BlockRegistry.MIGNONETTE_FENCE_GATE);

        blockModelGenerators.createDoor(BlockRegistry.MIGNONETTE_DOOR);
        blockModelGenerators.createOrientableTrapdoor(BlockRegistry.MIGNONETTE_TRAPDOOR);

        blockModelGenerators.createCrossBlockWithDefaultItem(BlockRegistry.MIGNONETTE_SAPLING, BlockModelGenerators.TintState.NOT_TINTED);
        blockModelGenerators.createCrossBlockWithDefaultItem(BlockRegistry.FLOWERING_MIGNONETTE_SAPLING, BlockModelGenerators.TintState.NOT_TINTED);

        blockModelGenerators.createDoublePlant(BlockRegistry.WILD_AMARANTH, BlockModelGenerators.TintState.NOT_TINTED);
    }


    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(ItemRegistry.SPICY_HONEY_BOTTLE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.SPICY_HONEYCOMB, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BEEPER_FLUFF, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BEEKEEPER_HAT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BEEKEEPER_TUNIC, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BEEKEEPER_PANTS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BEEKEEPER_BOOTS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(BlockRegistry.MIGNONETTE_FLOWER.asItem(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.DRAGONFRUIT_SEEDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.DRAGONFRUIT, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.AMARANTH_SEEDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.AMARANTH, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BRADDISH_SEEDS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.BRADDISH, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(ItemRegistry.POLLEN_PUFF, ModelTemplates.FLAT_ITEM);
    }
}
