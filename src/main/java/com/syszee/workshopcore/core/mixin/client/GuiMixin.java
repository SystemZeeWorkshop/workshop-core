package com.syszee.workshopcore.core.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.syszee.workshopcore.core.WorkshopCore;
import com.syszee.workshopcore.core.WorkshopCoreClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.syszee.workshopcore.client.WorkshopClientUtil.fillGradient;

@Mixin(Gui.class)
public final class GuiMixin {
	private static final ResourceLocation LIGHT_BULB_ICON = new ResourceLocation(WorkshopCore.MOD_ID, "textures/light_bulb_icon.png");

	@Shadow
	private int screenWidth;
	@Shadow
	private int screenHeight;

	@Inject(method = "render", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/Options;hideGui:Z", ordinal = 1))
	private void renderPopups(PoseStack poseStack, float f, CallbackInfo info) {
		var entityPopup = WorkshopCoreClient.entityPopup;
		if (entityPopup != null) {
			Entity crosshairPickEntity = Minecraft.getInstance().crosshairPickEntity;
			if (crosshairPickEntity != entityPopup.entity()) {
				WorkshopCoreClient.entityPopup = null;
			} else if (!Minecraft.getInstance().options.hideGui) {
				int width = this.screenWidth;
				int x = width / 2 + 20;
				int y = this.screenHeight / 2 - 16;
				Font font = Minecraft.getInstance().font;
				Component title = entityPopup.title();
				Component description = entityPopup.description();
				int k = Math.min(192, 18 + Math.max(font.width(title), font.width(description)));
				int edgeX = x + k + 4;
				if (edgeX > width) k -= (edgeX - width);
				int wrapping = k - 14;
				var wrappedSplitDescription = font.split(description, wrapping);
				int l = 11 + wrappedSplitDescription.size() * 9;
				Tesselator tesselator = Tesselator.getInstance();
				BufferBuilder bufferBuilder = tesselator.getBuilder();
				RenderSystem.setShader(GameRenderer::getPositionColorShader);
				bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
				Matrix4f matrix4f = poseStack.last().pose();
				int outlineStartColor = 1347420415;
				int outlineEndColor = 1344798847;
				fillGradient(matrix4f, bufferBuilder, x - 3, y - 4, x + k + 3, y - 3, 400, -267386864, -267386864);
				fillGradient(matrix4f, bufferBuilder, x - 3, y + l + 3, x + k + 3, y + l + 4, 400, -267386864, -267386864);
				fillGradient(matrix4f, bufferBuilder, x - 3, y - 3, x + k + 3, y + l + 3, 400, -267386864, -267386864);
				fillGradient(matrix4f, bufferBuilder, x - 4, y - 3, x - 3, y + l + 3, 400, -267386864, -267386864);
				fillGradient(matrix4f, bufferBuilder, x + k + 3, y - 3, edgeX, y + l + 3, 400, -267386864, -267386864);
				fillGradient(matrix4f, bufferBuilder, x - 3, y - 3 + 1, x - 3 + 1, y + l + 3 - 1, 400, outlineStartColor, outlineEndColor);
				fillGradient(matrix4f, bufferBuilder, x + k + 2, y - 3 + 1, x + k + 3, y + l + 3 - 1, 400, outlineStartColor, outlineEndColor);
				fillGradient(matrix4f, bufferBuilder, x - 3, y - 3, x + k + 3, y - 3 + 1, 400, outlineStartColor, outlineStartColor);
				fillGradient(matrix4f, bufferBuilder, x - 3, y + l + 2, x + k + 3, y + l + 3, 400, outlineEndColor, outlineEndColor);
				RenderSystem.enableDepthTest();
				RenderSystem.enableBlend();
				RenderSystem.defaultBlendFunc();
				BufferUploader.drawWithShader(bufferBuilder.end());

				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.setShaderTexture(0, LIGHT_BULB_ICON);
				RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
				bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
				int iconStartX = x - 1;
				int iconEndX = x + 15;
				int iconStartY = y - 1;
				int iconEndY = y + 15;
				bufferBuilder.vertex(matrix4f, iconStartX, iconEndY, 400.0F).uv(0, 1).endVertex();
				bufferBuilder.vertex(matrix4f, iconEndX, iconEndY, 400.0F).uv(1, 1).endVertex();
				bufferBuilder.vertex(matrix4f, iconEndX, iconStartY, 400.0F).uv(1, 0).endVertex();
				bufferBuilder.vertex(matrix4f, iconStartX, iconStartY, 400.0F).uv(0, 0).endVertex();
				BufferUploader.drawWithShader(bufferBuilder.end());

				RenderSystem.disableBlend();

				poseStack.pushPose();
				poseStack.translate(0.0D, 0.0D, 400.0D);
				matrix4f = poseStack.last().pose();
				MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
				int textX = x + 18;
				font.drawInBatch(title, textX, y, -1, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
				int currentY = y + 12;
				for (var iterator = wrappedSplitDescription.iterator(); iterator.hasNext(); currentY += 9) {
					font.drawInBatch(iterator.next(), textX, currentY, -1, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
				}
				bufferSource.endBatch();
				poseStack.popPose();
			}
		}
	}
}
