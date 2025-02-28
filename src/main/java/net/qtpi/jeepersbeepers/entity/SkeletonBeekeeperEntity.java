package net.qtpi.jeepersbeepers.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

public class SkeletonBeekeeperEntity extends Skeleton {
    public SkeletonBeekeeperEntity(EntityType<? extends Skeleton> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Skeleton.createAttributes();
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag
    ) {
        spawnData = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
        RandomSource randomSource = level.getRandom();
        this.populateDefaultEquipmentSlots(randomSource, difficulty);
        this.populateDefaultEquipmentEnchantments(randomSource, difficulty);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(randomSource.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
        if (randomSource.nextDouble() < 0.7)
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.BEEKEEPER_HAT));
        if (randomSource.nextDouble() < 0.7)
            this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(ItemRegistry.BEEKEEPER_TUNIC));
        if (randomSource.nextDouble() < 0.7)
            this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(ItemRegistry.BEEKEEPER_PANTS));
        if (randomSource.nextDouble() < 0.7)
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(ItemRegistry.BEEKEEPER_BOOTS));
        this.armorDropChances[EquipmentSlot.HEAD.getIndex()] = 0.05F;
        this.armorDropChances[EquipmentSlot.CHEST.getIndex()] = 0.05F;
        this.armorDropChances[EquipmentSlot.LEGS.getIndex()] = 0.05F;
        this.armorDropChances[EquipmentSlot.FEET.getIndex()] = 0.05F;

        double rand = randomSource.nextDouble();
        if (rand < 0.50) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
        }
        if (rand < 0.75) {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_HOE));
        }
        else {
            setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_HOE));
        }
        handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 0.0f;

        return spawnData;
    }
}
