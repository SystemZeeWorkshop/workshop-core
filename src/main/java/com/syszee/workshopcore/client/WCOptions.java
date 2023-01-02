package com.syszee.workshopcore.client;

import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.client.OptionInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import static net.minecraft.client.Options.genericValueLabel;

public final class WCOptions {
	private static final Component TOOLTIP_SCREEN_SCREEN_SHAKE = Component.translatable(WorkshopCore.MOD_ID + ".options.screenShakeScale.tooltip");
	public static final OptionInstance<Double> SCREEN_SHAKE_SCALE = WorkshopClientUtil.registerOption(WorkshopCore.MOD_ID + ".screenShakeScale", new OptionInstance<>(WorkshopCore.MOD_ID + ".options.screenShakeScale", OptionInstance.cachedConstantTooltip(TOOLTIP_SCREEN_SCREEN_SHAKE), (component, percent) -> {
		return percent == 0.0D ? genericValueLabel(component, CommonComponents.OPTION_OFF) : Component.translatable("options.percent_value", component, (int)(percent * 100.0D));
	}, OptionInstance.UnitDouble.INSTANCE, 1.0D, percent -> {}), true, true);

	public static void register() {}
}
