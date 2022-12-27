package com.syszee.workshopcore.core.screenshake;

import com.mojang.math.Vector3f;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.syszee.workshopcore.core.math.Vector2f;
import com.syszee.workshopcore.core.screenshake.distribution.ShakeDistributor;
import net.minecraft.client.Camera;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class PositionedScreenShakeSource extends DecayingScreenShakeSource {
	public static final Codec<PositionedScreenShakeSource> CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				ShakeDistributor.CODEC.fieldOf("distributor").forGetter(source -> source.shakeDistributor),
				ResourceKey.codec(Registry.DIMENSION_REGISTRY).fieldOf("dimension").forGetter(source -> source.dimension),
				Vec3.CODEC.fieldOf("position").forGetter(source -> new Vec3(source.x, source.y, source.z)),
				Codec.DOUBLE.fieldOf("falloff_distance").forGetter(source -> source.falloffDistance),
				Codec.DOUBLE.fieldOf("max_distance").forGetter(source -> source.maxDistance),
				Codec.FLOAT.fieldOf("x_intensity").forGetter(source -> source.xIntensity),
				Codec.FLOAT.fieldOf("y_intensity").forGetter(source -> source.yIntensity),
				Codec.FLOAT.fieldOf("x_max_build_up").forGetter(source -> source.xMaxBuildup),
				Codec.FLOAT.fieldOf("y_max_build_up").forGetter(source -> source.yMaxBuildup),
				Codec.FLOAT.fieldOf("x_decay").forGetter(source -> source.xDecay),
				Codec.FLOAT.fieldOf("y_decay").forGetter(source -> source.yDecay),
				Codec.INT.fieldOf("duration").forGetter(source -> source.duration)
		).apply(instance, (shakeDistributor, dimension, vec3, falloffDistance, maxDistance, xIntensity, yIntensity, xMaxBuildup, yMaxBuildup, xDecay, yDecay, duration) -> new PositionedScreenShakeSource(shakeDistributor, dimension, vec3.x, vec3.y, vec3.z, falloffDistance, maxDistance, xIntensity, yIntensity, xMaxBuildup, yMaxBuildup, xDecay, yDecay, duration));
	});
	protected ResourceKey<Level> dimension;
	protected double xo, yo, zo;
	protected double x, y, z;
	protected double falloffDistance, maxDistance;

	public PositionedScreenShakeSource(ShakeDistributor shakeDistributor, ResourceKey<Level> dimension, double x, double y, double z, double falloffDistance, double maxDistance, float xIntensity, float yIntensity, float xMaxBuildup, float yMaxBuildup, float xDecay, float yDecay, int duration) {
		super(shakeDistributor, xIntensity, yIntensity, xMaxBuildup, yMaxBuildup, xDecay, yDecay, duration);
		this.dimension = dimension;
		this.x = this.xo = x;
		this.y = this.yo = y;
		this.z = this.zo = z;
		this.falloffDistance = falloffDistance;
		this.maxDistance = maxDistance;
	}

	@Override
	protected Vector2f getShake(Camera camera, float partialTicks) {
		var cameraEntity = camera.getEntity();
		if (cameraEntity == null || !cameraEntity.level.dimension().equals(this.dimension)) return ShakeDistributor.SHAKE_VECTOR_CACHE.set(0.0F, 0.0F);
		double x = Mth.lerp(partialTicks, this.xo, this.x);
		double y = Mth.lerp(partialTicks, this.yo, this.y);
		double z = Mth.lerp(partialTicks, this.zo, this.z);
		Vec3 cameraPos = camera.getPosition();
		double xDif = x - cameraPos.x;
		double yDif = y - cameraPos.y;
		double zDif = z - cameraPos.z;
		double distanceSqr = Mth.square(xDif) + Mth.square(yDif) + Mth.square(zDif);
		double distance = Math.sqrt(distanceSqr);
		if (distance > this.maxDistance) return ShakeDistributor.SHAKE_VECTOR_CACHE.set(0.0F, 0.0F);
		Vector2f shake = super.getShake(camera, partialTicks);
		if (distance > this.falloffDistance) {
			shake.mul(Math.max((float) (1.0D - (distance - this.falloffDistance) / (this.maxDistance - this.falloffDistance)), 0.0F));
		}
		Vector3f cameraLookVector = camera.getLookVector();
		double inverseDistance = 1.0F / distance;
		float viewCloseness = Math.max(0.0F, cameraLookVector.x() * (float) (xDif * inverseDistance) + cameraLookVector.y() * (float) (yDif * inverseDistance) + cameraLookVector.z() * (float) (zDif * inverseDistance));
		shake.mul(1.0F - (1.0F - viewCloseness) * 0.5F);
		return shake;
	}

	@Override
	public boolean shouldRemoveSelf(Camera camera) {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		return super.shouldRemoveSelf(camera);
	}

	@Override
	public Codec<? extends ScreenShakeSource> codec() {
		return CODEC;
	}
}
