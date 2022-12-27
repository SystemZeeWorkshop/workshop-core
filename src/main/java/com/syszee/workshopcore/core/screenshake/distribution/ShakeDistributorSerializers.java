package com.syszee.workshopcore.core.screenshake.distribution;

import com.mojang.serialization.Codec;
import com.syszee.workshopcore.core.WorkshopCore;
import com.syszee.workshopcore.core.registry.util.BasicRegistry;
import net.minecraft.resources.ResourceLocation;

public final class ShakeDistributorSerializers {
	public static final BasicRegistry<Codec<? extends ShakeDistributor>> REGISTRY = new BasicRegistry<>();

	public static void register() {}

	static {
		REGISTRY.register(new ResourceLocation(WorkshopCore.MOD_ID, "jitter"), JitterShakeDistributor.CODEC);
		REGISTRY.register(new ResourceLocation(WorkshopCore.MOD_ID, "irregular_wave"), IrregularWaveShakeDistributor.CODEC);
	}
}
