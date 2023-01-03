package com.syszee.workshopcore.core.mixin.client;

import com.syszee.workshopcore.core.WCPlayer;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public final class MultiPlayerGameModeMixin {
	@Inject(method = "handleInventoryMouseClick", at = @At("HEAD"), cancellable = true)
	private void cancelHandleInventoryMouseClickIfFrozen(int i, int j, int k, ClickType clickType, Player player, CallbackInfo info) {
		if (((WCPlayer) player).isFrozen()) info.cancel();
	}
}
