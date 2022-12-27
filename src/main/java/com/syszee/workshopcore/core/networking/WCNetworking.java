package com.syszee.workshopcore.core.networking;

import com.syszee.workshopcore.core.WorkshopCore;
import com.syszee.workshopcore.core.screenshake.ScreenShakeSource;
import com.syszee.workshopcore.core.screenshake.ScreenShaker;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@SuppressWarnings("deprecation")
public final class WCNetworking {
	public static final ResourceLocation ADD_SCREEN_SHAKE_SOURCES_CHANNEL = new ResourceLocation(WorkshopCore.MOD_ID, "add_screen_shake_sources");

	public static void client() {
		ClientPlayNetworking.registerGlobalReceiver(ADD_SCREEN_SHAKE_SOURCES_CHANNEL, (client, handler, buf, responseSender) -> {
			var screenShakeSources = buf.readWithCodec(ScreenShakeSource.NETWORK_CODEC);
			client.execute(() -> {
				for (ScreenShakeSource source : screenShakeSources) ScreenShaker.addSource(source);
			});
		});
	}

	public static void common() {}

	public static Packet<?> createS2CScreenShakePacket(ScreenShakeSource... sources) {
		FriendlyByteBuf friendlyByteBuf = new FriendlyByteBuf(Unpooled.buffer());
		friendlyByteBuf.writeWithCodec(ScreenShakeSource.NETWORK_CODEC, List.of(sources));
		return ServerPlayNetworking.createS2CPacket(ADD_SCREEN_SHAKE_SOURCES_CHANNEL, friendlyByteBuf);
	}
}
