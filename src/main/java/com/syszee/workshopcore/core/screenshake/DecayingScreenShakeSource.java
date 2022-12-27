package com.syszee.workshopcore.core.screenshake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.syszee.workshopcore.core.math.Vector2f;
import com.syszee.workshopcore.core.screenshake.distribution.JitterShakeDistributor;
import com.syszee.workshopcore.core.screenshake.distribution.ShakeDistributor;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;

// Fields are protected and mutable in case of future use
public class DecayingScreenShakeSource implements ScreenShakeSource {
	public static final Codec<DecayingScreenShakeSource> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				ShakeDistributor.CODEC.fieldOf("distributor").forGetter(source -> source.shakeDistributor),
				Codec.FLOAT.fieldOf("x_intensity").forGetter(source -> source.xIntensity),
				Codec.FLOAT.fieldOf("y_intensity").forGetter(source -> source.yIntensity),
				Codec.FLOAT.fieldOf("x_max_build_up").forGetter(source -> source.xMaxBuildup),
				Codec.FLOAT.fieldOf("y_max_build_up").forGetter(source -> source.yMaxBuildup),
				Codec.FLOAT.fieldOf("x_decay").forGetter(source -> source.xDecay),
				Codec.FLOAT.fieldOf("y_decay").forGetter(source -> source.yDecay),
				Codec.INT.fieldOf("duration").forGetter(source -> source.duration)
		).apply(instance, DecayingScreenShakeSource::new);
	});
	protected ShakeDistributor shakeDistributor;
	protected float xIntensityO, yIntensityO;
	protected float xIntensity, yIntensity;
	protected float xMaxBuildup, yMaxBuildup;
	protected float xDecay, yDecay;
	protected int age;
	protected int duration;

	public DecayingScreenShakeSource(ShakeDistributor shakeDistributor, float xIntensity, float yIntensity, float xMaxBuildup, float yMaxBuildup, float xDecay, float yDecay, int duration) {
		this.shakeDistributor = shakeDistributor;
		this.xIntensity = this.xIntensityO = xIntensity;
		this.yIntensity = this.yIntensityO = yIntensity;
		this.xMaxBuildup = Mth.abs(xMaxBuildup);
		this.yMaxBuildup = Mth.abs(yMaxBuildup);
		this.xDecay = xDecay;
		this.yDecay = yDecay;
		this.duration = duration;
	}

	public DecayingScreenShakeSource(float xIntensity, float yIntensity, float xMaxBuildup, float yMaxBuildup, float xDecay, float yDecay, int duration) {
		this(JitterShakeDistributor.INSTANCE, xIntensity, yIntensity, xMaxBuildup, yMaxBuildup, xDecay, yDecay, duration);
	}

	public DecayingScreenShakeSource(float intensity, float maxBuildup, float decay, int duration) {
		this(intensity, intensity, maxBuildup, maxBuildup, decay, decay, duration);
	}

	protected Vector2f getShake(Camera camera, float partialTicks) {
		return this.shakeDistributor.computeShake(camera, Mth.lerp(partialTicks, this.xIntensityO, this.xIntensity), Mth.lerp(partialTicks, this.yIntensityO, this.yIntensity), partialTicks);
	}

	@Override
	public void modifyShake(Vector2f currentShake, Camera camera, float partialTicks) {
		Vector2f shake = this.getShake(camera, partialTicks);
		float xCurrentShake = currentShake.x();
		float xNewShake = xCurrentShake + shake.x();
		if (Mth.abs(xNewShake) >= this.xMaxBuildup && Mth.abs(xCurrentShake) < this.xMaxBuildup) {
			xNewShake = Math.signum(xNewShake) * this.xMaxBuildup;
		}
		float yCurrentShake = currentShake.y();
		float yNewShake = yCurrentShake + shake.y();
		if (Mth.abs(yNewShake) >= this.yMaxBuildup && Mth.abs(yCurrentShake) < this.yMaxBuildup) {
			yNewShake = Math.signum(yNewShake) * this.yMaxBuildup;
		}
		currentShake.set(xNewShake, yNewShake);
	}

	@Override
	public boolean shouldRemoveSelf(Camera camera) {
		this.xIntensityO = this.xIntensity;
		this.yIntensityO = this.yIntensity;
		this.xIntensity *= this.xDecay;
		this.yIntensity *= this.yDecay;
		return ++this.age >= this.duration;
	}

	@Override
	public Codec<? extends ScreenShakeSource> codec() {
		return CODEC;
	}
}
