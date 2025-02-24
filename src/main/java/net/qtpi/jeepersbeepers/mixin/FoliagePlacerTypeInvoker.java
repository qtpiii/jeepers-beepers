package net.qtpi.jeepersbeepers.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FoliagePlacerType.class)
public interface FoliagePlacerTypeInvoker {

    @Invoker
    public static <P extends FoliagePlacer>FoliagePlacerType<P> callRegister(String id, Codec<P> codec) {
        throw new IllegalStateException();
    }
}
