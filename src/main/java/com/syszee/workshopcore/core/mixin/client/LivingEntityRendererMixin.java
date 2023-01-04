package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.common.entity.Body;
import com.syszee.workshopcore.core.WCPlayer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@Inject(method = "getWhiteOverlayProgress", at = @At("HEAD"), cancellable = true)
	private void getWhiteOverlayProgress(LivingEntity livingEntity, float partialTicks, CallbackInfoReturnable<Float> info) {
		if (livingEntity instanceof WCPlayer) {
			float g = ((WCPlayer) livingEntity).getSwelling(partialTicks);
			info.setReturnValue((int)(g * 10.0F) % 2 == 0 ? 0.0F : Mth.clamp(g, 0.5F, 1.0F));
		}
	}

	@Inject(method = "shouldShowName", at = @At("HEAD"), cancellable = true)
	private void shouldShowNameIfHasCoinChoice(LivingEntity livingEntity, CallbackInfoReturnable<Boolean> info) {
		if (Body.isClientBody(livingEntity)) {
			info.setReturnValue(false);
			return;
		}
		if (livingEntity instanceof WCPlayer wcPlayer && wcPlayer.getCoinChoice() != WCPlayer.CoinChoice.UNDEFINED) info.setReturnValue(true);
	}

}
