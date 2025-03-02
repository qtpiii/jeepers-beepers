package net.qtpi.jeepersbeepers.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.qtpi.jeepersbeepers.registry.BlockEntityRegistry;

public class MignonetteRootBlockEntity extends BlockEntity {
    public MignonetteRootBlockEntity(BlockPos pos, BlockState blockState) {
        super(BlockEntityRegistry.MIGNONETTE_ROOT_BLOCK_ENTITY, pos, blockState);
    }
}
