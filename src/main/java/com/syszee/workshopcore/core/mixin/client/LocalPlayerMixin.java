package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.core.WCPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public final class LocalPlayerMixin {
	@Shadow
	public Input input;
	
	@Inject(method = "isHandsBusy", at = @At("HEAD"), cancellable = true)
	private void makeHandsBusyWhenFrozen(CallbackInfoReturnable<Boolean> info) {
		if ((Object) this instanceof WCPlayer wcPlayer && wcPlayer.isFrozen()) info.setReturnValue(true);
	}

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/Input;tick(ZF)V", shift = At.Shift.AFTER))
	private void resetInputIfFrozen(CallbackInfo info) {
		if (((WCPlayer) (Object) this).isFrozen()) {
			Input input = this.input;
			input.up = false;
			input.down = false;
			input.left = false;
			input.right = false;
			input.forwardImpulse = 0.0F;
			input.leftImpulse = 0.0F;
			input.jumping = false;
			input.shiftKeyDown = false;
		}
	}

	@Inject(method = "drop", at = @At("HEAD"), cancellable = true)
	public void cancelDropIfFrozen(boolean dropAllItems, CallbackInfoReturnable<Boolean> info) {
		if ((Object) this instanceof WCPlayer wcPlayer && wcPlayer.isFrozen()) info.setReturnValue(false);
	}
}
