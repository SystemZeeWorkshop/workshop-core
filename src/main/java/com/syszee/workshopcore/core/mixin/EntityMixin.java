package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.common.entity.Body;
import com.syszee.workshopcore.core.WCPlayer;
import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

	@ModifyVariable(method = "move", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private Vec3 frozenMovement(Vec3 vec3) {
		return ((Object) this) instanceof WCPlayer wcPlayer && wcPlayer.isFrozen() ? Vec3.ZERO : vec3;
	}
}
