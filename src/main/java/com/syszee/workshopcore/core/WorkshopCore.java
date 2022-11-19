package com.syszee.workshopcore.core;

import com.syszee.workshopcore.core.registry.WCEntityTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class WorkshopCore implements ModInitializer {
	public static final String MOD_ID = "workshop_core";

	@Override
	public void onInitialize() {
		WCEntityTypes.register();
		CommandRegistrationCallback.EVENT.register(WCCommands::registerCommands);
	}
}
