package net.qtpi.jeepersbeepers.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;
import net.qtpi.jeepersbeepers.registry.BlockRegistry;


public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropSelf(BlockRegistry.BEEPER_FLUFF_BLOCK);
        dropSelf(BlockRegistry.BEEPER_HIVE);

        dropSelf(BlockRegistry.MIGNONETTE_LOG);
        dropSelf(BlockRegistry.MIGNONETTE_WOOD);
        dropSelf(BlockRegistry.STRIPPED_MIGNONETTE_LOG);
        dropSelf(BlockRegistry.STRIPPED_MIGNONETTE_WOOD);
        dropSelf(BlockRegistry.MIGNONETTE_PLANKS);

        dropSelf(BlockRegistry.MIGNONETTE_STAIRS);
        dropSelf(BlockRegistry.MIGNONETTE_BUTTON);
        dropSelf(BlockRegistry.MIGNONETTE_PRESSURE_PLATE);
        dropSelf(BlockRegistry.MIGNONETTE_FENCE);
        dropSelf(BlockRegistry.MIGNONETTE_FENCE_GATE);
        dropSelf(BlockRegistry.MIGNONETTE_TRAPDOOR);

        createDoorTable(BlockRegistry.MIGNONETTE_DOOR);
        createSlabItemTable(BlockRegistry.MIGNONETTE_SLAB);

        createLeavesDrops(BlockRegistry.MIGNONETTE_LEAVES, BlockRegistry.MIGNONETTE_WOOD, 0.05f);
    }
}
