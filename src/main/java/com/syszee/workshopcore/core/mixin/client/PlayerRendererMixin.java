package com.syszee.workshopcore.core.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.syszee.workshopcore.core.WCPlayer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>  {

	public PlayerRendererMixin(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> entityModel, float f) {
		super(context, entityModel, f);
	}

	@Inject(method = "scale", at = @At("RETURN"))
	private void addSwellScaling(AbstractClientPlayer abstractClientPlayer, PoseStack poseStack, float partialTicks, CallbackInfo info) {
		float g = ((WCPlayer) abstractClientPlayer).getSwelling(partialTicks);
		float h = 1.0F + Mth.sin(g * 100.0F) * g * 0.01F;
		g = Mth.clamp(g, 0.0F, 1.0F);
		g *= g;
		g *= g;
		float i = (1.0F + g * 0.4F) * h;
		float j = (1.0F + g * 0.1F) / h;
		poseStack.scale(i, j, i);
	}

	@Inject(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", ordinal = 0, shift = At.Shift.AFTER))
	private void renderCoinChoice(AbstractClientPlayer abstractClientPlayer, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo info) {
		WCPlayer.CoinChoice coinChoice = ((WCPlayer) abstractClientPlayer).getCoinChoice();
		if (coinChoice != WCPlayer.CoinChoice.UNDEFINED) {
			super.renderNameTag(abstractClientPlayer, coinChoice == WCPlayer.CoinChoice.HEADS ? WCPlayer.HEADS_COMPONENT : WCPlayer.TAILS_COMPONENT, poseStack, multiBufferSource, i);
			poseStack.translate(0.0D, 9.0F * 1.15F * 0.025F, 0.0D);
		}
	}

}
