package com.syszee.workshopcore.core.screenshake.distribution;

import com.mojang.serialization.Codec;
import net.minecraft.client.Camera;
import org.joml.Vector2f;

import java.util.Random;

public enum JitterShakeDistributor implements ShakeDistributor {
	INSTANCE;

	public static final Codec<JitterShakeDistributor> CODEC = Codec.unit(INSTANCE);
	private static final Random RANDOM = new Random();

	@Override
	public Vector2f computeShake(Camera camera, float xIntensity, float yIntensity, float partialTicks) {
		SHAKE_VECTOR_CACHE.set(randomizeIntensity(xIntensity), randomizeIntensity(yIntensity));
		return SHAKE_VECTOR_CACHE;
	}

	@Override
	public Codec<? extends ShakeDistributor> codec() {
		return CODEC;
	}

	private static float randomizeIntensity(float intensity) {
		float randomFloat = RANDOM.nextFloat();
		return (1.0F - randomFloat * randomFloat) * (RANDOM.nextInt(2) - 0.5F) * intensity * 2.0F;
	}
}
