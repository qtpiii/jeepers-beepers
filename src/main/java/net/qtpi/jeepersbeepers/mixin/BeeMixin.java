package net.qtpi.jeepersbeepers.mixin;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.UUID;

@Mixin(Bee.class)
public abstract class BeeMixin extends Mob {

    protected BeeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow @Nullable public abstract UUID getPersistentAngerTarget();

    @Inject(at = @At("HEAD"), method = "setRolling", cancellable = true)
    private void setRolling(boolean isRolling, CallbackInfo ci) {
        Set<Item> entityWornArmor = new ObjectOpenHashSet<>();
        LivingEntity target = getTarget();
        if (target != null) {
            for (ItemStack stack : target.getArmorSlots()) {
                entityWornArmor.add(stack.getItem());
            }
            if (entityWornArmor.containsAll(ObjectArrayList.of(
                    ItemRegistry.BEEKEEPER_BOOTS,
                    ItemRegistry.BEEKEEPER_PANTS,
                    ItemRegistry.BEEKEEPER_TUNIC,
                    ItemRegistry.BEEKEEPER_HAT))) {
                ci.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "doHurtTarget", cancellable = true)
    private void doHurtTarget(Entity target, CallbackInfoReturnable<Boolean> cir) {
        Set<Item> entityWornArmor = new ObjectOpenHashSet<>();
        for (ItemStack stack : target.getArmorSlots()) {
            entityWornArmor.add(stack.getItem());
        }
        if (entityWornArmor.containsAll(ObjectArrayList.of(
                ItemRegistry.BEEKEEPER_BOOTS,
                ItemRegistry.BEEKEEPER_PANTS,
                ItemRegistry.BEEKEEPER_TUNIC,
                ItemRegistry.BEEKEEPER_HAT))) {
            cir.setReturnValue(false);
        }
    }
}
