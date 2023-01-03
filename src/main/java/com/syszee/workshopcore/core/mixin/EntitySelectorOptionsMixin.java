package com.syszee.workshopcore.core.mixin;

import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.commands.arguments.selector.options.EntitySelectorOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(EntitySelectorOptions.class)
public final class EntitySelectorOptionsMixin {
	@Shadow
	private static void register(String string, EntitySelectorOptions.Modifier modifier, Predicate<EntitySelectorParser> predicate, Component component) {}

	@Inject(method = "bootStrap", at = @At(value = "JUMP", opcode = Opcodes.IFNE, ordinal = 0))
	private static void bootstrapWC(CallbackInfo info) {
		register("op", (entitySelectorParser) -> {
			boolean operatorsOnly = entitySelectorParser.getReader().readBoolean();
			entitySelectorParser.addPredicate(entity -> {
				if (!(entity instanceof ServerPlayer serverPlayer)) return false;
				var server = serverPlayer.getServer();
				if (server == null) return false;
				return server.getPlayerList().isOp(serverPlayer.getGameProfile()) == operatorsOnly;
			});
		}, (entitySelectorParser) -> true, Component.translatable("argument.entity.options.op.description"));
	}
}
