package net.qtpi.jeepersbeepers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.qtpi.jeepersbeepers.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

	@Shadow protected abstract Player getCameraPlayer();

	@Shadow protected abstract void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int yOffset, boolean renderHighlight, boolean halfHeart);

	@Inject(at = @At("HEAD"), method = "renderHeart", cancellable = true)
	private void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int yOffset, boolean renderHighlight, boolean halfHeart, CallbackInfo ci) {
		if (!(heartType.equals(Gui.HeartType.NORMAL) || heartType.equals(Gui.HeartType.CONTAINER))) return;

		boolean container = heartType.equals(Gui.HeartType.CONTAINER);

		Player player = this.getCameraPlayer();
		if (player == null) return;

		if (player.hasEffect(EffectRegistry.INVIGORATION)) {
			renderInvigoratedHeart(ci, guiGraphics, x, y, yOffset, halfHeart, container);
		}
	}

	@ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z", ordinal = 0))
	private boolean hasEffectRedirect(boolean original) {
		return original || this.getCameraPlayer().hasEffect(EffectRegistry.INVIGORATION);
	}

	@Unique
	private static void renderInvigoratedHeart(CallbackInfo ci, GuiGraphics guiGraphics, int x, int y, int yOffset, boolean half, boolean container) {
		if (container) return;
		ResourceLocation texture = new ResourceLocation("textures/gui/sprites/hud/heart/invigorated" + (half ? "_half.png" : "_full.png"));
		guiGraphics.blit(texture, x - 2, y - 4, 0, 0, 13, 13, 13, 13);
		ci.cancel();
	}
}