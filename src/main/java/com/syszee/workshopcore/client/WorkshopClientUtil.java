package com.syszee.workshopcore.client;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;

import java.util.ArrayList;

public class WorkshopClientUtil {
	private static final ArrayList<Option> ACCESSIBILITY_OPTIONS = new ArrayList<>(1);
	private static final ArrayList<Option> VIDEO_OPTIONS = new ArrayList<>(1);

	public static <T> OptionInstance<T> registerOption(String name, OptionInstance<T> optionInstance, boolean isAccessibility, boolean isVideo) {
		Option option = new Option(name, optionInstance);
		if (isVideo) VIDEO_OPTIONS.add(option);
		if (isAccessibility) ACCESSIBILITY_OPTIONS.add(option);
		return optionInstance;
	}

	public static void processModdedOptions(Options.FieldAccess fieldAccess) {
		ACCESSIBILITY_OPTIONS.forEach(option -> fieldAccess.process(option.name(), option.optionInstance()));
		VIDEO_OPTIONS.forEach(option -> fieldAccess.process(option.name(), option.optionInstance()));
	}

	public static OptionInstance<?>[] getModdedAccessibilityOptions() {
		return ACCESSIBILITY_OPTIONS.stream().map(Option::optionInstance).toArray(OptionInstance[]::new);
	}

	public static OptionInstance<?>[] getModdedVideoOptions() {
		return VIDEO_OPTIONS.stream().map(Option::optionInstance).toArray(OptionInstance[]::new);
	}

	public record Option(String name, OptionInstance<?> optionInstance) {}
}
