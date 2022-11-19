package com.syszee.workshopcore.core.mixin;

import com.syszee.workshopcore.core.WCPlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements WCPlayer {
	private static final EntityDataAccessor<Boolean> IS_SWELLING = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
	private static final EntityDataAccessor<Byte> COIN_CHOICE = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BYTE);
	private int oldSwell, swell;

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
		super(entityType, level);
	}

	@Inject(method = "defineSynchedData", at = @At("RETURN"))
	private void defineSwellingData(CallbackInfo info) {
		this.entityData.define(IS_SWELLING, false);
		this.entityData.define(COIN_CHOICE, (byte) 0);
	}

	@Inject(method = "tick", at = @At("RETURN"))
	private void tickSwelling(CallbackInfo info) {
		this.oldSwell = this.swell;
		if (this.entityData.get(IS_SWELLING)) {
			if (++this.swell >= 30 && !this.level.isClientSide) {
				Explosion.BlockInteraction blockInteraction = this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
				this.level.explode(this, this.getX(), this.getY(), this.getZ(), 3.0F, blockInteraction);
				this.kill();
				this.entityData.set(IS_SWELLING, false);
			}
		}
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
