package com.syszee.workshopcore.core.screenshake;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.syszee.workshopcore.core.math.Vector2f;
import net.minecraft.client.Camera;

import java.util.List;
import java.util.function.Function;

public interface ScreenShakeSource {
	Codec<ScreenShakeSource> CODEC = ScreenShakeSourceSerializers.REGISTRY.dispatchStable(ScreenShakeSource::codec, Function.identity());
	// FriendlyByteBuf can only serialize Compound NBT for NBT serialization
	Codec<List<ScreenShakeSource>> NETWORK_CODEC = RecordCodecBuilder.create(instance -> {
		return instance.group(
				CODEC.listOf().fieldOf("sources").forGetter(sources -> sources)
		).apply(instance, sources -> sources);
	});

	void modifyShake(Vector2f currentShake, Camera camera, float partialTicks);

	boolean shouldRemoveSelf(Camera camera);

	Codec<? extends ScreenShakeSource> codec();
}
