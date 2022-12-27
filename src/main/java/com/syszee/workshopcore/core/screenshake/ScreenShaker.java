package com.syszee.workshopcore.core.screenshake;

import com.syszee.workshopcore.core.math.Vector2f;
import net.minecraft.client.Camera;

import java.util.ArrayList;

public final class ScreenShaker {
	private static final ArrayList<ScreenShakeSource> SCREEN_SHAKE_SOURCES = new ArrayList<>();
	private static final Vector2f SHAKE_INTENSITY = new Vector2f();

	private ScreenShaker() {}

	public static void addSource(ScreenShakeSource source) {
		SCREEN_SHAKE_SOURCES.add(source);
	}

	public static void tick(Camera camera) {
		SCREEN_SHAKE_SOURCES.removeIf(screenShakeSource -> screenShakeSource.shouldRemoveSelf(camera));
	}

	public static void clear() {
		SCREEN_SHAKE_SOURCES.clear();
	}

	public static Vector2f getShake(Camera camera, float partialTicks) {
		SHAKE_INTENSITY.set(0.0F, 0.0F);
		for (ScreenShakeSource source : SCREEN_SHAKE_SOURCES) source.modifyShake(SHAKE_INTENSITY, camera, partialTicks);
		return SHAKE_INTENSITY;
	}
}
