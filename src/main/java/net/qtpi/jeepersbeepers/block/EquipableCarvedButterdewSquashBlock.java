package net.qtpi.jeepersbeepers.block;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Equipable;

public class EquipableCarvedButterdewSquashBlock extends CarvedButterdewSquashBlock implements Equipable {
    public EquipableCarvedButterdewSquashBlock(Properties properties) {
        super(properties);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
