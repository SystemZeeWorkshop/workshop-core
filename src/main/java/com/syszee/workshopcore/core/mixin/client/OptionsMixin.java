package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.client.WorkshopClientUtil;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public final class OptionsMixin {
	@Inject(method = "processOptions", at = @At("TAIL"))
	private void processModdedOptions(Options.FieldAccess fieldAccess, CallbackInfo info) {
		WorkshopClientUtil.processModdedOptions(fieldAccess);
	}
}
