package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.core.WCServerPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public final class ServerPlayerMixin implements WCServerPlayer {
	private boolean explosivePunchEnabled;

	@Inject(method = "restoreFrom", at = @At("RETURN"))
	private void restoreWCDataFrom(ServerPlayer oldPlayer, boolean bl, CallbackInfo info) {
		this.explosivePunchEnabled = ((WCServerPlayer) oldPlayer).isExplosivePunchEnabled();
	}

	@Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
	private void addWCSaveData(CompoundTag compoundTag, CallbackInfo info) {
		compoundTag.putBoolean("ExplosivePunchEnabled", this.explosivePunchEnabled);
	}

	@Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
	private void readWCSaveData(CompoundTag compoundTag, CallbackInfo info) {
		this.explosivePunchEnabled = compoundTag.getBoolean("ExplosivePunchEnabled");
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
