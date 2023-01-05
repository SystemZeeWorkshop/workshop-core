package com.syszee.workshopcore.core;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public interface WCEntity {
	void setPopupInfo(@Nullable PopupInfo popupInfo);

	@Nullable
	PopupInfo getPopupInfo();

	record PopupInfo(Component title, Component description) {}
}
