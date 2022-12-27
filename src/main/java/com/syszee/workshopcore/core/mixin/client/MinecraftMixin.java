package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.core.screenshake.ScreenShaker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public final class MinecraftMixin {
	@Inject(method = "Lnet/minecraft/client/Minecraft;clearLevel(Lnet/minecraft/client/gui/screens/Screen;)V", at = @At("HEAD"))
	private void clearScreenShaker(Screen screen, CallbackInfo info) {
		ScreenShaker.clear();
	}
}
