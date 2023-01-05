package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.common.entity.Body;
import com.syszee.workshopcore.core.WCEntity;
import com.syszee.workshopcore.core.WCPlayer;
import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public final class EntityMixin implements WCEntity {
	@Shadow
	public Level level;

	@Nullable
	private WCEntity.PopupInfo popupInfo;

	@Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", ordinal = 0))
	private void saveWCData(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> info) {
		var popupInfo = this.getPopupInfo();
		if (popupInfo != null) {
			CompoundTag popupInfoTag = new CompoundTag();
			popupInfoTag.putString("Title", Component.Serializer.toJson(popupInfo.title()));
			popupInfoTag.putString("Description", Component.Serializer.toJson(popupInfo.description()));
			compoundTag.put("PopupInfo", popupInfoTag);
		}
	}

	@Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", ordinal = 0))
	private void loadWCData(CompoundTag compoundTag, CallbackInfo info) {
		if (compoundTag.get("PopupInfo") instanceof CompoundTag popupInfoTag) {
			this.setPopupInfo(new PopupInfo(Component.Serializer.fromJson(popupInfoTag.getString("Title")), Component.Serializer.fromJson(popupInfoTag.getString("Description"))));
		}
	}

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

	@Override
	public void setPopupInfo(@Nullable PopupInfo popupInfo) {
		this.popupInfo = popupInfo;
	}

	@Nullable
	@Override
	public PopupInfo getPopupInfo() {
		return this.popupInfo;
	}
}
