package com.syszee.workshopcore.core.networking;

import com.syszee.workshopcore.common.entity.EntityPopup;
import com.syszee.workshopcore.core.WorkshopCore;
import com.syszee.workshopcore.core.WorkshopCoreClient;
import com.syszee.workshopcore.core.screenshake.ScreenShakeSource;
import com.syszee.workshopcore.core.screenshake.ScreenShaker;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings("deprecation")
public final class WCNetworking {
	public static final ResourceLocation ADD_SCREEN_SHAKE_SOURCES_CHANNEL = new ResourceLocation(WorkshopCore.MOD_ID, "add_screen_shake_sources");
	public static final ResourceLocation UPDATE_ENTITY_POPUP_CHANNEL = new ResourceLocation(WorkshopCore.MOD_ID, "update_entity_popup");

	public static void client() {
		ClientPlayNetworking.registerGlobalReceiver(ADD_SCREEN_SHAKE_SOURCES_CHANNEL, (client, handler, buf, responseSender) -> {
			var screenShakeSources = buf.readWithCodec(ScreenShakeSource.NETWORK_CODEC);
			client.execute(() -> {
				for (ScreenShakeSource source : screenShakeSources) ScreenShaker.addSource(source);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(UPDATE_ENTITY_POPUP_CHANNEL, (client, handler, buf, responseSender) -> {
			if (!buf.readBoolean()) {
				WorkshopCoreClient.entityPopup = null;
				return;
			}
			int entityID = buf.readInt();
			Component title = buf.readComponent();
			Component description = buf.readComponent();
			client.execute(() -> {
				ClientLevel clientLevel = Minecraft.getInstance().level;
				if (clientLevel != null) {
					Entity entity = clientLevel.getEntity(entityID);
					if (entity != null) WorkshopCoreClient.entityPopup = new EntityPopup(entity, title, description);
				}
			});
		});
	}

	public static void common() {}

	public static Packet<?> createS2CScreenShakePacket(ScreenShakeSource... sources) {
		FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
		friendlyByteBuf.writeWithCodec(ScreenShakeSource.NETWORK_CODEC, List.of(sources));
		return ServerPlayNetworking.createS2CPacket(ADD_SCREEN_SHAKE_SOURCES_CHANNEL, friendlyByteBuf);
	}

	public static Packet<?> createS2CUpdateEntityPopup(@Nullable EntityPopup entityPopup) {
		FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
		if (entityPopup == null) {
			friendlyByteBuf.writeBoolean(false);
		} else {
			friendlyByteBuf.writeBoolean(true);
			friendlyByteBuf.writeInt(entityPopup.entity().getId());
			friendlyByteBuf.writeComponent(entityPopup.title());
			friendlyByteBuf.writeComponent(entityPopup.description());
		}
		return ServerPlayNetworking.createS2CPacket(UPDATE_ENTITY_POPUP_CHANNEL, friendlyByteBuf);
	}
}
