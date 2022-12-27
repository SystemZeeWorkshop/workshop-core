package com.syszee.workshopcore.core.screenshake.distribution;

import com.mojang.serialization.Codec;
import com.syszee.workshopcore.core.math.Vector2f;
import net.minecraft.client.Camera;

import java.util.function.Function;

public interface ShakeDistributor {
	Codec<ShakeDistributor> CODEC = ShakeDistributorSerializers.REGISTRY.dispatchStable(ShakeDistributor::codec, Function.identity());
	Vector2f SHAKE_VECTOR_CACHE = new Vector2f();

	Vector2f computeShake(Camera camera, float xIntensity, float yIntensity, float partialTicks);

	Codec<? extends ShakeDistributor> codec();
}
