package com.syszee.workshopcore.client;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;

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
}
