package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.client.WorkshopClientUtil;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(AccessibilityOptionsScreen.class)
public final class AccessibilityOptionsScreenMixin {
	@Inject(method = "options", at = @At("RETURN"), cancellable = true)
	private static void concatenateModdedOptions(Options options, CallbackInfoReturnable<OptionInstance<?>[]> info) {
		var optionsArray = info.getReturnValue();
		if (optionsArray.length > 0) {
			OptionInstance<?>[] moddedAccessibilityOptions = WorkshopClientUtil.getModdedAccessibilityOptions();
			OptionInstance<?>[] result = Arrays.copyOf(optionsArray, optionsArray.length + moddedAccessibilityOptions.length);
			System.arraycopy(moddedAccessibilityOptions, 0, result, optionsArray.length, moddedAccessibilityOptions.length);
			info.setReturnValue(result);
		}
	}
}
