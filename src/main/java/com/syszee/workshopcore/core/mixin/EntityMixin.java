package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.common.entity.Body;
import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public final class EntityMixin {
	@Shadow
	public Level level;

	@Inject(method = "setRemoved", at = @At("RETURN"))
	private void spawnBodyIfKilled(Entity.RemovalReason removalReason, CallbackInfo info) {
		if (WorkshopCore.bodiesEnabled && !this.level.isClientSide && removalReason == Entity.RemovalReason.KILLED && ((Object) (this)) instanceof LivingEntity livingEntity) {
			this.level.addFreshEntity(Body.of(livingEntity));
		}
	}
}
