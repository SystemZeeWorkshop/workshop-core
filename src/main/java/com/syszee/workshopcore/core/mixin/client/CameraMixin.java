package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.core.math.Vector2f;
import com.syszee.workshopcore.core.screenshake.ScreenShaker;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public final class CameraMixin {
	@Shadow
	protected void setRotation(float yRot, float xRot) {}

	@Inject(method = "tick", at = @At("HEAD"))
	private void tickShakingIntensity(CallbackInfo info) {
		ScreenShaker.tick((Camera) (Object) this);
	}

	@Inject(at = @At("TAIL"), method = "setup")
	public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float partialTicks, CallbackInfo info) {
		Camera camera = (Camera) (Object) this;
		Vector2f shake = ScreenShaker.getShake(camera, partialTicks);
		float xShake = shake.x();
		float yShake = shake.y();
		if (Mth.abs(xShake) >= 0.01F || Mth.abs(yShake) >= 0.01F) {
			this.setRotation(camera.getYRot() + 10.0F * yShake, camera.getXRot() + 10.0F * xShake);
		}
	}
}
