package com.syszee.workshopcore.core;

import com.syszee.workshopcore.core.networking.WCNetworking;
import com.syszee.workshopcore.core.registry.WCEntityDataSerializers;
import com.syszee.workshopcore.core.registry.WCEntityTypes;
import com.syszee.workshopcore.core.registry.WCGameRules;
import com.syszee.workshopcore.core.registry.WCParticleTypes;
import com.syszee.workshopcore.core.screenshake.ScreenShakeSourceSerializers;
import com.syszee.workshopcore.core.screenshake.distribution.ShakeDistributorSerializers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class WorkshopCore implements ModInitializer {
	public static final String MOD_ID = "workshop_core";
	public static boolean bodiesEnabled = false;
	public static MinecraftServer server;

	@Override
	public void onInitialize() {
		WCEntityDataSerializers.register();
		WCEntityTypes.register();
		WCGameRules.register();
		WCParticleTypes.register();
		ShakeDistributorSerializers.register();
		ScreenShakeSourceSerializers.register();
		WCNetworking.common();
		CommandRegistrationCallback.EVENT.register(WCCommands::registerCommands);

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			WorkshopCore.server = server;
		});
	}
}
