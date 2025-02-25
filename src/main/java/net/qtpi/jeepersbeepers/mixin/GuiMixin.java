package net.qtpi.jeepersbeepers.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.qtpi.jeepersbeepers.registry.EffectRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class GuiMixin {

	@Shadow protected abstract Player getCameraPlayer();

	@Inject(at = @At("HEAD"), method = "renderHeart", cancellable = true)
	private void renderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int x, int y, int yOffset, boolean renderHighlight, boolean halfHeart, CallbackInfo ci) {
		if (!(heartType.equals(Gui.HeartType.NORMAL) || heartType.equals(Gui.HeartType.CONTAINER))) return;

		boolean container = heartType.equals(Gui.HeartType.CONTAINER);

		Player player = this.getCameraPlayer();
		if (player == null) return;

		if (player.hasEffect(EffectRegistry.INVIGORATION)) {
			renderInvigoratedHeart(ci, guiGraphics, x, y, halfHeart, container);
		}
	}

	@Unique
	private static void renderInvigoratedHeart(CallbackInfo ci, GuiGraphics guiGraphics, int x, int y, boolean half, boolean container) {
		if (container) return;
		ResourceLocation texture = new ResourceLocation("textures/gui/sprites/hud/heart/invigorated" + (half ? "_half.png" : "_full.png"));
		guiGraphics.blit(texture, x, y, 0, 0, 9, 9, 9, 9);
		ci.cancel();
	}
}