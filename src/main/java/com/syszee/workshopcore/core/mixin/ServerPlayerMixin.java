package com.syszee.workshopcore.core.mixin;

import com.mojang.authlib.GameProfile;
import com.syszee.workshopcore.core.WCPlayer;
import com.syszee.workshopcore.core.WCServerPlayer;
import com.syszee.workshopcore.core.registry.WCGameRules;
import com.syszee.workshopcore.core.registry.WCParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements WCServerPlayer {
	private boolean explosivePunchEnabled;

	private ServerPlayerMixin(MinecraftServer minecraftServer, ServerLevel serverLevel, GameProfile gameProfile) {
		super(serverLevel, serverLevel.getSharedSpawnPos(), serverLevel.getSharedSpawnAngle(), gameProfile);
	}

	@Inject(method = "restoreFrom", at = @At("RETURN"))
	private void restoreWCDataFrom(ServerPlayer oldPlayer, boolean bl, CallbackInfo info) {
		this.explosivePunchEnabled = ((WCServerPlayer) oldPlayer).isExplosivePunchEnabled();
		((WCPlayer) (Object) this).setFrozen(((WCPlayer) oldPlayer).isFrozen());
	}

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	private void addWCSaveData(CompoundTag compoundTag, CallbackInfo info) {
		compoundTag.putBoolean("ExplosivePunchEnabled", this.explosivePunchEnabled);
		compoundTag.putBoolean("IsFrozen", ((WCPlayer) (Object) this).isFrozen());
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void readWCSaveData(CompoundTag compoundTag, CallbackInfo info) {
		this.explosivePunchEnabled = compoundTag.getBoolean("ExplosivePunchEnabled");
		((WCPlayer) (Object) this).setFrozen(compoundTag.getBoolean("IsFrozen"));
	}

	@Inject(method = "die", at = @At("TAIL"))
	private void die(DamageSource damageSource, CallbackInfo info) {
		if (this.level instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(WCGameRules.RULE_DO_DEATH_CONFETTI)) {
			serverLevel.sendParticles(WCParticleTypes.CONFETTI, this.getX(), this.getY(), this.getZ(), this.random.nextInt(90, 120), 0.0D, 0.0D, 0.0D, 1.0D);
		}
	}

	@Override
	public void enableExplosivePunch(boolean enable) {
		this.explosivePunchEnabled = enable;
	}

	@Override
	public boolean isExplosivePunchEnabled() {
		return this.explosivePunchEnabled;
	}
}
