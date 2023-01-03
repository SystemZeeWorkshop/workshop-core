package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.core.WCPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public final class LivingEntityMixin {
	@Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
	private void cancelTickHeadTurnIfFrozen(float p_21260_, float distanceFromPreviousPos, CallbackInfoReturnable<Float> info) {
		if ((Object) this instanceof WCPlayer wcPlayer && wcPlayer.isFrozen()) info.setReturnValue(distanceFromPreviousPos);
	}
}
