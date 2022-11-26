package com.syszee.workshopcore.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.syszee.workshopcore.common.entity.Body;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

public class BodyRenderer extends EntityRenderer<Body> {

	public BodyRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(Body body, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
		var entity = body.getEntity();
		if (entity == null) return;
		Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).render(entity, f, 0.0F, poseStack, multiBufferSource, i);
	}

	@Override
	public ResourceLocation getTextureLocation(Body body) {
		var entity = body.getEntity();
		if (entity == null) return MissingTextureAtlasSprite.getLocation();
		return Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).getTextureLocation(entity);
	}

}
