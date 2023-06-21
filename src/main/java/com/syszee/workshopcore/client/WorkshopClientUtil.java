package com.syszee.workshopcore.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.joml.Matrix4f;

import java.util.ArrayList;

public class WorkshopClientUtil {
	private static final ArrayList<Pair<String, OptionInstance<?>>> ACCESSIBILITY_OPTIONS = new ArrayList<>(1);

	public static <T> OptionInstance<T> registerAccessibilityOption(String name, OptionInstance<T> optionInstance) {
		ACCESSIBILITY_OPTIONS.add(Pair.of(name, optionInstance));
		return optionInstance;
	}

	public static void processModdedOptions(Options.FieldAccess fieldAccess) {
		ACCESSIBILITY_OPTIONS.forEach(option -> fieldAccess.process(option.getFirst(), option.getSecond()));
	}

	public static OptionInstance<?>[] getModdedAccessibilityOptions() {
		return ACCESSIBILITY_OPTIONS.stream().map(Pair::getSecond).toArray(OptionInstance[]::new);
	}

	public static void fillGradient(Matrix4f matrix4f, BufferBuilder bufferBuilder, int x1, int y1, int x2, int y2, int z, int startColor, int endColor) {
		float f = (float)(startColor >> 24 & 255) / 255.0F;
		float g = (float)(startColor >> 16 & 255) / 255.0F;
		float h = (float)(startColor >> 8 & 255) / 255.0F;
		float p = (float)(startColor & 255) / 255.0F;
		float q = (float)(endColor >> 24 & 255) / 255.0F;
		float r = (float)(endColor >> 16 & 255) / 255.0F;
		float s = (float)(endColor >> 8 & 255) / 255.0F;
		float t = (float)(endColor & 255) / 255.0F;
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y1, (float)z).color(g, h, p, f).endVertex();
		bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z).color(g, h, p, f).endVertex();
		bufferBuilder.vertex(matrix4f, (float)x1, (float)y2, (float)z).color(r, s, t, q).endVertex();
		bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z).color(r, s, t, q).endVertex();
	}
}
