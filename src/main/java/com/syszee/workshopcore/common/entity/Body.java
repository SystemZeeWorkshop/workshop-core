package com.syszee.workshopcore.common.entity;

import com.syszee.workshopcore.core.registry.WCEntityDataSerializers;
import com.syszee.workshopcore.core.registry.WCEntityDataSerializers.LossyEntityCloningData;
import com.syszee.workshopcore.core.registry.WCEntityTypes;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class Body extends Entity {
	private static final Set<Entity> ALL_CLIENT_BODIES = Collections.newSetFromMap(new WeakHashMap<>());
	private static final EntityDataAccessor<LossyEntityCloningData> BODY_ENTITY_DATA = SynchedEntityData.defineId(Body.class, WCEntityDataSerializers.LOSSY_ENTITY_CLONING_DATA_SERIALIZER);
	@Environment(EnvType.CLIENT)
	@Nullable
	private Entity entity;

	public Body(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	private Body(LivingEntity entity) {
		super(WCEntityTypes.BODY, entity.level());
		this.setPos(entity.position());
		this.setBodyEntityData(new LossyEntityCloningData(entity.level().registryAccess().registryOrThrow(Registries.ENTITY_TYPE).getId(entity.getType()), entity.getUUID(), entity.hasCustomName() || entity instanceof Player ? entity.getName() : Component.empty(), entity.yHeadRot, entity.yBodyRot, entity.getEntityData().getNonDefaultValues()));
	}

	public static Body of(LivingEntity entity) {
		return new Body(entity);
	}

	public static boolean isClientBody(Entity entity) {
		return ALL_CLIENT_BODIES.contains(entity);
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(BODY_ENTITY_DATA, LossyEntityCloningData.DEFAULT);
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
		super.onSyncedDataUpdated(entityDataAccessor);
		if (BODY_ENTITY_DATA.equals(entityDataAccessor) && this.level().isClientSide) {
			Entity entity = this.getBodyEntityData().createClientEntity((ClientLevel) this.level());
			if (entity != null) {
				entity.setPos(this.position());
				ALL_CLIENT_BODIES.add(entity);
			}
			this.entity = entity;
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide) {
			Entity entity = this.entity;
			if (entity != null) {
				entity.xo = entity.getX();
				entity.yo = entity.getY();
				entity.zo = entity.getZ();
				entity.setPos(this.position());
			}
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {
		try {
			this.setBodyEntityData(WCEntityDataSerializers.LOSSY_ENTITY_CLONING_DATA_SERIALIZER.read(new FriendlyByteBuf(Unpooled.wrappedBuffer(compoundTag.getByteArray("BodyData")))));
		} catch (Exception ignored) {}
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {
		FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
		WCEntityDataSerializers.LOSSY_ENTITY_CLONING_DATA_SERIALIZER.write(friendlyByteBuf, this.getBodyEntityData());
		compoundTag.putByteArray("BodyData", friendlyByteBuf.array());
	}

	@Override
	public AABB getBoundingBoxForCulling() {
		return this.entity == null ? super.getBoundingBoxForCulling() : this.entity.getBoundingBoxForCulling();
	}

	private void setBodyEntityData(LossyEntityCloningData data) {
		this.entityData.set(BODY_ENTITY_DATA, data);
	}

	public LossyEntityCloningData getBodyEntityData() {
		return this.entityData.get(BODY_ENTITY_DATA);
	}

	@Environment(EnvType.CLIENT)
	public Entity getEntity() {
		return this.entity;
	}
}
