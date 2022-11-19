package com.syszee.workshopcore.common.entity;

import com.syszee.workshopcore.core.WCPlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;
import org.jetbrains.annotations.Nullable;

public class Coin extends Entity {
	private static final EntityDataAccessor<Byte> FLIPPING_STATUS = SynchedEntityData.defineId(Coin.class, EntityDataSerializers.BYTE);
	private int flippingTimer;
	@Nullable
	private ServerPlayer heads, tails;
	private final PlayerTeam winners, losers;
	public AnimationState flipAnimationState = new AnimationState();

	public Coin(EntityType<?> entityType, Level level) {
		super(entityType, level);
		if (!level.isClientSide) {
			Scoreboard scoreboard = level.getScoreboard();
			PlayerTeam winners = scoreboard.getPlayerTeam("winners");
			if (winners == null) winners = scoreboard.addPlayerTeam("winners");
			winners.setColor(ChatFormatting.GREEN);
			winners.setNameTagVisibility(Team.Visibility.ALWAYS);
			this.winners = winners;
			PlayerTeam losers = scoreboard.getPlayersTeam("losers");
			if (losers == null) losers = scoreboard.addPlayerTeam("losers");
			losers.setColor(ChatFormatting.RED);
			losers.setNameTagVisibility(Team.Visibility.ALWAYS);
			this.losers = losers;
		} else {
			this.winners = this.losers = null;
		}
		this.blocksBuilding = true;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(FLIPPING_STATUS, (byte) 0);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		super.onSyncedDataUpdated(entityDataAccessor);
		if (entityDataAccessor.equals(FLIPPING_STATUS)) {
			if (this.isFlipping()) {
				this.flipAnimationState.start(this.tickCount);
			}
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (!this.level.isClientSide) {
			if (this.flippingTimer > 0) {
				if (--this.flippingTimer == 0) {
					ServerPlayer winner;
					ServerPlayer loser;
					if (this.isHeads()) {
						winner = this.heads;
						loser = this.tails;
					} else {
						winner = this.tails;
						loser = this.heads;
					}
					Scoreboard scoreboard = this.level.getScoreboard();
					if (winner != null) {
						winner.setGlowingTag(true);
						String winnerName = winner.getScoreboardName();
						scoreboard.addPlayerToTeam(winnerName, this.winners);
					}
					if (loser != null) {
						loser.setGlowingTag(true);
						((WCPlayer) loser).startSwelling();
						loser.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
						String loserName = loser.getScoreboardName();
						scoreboard.addPlayerToTeam(loserName, this.losers);
					}
					this.stopFlipping();
				}
			}
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {}

	@Override
	public boolean canBeCollidedWith() {
		return true;
	}

	@Override
	public boolean isPickable() {
		return true;
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		return super.getBoundingBoxForCulling().inflate(6.0D);
	}

	@Override
	public InteractionResult interactAt(Player player, Vec3 vec3, InteractionHand interactionHand) {
		return this.interact(player, interactionHand);
	}

	@Override
	public InteractionResult interact(Player player, InteractionHand interactionHand) {
		if (!this.isFlipping()) {
			if (player instanceof ServerPlayer serverPlayer && serverPlayer.getServer().getProfilePermissions(serverPlayer.getGameProfile()) >= 2) {
				this.flip();
				Scoreboard scoreboard = this.level.getScoreboard();
				ServerPlayer heads = this.heads;
				if (heads != null) {
					Team team = heads.getTeam();
					if (team == this.winners || team == this.losers) scoreboard.removePlayerFromTeam(heads.getScoreboardName());
				}
				ServerPlayer tails = this.tails;
				if (tails != null) {
					Team team = tails.getTeam();
					if (team == this.winners || team == this.losers) scoreboard.removePlayerFromTeam(tails.getScoreboardName());
				}
				this.flippingTimer = 60;
				return InteractionResult.CONSUME;
			}
			return InteractionResult.SUCCESS;
		}
		return super.interact(player, interactionHand);
	}

	public void assignPlayerAsHeads(ServerPlayer player) {
		((WCPlayer) player).setCoinChoice(WCPlayer.CoinChoice.HEADS);
		this.heads = player;
		if (player == this.tails) this.tails = null;
	}

	public void assignPlayerAsTails(ServerPlayer player) {
		((WCPlayer) player).setCoinChoice(WCPlayer.CoinChoice.TAILS);
		this.tails = player;
		if (player == this.heads) this.heads = null;
	}

	public void reset() {
		ServerPlayer heads = this.heads;
		if (heads != null) {
			((WCPlayer) heads).setCoinChoice(WCPlayer.CoinChoice.UNDEFINED);
		}
		ServerPlayer tails = this.tails;
		if (tails != null) {
			((WCPlayer) tails).setCoinChoice(WCPlayer.CoinChoice.UNDEFINED);
		}
		this.heads = null;
		this.tails = null;
	}

	public boolean isHeads() {
		return this.entityData.get(FLIPPING_STATUS) % 2 == 0;
	}

	public boolean isFlipping() {
		return this.entityData.get(FLIPPING_STATUS) > 1;
	}

	public void flip() {
		this.entityData.set(FLIPPING_STATUS, this.random.nextBoolean() ? (byte) 2 : 3);
	}

	public void stopFlipping() {
		this.entityData.set(FLIPPING_STATUS, (byte) Math.max(0, this.entityData.get(FLIPPING_STATUS) - 2));
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
