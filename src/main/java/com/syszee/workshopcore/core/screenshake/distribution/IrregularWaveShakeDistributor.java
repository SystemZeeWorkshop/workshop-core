package com.syszee.workshopcore.core.screenshake.distribution;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

public record IrregularWaveShakeDistributor(float strongFrequency, float firstMinorFrequency, float secondMinorFrequency, int xShift, int yShift) implements ShakeDistributor {
	public static final Codec<IrregularWaveShakeDistributor> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				Codec.FLOAT.fieldOf("strong_frequency").forGetter(IrregularWaveShakeDistributor::strongFrequency),
				Codec.FLOAT.fieldOf("first_minor_frequency").forGetter(IrregularWaveShakeDistributor::firstMinorFrequency),
				Codec.FLOAT.fieldOf("second_minor_frequency").forGetter(IrregularWaveShakeDistributor::secondMinorFrequency),
				Codec.INT.fieldOf("x_shift").forGetter(IrregularWaveShakeDistributor::xShift),
				Codec.INT.fieldOf("y_shift").forGetter(IrregularWaveShakeDistributor::yShift)
		).apply(instance, IrregularWaveShakeDistributor::new);
	});

	@Override
	public Vector2f computeShake(Camera camera, float xIntensity, float yIntensity, float partialTicks) {
		float time = camera.getEntity() != null ? (camera.getEntity().tickCount + partialTicks) * 0.85F : 0;
		float xTime = time + this.xShift;
		float xShake = 0.25F * (-3.0F * Mth.sin(this.strongFrequency * xTime) - Mth.sin(this.firstMinorFrequency * xTime) + Mth.sin(this.secondMinorFrequency * xTime));
		float yTime = time + this.yShift;
		float yShake = 0.25F * (-3.0F * Mth.cos(this.strongFrequency * yTime) - Mth.sin(this.firstMinorFrequency * yTime) + Mth.sin(this.secondMinorFrequency * yTime));
		SHAKE_VECTOR_CACHE.set(xIntensity * xShake, yIntensity * yShake);
		return SHAKE_VECTOR_CACHE;
	}

	@Override
	public Codec<? extends ShakeDistributor> codec() {
		return CODEC;
	}
}
