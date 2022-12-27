package com.syszee.workshopcore.core.screenshake;

import com.mojang.serialization.Codec;
import com.syszee.workshopcore.core.WorkshopCore;
import com.syszee.workshopcore.core.registry.util.BasicRegistry;
import net.minecraft.resources.ResourceLocation;

public final class ScreenShakeSourceSerializers {
	public static final BasicRegistry<Codec<? extends ScreenShakeSource>> REGISTRY = new BasicRegistry<>();

	public static void register() {}

	static {
		REGISTRY.register(new ResourceLocation(WorkshopCore.MOD_ID, "decaying"), DecayingScreenShakeSource.CODEC);
		REGISTRY.register(new ResourceLocation(WorkshopCore.MOD_ID, "positioned"), PositionedScreenShakeSource.CODEC);
	}
}
