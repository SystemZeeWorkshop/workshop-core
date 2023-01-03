package com.syszee.workshopcore.core;

import net.minecraft.network.chat.Component;

public interface WCPlayer {
	Component HEADS_COMPONENT = Component.literal("Heads");
	Component TAILS_COMPONENT = Component.literal("Tails");

	void setFrozen(boolean frozen);

	boolean isFrozen();

	void startSwelling();

	float getSwelling(float partialTicks);

	void setCoinChoice(CoinChoice choice);

	CoinChoice getCoinChoice();

	enum CoinChoice {
		UNDEFINED,
		HEADS,
		TAILS
	}
}
