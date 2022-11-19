package com.syszee.workshopcore.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.syszee.workshopcore.client.model.CoinModel;
import com.syszee.workshopcore.common.entity.Coin;
import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CoinRenderer extends EntityRenderer<Coin> {
	private static final ResourceLocation TEXTURE = new ResourceLocation(WorkshopCore.MOD_ID, "textures/entity/coin.png");
	private final CoinModel model;

	public CoinRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.model = new CoinModel(context.bakeLayer(CoinModel.LAYER_LOCATION));
	}

	@Override
	public void render(Coin coin, float f, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
		poseStack.pushPose();
		poseStack.scale(-1.0F, -1.0F, 1.0F);
		poseStack.translate(0.0D, -1.5010000467300415D, 0.0D);
		this.model.setupAnim(coin, 0.0F, 0.0F, partialTicks + coin.tickCount, 0.0F, 0.0F);
		this.model.renderToBuffer(poseStack, multiBufferSource.getBuffer(RenderType.entityCutout(this.getTextureLocation(coin))), i, OverlayTexture.pack(0.0F, false), 1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
		super.render(coin, f, partialTicks, poseStack, multiBufferSource, i);
	}

	@Override
	public ResourceLocation getTextureLocation(Coin entity) {
		return TEXTURE;
	}
}
