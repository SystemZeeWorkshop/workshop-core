package com.syszee.workshopcore.core;

import com.syszee.workshopcore.core.networking.WCNetworking;
import com.syszee.workshopcore.core.registry.WCEntityDataSerializers;
import com.syszee.workshopcore.core.registry.WCEntityTypes;
import com.syszee.workshopcore.core.screenshake.ScreenShakeSourceSerializers;
import com.syszee.workshopcore.core.screenshake.distribution.ShakeDistributorSerializers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class WorkshopCore implements ModInitializer {
	public static final String MOD_ID = "workshop_core";
	public static boolean bodiesEnabled = false;

	@Override
	public void onInitialize() {
		WCEntityDataSerializers.register();
		WCEntityTypes.register();
		ShakeDistributorSerializers.register();
		ScreenShakeSourceSerializers.register();
		WCNetworking.common();
		CommandRegistrationCallback.EVENT.register(WCCommands::registerCommands);
	}
}
