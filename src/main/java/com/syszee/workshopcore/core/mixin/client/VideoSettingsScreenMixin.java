package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.client.WorkshopClientUtil;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.VideoSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(VideoSettingsScreen.class)
public final class VideoSettingsScreenMixin {
	@Inject(method = "options", at = @At("RETURN"), cancellable = true)
	private static void concatenateModdedOptions(Options options, CallbackInfoReturnable<OptionInstance<?>[]> info) {
		var optionsArray = info.getReturnValue();
		if (optionsArray.length > 0) {
			OptionInstance<?>[] moddedVideoOptions = WorkshopClientUtil.getModdedVideoOptions();
			OptionInstance<?>[] result = Arrays.copyOf(optionsArray, optionsArray.length + moddedVideoOptions.length);
			System.arraycopy(moddedVideoOptions, 0, result, optionsArray.length, moddedVideoOptions.length);
			info.setReturnValue(result);
		}
	}
}
