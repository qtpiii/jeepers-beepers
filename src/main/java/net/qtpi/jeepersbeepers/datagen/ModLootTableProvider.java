package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;


public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropSelf(BlockRegistry.BEEPER_FLUFF_BLOCK);
        dropSelf(BlockRegistry.BEEPER_HIVE);
        dropWhenSilkTouch(BlockRegistry.BEEPER_NEST);

        dropSelf(BlockRegistry.LOAM);
        dropSelf(BlockRegistry.LOAM_BRICKS);
        dropSelf(BlockRegistry.LOAM_BRICK_STAIRS);
        dropSelf(BlockRegistry.LOAM_BRICK_WALL);
        createSlabItemTable(BlockRegistry.LOAM_BRICK_SLAB);

        dropSelf(BlockRegistry.MIGNONETTE_LOG);
        dropSelf(BlockRegistry.MIGNONETTE_WOOD);
        dropSelf(BlockRegistry.STRIPPED_MIGNONETTE_LOG);
        dropSelf(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);
        dropSelf(BlockRegistry.MIGNONETTE_PLANKS);
        dropSelf(BlockRegistry.MIGNONETTE_SAPLING);
        dropSelf(BlockRegistry.FLOWERING_MIGNONETTE_SAPLING);

        dropSelf(BlockRegistry.MIGNONETTE_STAIRS);
        dropSelf(BlockRegistry.MIGNONETTE_BUTTON);
        dropSelf(BlockRegistry.MIGNONETTE_PRESSURE_PLATE);
        dropSelf(BlockRegistry.MIGNONETTE_FENCE);
        dropSelf(BlockRegistry.MIGNONETTE_FENCE_GATE);
        dropSelf(BlockRegistry.MIGNONETTE_TRAPDOOR);
        createSlabItemTable(BlockRegistry.MIGNONETTE_SLAB);

        createStemDrops(BlockRegistry.BUTTERDEW_SQUASH_STEM, ItemRegistry.BUTTERDEW_SQUASH_SEEDS);
        createAttachedStemDrops(BlockRegistry.ATTACHED_BUTTERDEW_SQUASH_STEM, ItemRegistry.BUTTERDEW_SQUASH_SEEDS);

        dropOther(BlockRegistry.DRAGONFRUIT_TREE, ItemRegistry.DRAGONFRUIT_SEEDS);

        dropSelf(BlockRegistry.BUTTERDEW_SQUASH);
        dropSelf(BlockRegistry.CARVED_BUTTERDEW_SQUASH);
        dropSelf(BlockRegistry.BUTTERDEW_LANTERN);
        dropSelf(BlockRegistry.CAWVED_BUTTEWDEW_SQUASH);
        dropSelf(BlockRegistry.BUTTEWDEW_LANTEWN);

        dropSelf(BlockRegistry.MIGNONETTE_FLOWER);
    }
}
