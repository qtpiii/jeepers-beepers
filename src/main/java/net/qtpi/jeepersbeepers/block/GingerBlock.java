package net.qtpi.jeepersbeepers.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class GingerBlock extends CropBlock {
    public GingerBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        return ItemRegistry.GINGER;
    }
}
