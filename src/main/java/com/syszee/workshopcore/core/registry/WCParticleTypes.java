package com.syszee.workshopcore.core.registry;

import com.syszee.workshopcore.client.particle.ConfettiParticle;
import com.syszee.workshopcore.client.particle.PlayerParticle;
import com.syszee.workshopcore.common.particles.PlayerParticleOptions;
import com.syszee.workshopcore.core.WorkshopCore;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

public final class WCParticleTypes {
	public static final ParticleType<PlayerParticleOptions> PLAYER = register("player", FabricParticleTypes.complex(PlayerParticleOptions.DESERIALIZER));
	public static final SimpleParticleType CONFETTI = register("confetti", FabricParticleTypes.simple());

	public static <P extends ParticleOptions, T extends ParticleType<P>> T register(String name, T type) {
		return Registry.register(Registry.PARTICLE_TYPE, new ResourceLocation(WorkshopCore.MOD_ID, name), type);
	}

	public static void register() {}

	@Environment(EnvType.CLIENT)
	public static void registerFactories() {
		ParticleFactoryRegistry.getInstance().register(PLAYER, new PlayerParticle.Provider());
		ParticleFactoryRegistry.getInstance().register(CONFETTI, ConfettiParticle.Provider::new);
	}
}
