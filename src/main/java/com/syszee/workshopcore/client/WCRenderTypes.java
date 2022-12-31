package com.syszee.workshopcore.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class WCRenderTypes extends RenderType {
	public static final ShaderStateShard RENDERTYPE_TRANSLUCENT_PARTICLE_SHADER = new RenderStateShard.ShaderStateShard(GameRenderer::getParticleShader);
	public static final Function<ResourceLocation, RenderType> TRANSLUCENT_PARTICLE = Util.memoize((resourceLocation) -> {
		RenderType.CompositeState compositeState = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_TRANSLUCENT_PARTICLE_SHADER).setTextureState(new TextureStateShard(resourceLocation, false, false)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setLightmapState(LIGHTMAP).createCompositeState(true);
		return create(WorkshopCore.MOD_ID + ":translucent_particle", DefaultVertexFormat.PARTICLE, VertexFormat.Mode.QUADS, 256, true, false, compositeState);
	});

	private WCRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean bl, boolean bl2, Runnable runnable, Runnable runnable2) {
		super(string, vertexFormat, mode, i, bl, bl2, runnable, runnable2);
	}
}
