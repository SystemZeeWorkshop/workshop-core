package com.syszee.workshopcore.core.registry;

import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.world.level.GameRules;

public final class WCGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> RULE_SWELL_GRIEFING = GameRules.register(WorkshopCore.MOD_ID + ".swellGriefing", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));
	public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_DEATH_CONFETTI = GameRules.register(WorkshopCore.MOD_ID + ".doDeathConfetti", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));

	public static void register() {}
}
