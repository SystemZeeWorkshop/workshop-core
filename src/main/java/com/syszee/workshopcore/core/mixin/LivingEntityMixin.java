package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.core.WCPlayer;
import com.syszee.workshopcore.core.registry.WCGameRules;
import com.syszee.workshopcore.core.registry.WCParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	private LivingEntityMixin(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "tickHeadTurn", at = @At("HEAD"), cancellable = true)
	private void cancelTickHeadTurnIfFrozen(float p_21260_, float distanceFromPreviousPos, CallbackInfoReturnable<Float> info) {
		if (this instanceof WCPlayer wcPlayer && wcPlayer.isFrozen()) info.setReturnValue(distanceFromPreviousPos);
	}

	@Inject(method = "die", at = @At("TAIL"))
	private void die(DamageSource damageSource, CallbackInfo info) {
		if (this.level instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(WCGameRules.RULE_DO_DEATH_CONFETTI)) {
			serverLevel.sendParticles(WCParticleTypes.CONFETTI, this.getX(), this.getY(), this.getZ(), this.random.nextInt(90, 120), 0.0D, 0.0D, 0.0D, 1.0D);
		}
	}

}
