package net.qtpi.jeepersbeepers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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

	@Unique private int heartTicks;

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

	@Inject(at = @At("HEAD"), method = "tick()V")
	private void tick(CallbackInfo ci) {
		heartTicks++;
	}

	@ModifyExpressionValue(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;hasEffect(Lnet/minecraft/world/effect/MobEffect;)Z", ordinal = 0))
	private boolean hasEffectRedirect(boolean original) {
		return original || this.getCameraPlayer().hasEffect(EffectRegistry.INVIGORATION);
	}

	@Unique
	private void renderInvigoratedHeart(CallbackInfo ci, GuiGraphics guiGraphics, int x, int y, int yOffset, boolean half, boolean container) {
		if (container) return;
		Player player = this.getCameraPlayer();
		ResourceLocation texture;
		if (player.getCommandSenderWorld().getLevelData().isHardcore()) {
			texture = new ResourceLocation("textures/gui/sprites/hud/heart/invigorated_hardcore" + (half ? "_half.png" : "_full.png"));
		} else {
			texture = new ResourceLocation("textures/gui/sprites/hud/heart/invigorated" + (half ? "_half.png" : "_full.png"));
		}

		int uOffset = 0;
		switch (this.heartTicks % 8) {
			case 2, 3: {
				uOffset = 13;
				break;
			}
			case 4, 5: {
				uOffset = 26;
				break;
			}
			case 6, 7: {
				uOffset = 39;
				break;
			}
        }
		guiGraphics.blit(texture, x - 2, y - 4, uOffset, 0, 13, 13, 52, 13);
		ci.cancel();
	}
}