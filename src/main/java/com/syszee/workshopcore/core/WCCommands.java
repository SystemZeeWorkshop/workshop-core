package com.syszee.workshopcore.core;

import com.mojang.brigadier.CommandDispatcher;
import com.syszee.workshopcore.common.entity.Body;
import com.syszee.workshopcore.common.entity.Coin;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;

import static net.minecraft.commands.Commands.literal;

public final class WCCommands {

	public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
		dispatcher.register(
				literal("coinflip").requires(commandSourceStack -> {
					return commandSourceStack.hasPermission(2);
				}).then(
					literal("heads")
							.then(
									Commands.argument("player", EntityArgument.player())
											.executes(context -> {
												return performActionOnNearestCoin(context.getSource(), EntityArgument.getPlayer(context, "player"), Coin::assignPlayerAsHeads);
											})
							)
				).then(
					literal("tails")
							.then(
									Commands.argument("player", EntityArgument.player())
											.executes(context -> {
												return performActionOnNearestCoin(context.getSource(), EntityArgument.getPlayer(context, "player"), Coin::assignPlayerAsTails);
											})
							)
				).then(
					literal("reset").executes(context -> {
						return performActionOnNearestCoin(context.getSource(), null, (coin, player) -> coin.reset());
					})
				)
		);
		dispatcher.register(
				literal("bodies").requires(commandSourceStack -> commandSourceStack.hasPermission(2))
						.then(
								literal("on").executes(context -> {
									WorkshopCore.bodiesEnabled = true;
									context.getSource().sendSuccess(Component.literal("Enabled bodies"), true);
									return 1;
								})
						)
						.then(
								literal("off").executes(context -> {
									WorkshopCore.bodiesEnabled = false;
									context.getSource().sendSuccess(Component.literal("Disabled bodies"), true);
									return 1;
								})
						)
						.then(
								literal("clear").executes(context -> {
									CommandSourceStack sourceStack = context.getSource();
									int count = 0;
									for (Entity entity : sourceStack.getLevel().getAllEntities()) {
										if (entity instanceof Body) {
											entity.kill();
											count++;
										}
									}
									sourceStack.sendSuccess(Component.literal("Removed " + count + " bodies"), true);
									return count;
								})
						)
		);
	}

	private static int performActionOnNearestCoin(CommandSourceStack sourceStack, ServerPlayer player, BiConsumer<Coin, ServerPlayer> consumer) {
		ServerLevel level = sourceStack.getLevel();
		ServerPlayer sender = sourceStack.getPlayer();
		if (sender == null) return 0;
		List<Coin> coins = level.getEntitiesOfClass(Coin.class, sender.getBoundingBox().inflate(32.0D, 16.0D, 32.0D));
		double smallestDistanceSqr = Double.MAX_VALUE;
		Vec3 playerPosition = sender.position();
		Coin nearestCoin = null;
		for (Coin coin : coins) {
			double distanceSqr = coin.position().distanceToSqr(playerPosition);
			if (distanceSqr < smallestDistanceSqr) {
				smallestDistanceSqr = distanceSqr;
				nearestCoin = coin;
			}
		}
		if (nearestCoin != null) {
			consumer.accept(nearestCoin, player);
			sourceStack.sendSuccess(Component.literal("Successfully performed command on coin at " + nearestCoin.position().toString()).withStyle(ChatFormatting.BOLD), true);
			return 1;
		}
		return 0;
	}

}
