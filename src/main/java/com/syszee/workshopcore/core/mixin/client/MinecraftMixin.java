package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.common.entity.EntityPopup;
import com.syszee.workshopcore.core.WCPlayer;
import com.syszee.workshopcore.core.WorkshopCoreClient;
import com.syszee.workshopcore.core.screenshake.ScreenShaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public final class MinecraftMixin {
	@Nullable
	@Shadow
	public LocalPlayer player;

	@Inject(method = "Lnet/minecraft/client/Minecraft;clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
	private void clearScreenShaker(Screen screen, CallbackInfo info) {
		ScreenShaker.clear();
	}

	@Inject(method = "handleKeybinds", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true)
	private void preventHandlingSomeKeybindsIfFrozen(CallbackInfo info) {
		if (((WCPlayer) this.player).isFrozen()) info.cancel();
	}

	@Inject(method = "shouldEntityAppearGlowing", at = @At("HEAD"), cancellable = true)
	public void allowEntityToGlow(Entity entity, CallbackInfoReturnable<Boolean> info) {
		EntityPopup popup = WorkshopCoreClient.entityPopup;
		if (popup != null && popup.entity() == entity) info.setReturnValue(true);
	}
}
