package com.syszee.workshopcore.core.registry;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class WCEntityDataSerializers {
	public static final EntityDataSerializer<LossyEntityCloningData> LOSSY_ENTITY_CLONING_DATA_SERIALIZER = new EntityDataSerializer<>() {
		@Override
		public void write(FriendlyByteBuf friendlyByteBuf, LossyEntityCloningData lossyEntityCloneData) {
			friendlyByteBuf.writeInt(lossyEntityCloneData.entityTypeID);
			friendlyByteBuf.writeUUID(lossyEntityCloneData.uuid);
			friendlyByteBuf.writeComponent(lossyEntityCloneData.name);
			friendlyByteBuf.writeFloat(lossyEntityCloneData.yHeadRot);
			friendlyByteBuf.writeFloat(lossyEntityCloneData.yBodyRot);
			for (SynchedEntityData.DataValue<?> dataValue : lossyEntityCloneData.dataValues) {
				dataValue.write(friendlyByteBuf);
			}
			friendlyByteBuf.writeByte(255);
		}

		@Override
		public LossyEntityCloningData read(FriendlyByteBuf friendlyByteBuf) {
			int entityTypeID = friendlyByteBuf.readInt();
			UUID uuid = friendlyByteBuf.readUUID();
			Component name = friendlyByteBuf.readComponent();
			float yHeadRot = friendlyByteBuf.readFloat();
			float yBodyRot = friendlyByteBuf.readFloat();
			ArrayList<SynchedEntityData.DataValue<?>> dataValues = new ArrayList<>();
			short i;
			while ((i = friendlyByteBuf.readUnsignedByte()) != 255) {
				dataValues.add(SynchedEntityData.DataValue.read(friendlyByteBuf, i));
			}
			return new LossyEntityCloningData(entityTypeID, uuid, name, yHeadRot, yBodyRot, dataValues);
		}

		@Override
		public LossyEntityCloningData copy(LossyEntityCloningData lossyEntityCloneData) {
			return new LossyEntityCloningData(lossyEntityCloneData.entityTypeID, lossyEntityCloneData.uuid, lossyEntityCloneData.name, lossyEntityCloneData.yHeadRot, lossyEntityCloneData.yBodyRot, lossyEntityCloneData.dataValues);
		}
	};

	public static void register() {
		EntityDataSerializers.registerSerializer(LOSSY_ENTITY_CLONING_DATA_SERIALIZER);
	}

	public record LossyEntityCloningData(int entityTypeID, UUID uuid, Component name, float yHeadRot, float yBodyRot, List<SynchedEntityData.DataValue<?>> dataValues) {
		public static final LossyEntityCloningData DEFAULT = new LossyEntityCloningData(0, UUID.randomUUID(), Component.empty(), 0.0F, 0.0F, List.of());

		@Nullable
		@Environment(EnvType.CLIENT)
		public Entity createClientEntity(ClientLevel level) {
			int id = this.entityTypeID();
			UUID uuid = this.uuid();
			Component name = this.name();
			EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.byId(id);
			Entity entity;
			if (entityType == EntityType.PLAYER) {
				entity = new RemotePlayer(level, new GameProfile(uuid, name.getString()));
			} else {
				entity = entityType.create(level);
			}
			if (entity != null) {
				entity.setUUID(uuid);
				entity.setYHeadRot(this.yHeadRot());
				entity.setYBodyRot(this.yBodyRot());
				entity.getEntityData().assignValues(this.dataValues);
				if (!name.getString().isEmpty()) entity.setCustomName(name);
				if (entity instanceof LivingEntity livingEntity) {
					livingEntity.yBodyRotO = livingEntity.yBodyRot;
					livingEntity.xRotO = livingEntity.getXRot();
					livingEntity.yHeadRotO = livingEntity.yHeadRot;
					livingEntity.deathTime = 20;
					livingEntity.setHealth(0.0F);
				}
			}
			return entity;
		}
	}
}
