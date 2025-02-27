package net.qtpi.jeepersbeepers.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.qtpi.jeepersbeepers.registry.EntityRegistry;
import net.qtpi.jeepersbeepers.registry.ItemRegistry;
import net.qtpi.jeepersbeepers.registry.ParticleRegistry;
import net.qtpi.jeepersbeepers.registry.TagRegistry;
import org.jetbrains.annotations.NotNull;

public class ThrownPollenPuff extends ThrowableItemProjectile implements ItemSupplier {
    public ThrownPollenPuff(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ThrownPollenPuff(Level level, LivingEntity shooter) {
        super(EntityRegistry.THROWN_POLLEN_PUFF, shooter, level);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return ItemRegistry.POLLEN_PUFF;
    }

    @Override
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        if (getItem().hasTag()) {
            PollenCloud cloud = new PollenCloud(null, level());
            cloud.setPos(this.position());
            cloud.sourceCropTags = TagRegistry.Blocks.integerListToCropTagList(getItem().getTag().getIntArray("cropTags"));
            cloud.sourceCropPos = NbtUtils.readBlockPos(getItem().getOrCreateTagElement("cropPos"));
            level().addFreshEntity(cloud);
        }
        discard();
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide() && tickCount > 5 && random.nextDouble() < 0.25) {
            spawnFluidParticle(this.level(), this.getX() - (double) 0.3F, this.getX() + (double) 0.3F, this.getZ() - (double) 0.3F, this.getZ() + (double) 0.3F, this.getY((double) 0.5F) + 0.5, ParticleRegistry.BEEPER_SNEEZE_POOF);
        }
    }

    private void spawnFluidParticle(Level level, double startX, double endX, double startZ, double endZ, double posY, ParticleOptions particleOption) {
        level.addParticle(particleOption, Mth.lerp(level.random.nextDouble(), startX, endX), posY, Mth.lerp(level.random.nextDouble(), startZ, endZ), (double) 0.0F, (double) 0.0F, (double) 0.0F);
    }
}
