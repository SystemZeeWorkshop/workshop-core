package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.common.entity.EntityPopup;
import com.syszee.workshopcore.core.*;
import com.syszee.workshopcore.core.networking.WCNetworking;
import com.syszee.workshopcore.core.registry.WCGameRules;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements WCPlayer {
	private static final EntityDataAccessor<Boolean> FROZEN = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Boolean> IS_SWELLING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> COIN_CHOICE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);
	private int oldSwell, swell;

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "defineSynchedData", at = @At("RETURN"))
	private void defineSwellingData(CallbackInfo info) {
		this.entityData.define(FROZEN, false);
		this.entityData.define(IS_SWELLING, false);
		this.entityData.define(COIN_CHOICE, (byte) 0);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void tick(CallbackInfo info) {
		this.oldSwell = this.swell;
		if (this.entityData.get(IS_SWELLING)) {
			if (++this.swell >= 30 && !this.level().isClientSide) {
				Level.ExplosionInteraction blockInteraction = this.level().getGameRules().getBoolean(WCGameRules.RULE_SWELL_GRIEFING) ? Level.ExplosionInteraction.MOB : Level.ExplosionInteraction.NONE;
				this.level().explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, blockInteraction);
				this.kill();
				this.entityData.set(IS_SWELLING, false);
			}
		}
	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;setLastHurtMob(Lnet/minecraft/world/entity/Entity;)V", ordinal = 0))
	private void addSwellToAttackedEntity(Entity entity, CallbackInfo info) {
		if (this instanceof WCServerPlayer wcServerPlayer && wcServerPlayer.isExplosivePunchEnabled() && entity instanceof WCPlayer target && this.getMainHandItem().isEmpty()) {
			target.startSwelling();
		}
	}

	@Inject(method = "interactOn", at = @At("HEAD"), cancellable = true)
	private void interactOn(Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> info) {
		if (this.isFrozen()) info.setReturnValue(InteractionResult.FAIL);
		if (this.level() instanceof ServerLevel serverLevel && serverLevel.getGameRules().getBoolean(WCGameRules.RULE_SHOW_INFO_POPUPS)) {
			WCEntity.PopupInfo popupInfo = ((WCEntity) entity).getPopupInfo();
			if (popupInfo != null && (Object) this instanceof ServerPlayer serverPlayer) {
				serverPlayer.connection.send(WCNetworking.createS2CUpdateEntityPopup(new EntityPopup(entity, popupInfo.title(), popupInfo.description())));
			}
		}
	}

	@Inject(method = "attack", at = @At("HEAD"), cancellable = true)
	private void preventAttackWhenFrozen(Entity entity, CallbackInfo info) {
		if (this.isFrozen()) info.cancel();
	}

	@Inject(method = "getDestroySpeed", at = @At("HEAD"), cancellable = true)
	private void haltDestroySpeedWhenFrozen(BlockState blockState, CallbackInfoReturnable<Float> info) {
		if (this.isFrozen()) info.setReturnValue(0.0F);
	}

	@Inject(method = "wantsToStopRiding", at = @At("HEAD"), cancellable = true)
	private void preventDismountWhenFrozen(CallbackInfoReturnable<Boolean> info) {
		if (this.isFrozen()) info.setReturnValue(false);
	}

	@Override
	public void setFrozen(boolean frozen) {
		this.entityData.set(FROZEN, frozen);
	}

	@Override
	public boolean isFrozen() {
		return this.entityData.get(FROZEN);
	}

	@Override
	public void startSwelling() {
		this.entityData.set(IS_SWELLING, true);
	}

	@Override
	public float getSwelling(float partialTicks) {
		return Mth.lerp(partialTicks, this.oldSwell, this.swell) / 30.0F;
	}

	@Override
	public void setCoinChoice(CoinChoice choice) {
		this.entityData.set(COIN_CHOICE, (byte) choice.ordinal());
	}

	@Override
	public CoinChoice getCoinChoice() {
		return switch (this.entityData.get(COIN_CHOICE)) {
			default -> CoinChoice.UNDEFINED;
			case 1 -> CoinChoice.HEADS;
			case 2 -> CoinChoice.TAILS;
		};
	}
}
