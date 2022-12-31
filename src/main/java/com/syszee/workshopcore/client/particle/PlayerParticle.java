package com.syszee.workshopcore.client.particle;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.syszee.workshopcore.client.WCRenderTypes;
import com.syszee.workshopcore.common.particles.PlayerParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class PlayerParticle extends SingleQuadParticle {
	private final Supplier<GameProfile> profile;
	private final float u0, u1, v0, v1;

	public PlayerParticle(ClientLevel clientLevel, double x, double y, double z, double xd, double yd, double zd, Supplier<GameProfile> profile, boolean faceOnly) {
		super(clientLevel, x, y, z, xd, yd, zd);
		this.profile = profile;
		if (faceOnly) {
			this.u0 = 0.25F;
			this.u1 = 0.125F;
			this.v0 = 0.125F;
			this.v1 = 0.25F;
		} else {
			this.u1 = this.random.nextInt(16) * 4.0F / 64.0F;
			this.u0 = this.u1 + 0.0625F;
			this.v0 = this.random.nextInt(16) * 4.0F / 64.0F;
			this.v1 = this.v0 + 0.0625F;
		}
		this.gravity = 1.0F;
		this.quadSize /= 2.0F;
		this.lifetime = 20 + this.random.nextInt(41);
	}

	@Override
	public void render(VertexConsumer vertexConsumer, Camera camera, float f) {
		MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
		ResourceLocation skinLocation;
		GameProfile profile = this.profile.get();
		if (profile == null) {
			skinLocation = DefaultPlayerSkin.getDefaultSkin();
		} else {
			SkinManager skinManager = Minecraft.getInstance().getSkinManager();
			var map = skinManager.getInsecureSkinInformation(profile);
			MinecraftProfileTexture skinTexture = map.get(MinecraftProfileTexture.Type.SKIN);
			skinLocation = skinTexture != null ? skinManager.registerTexture(skinTexture, MinecraftProfileTexture.Type.SKIN) : DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(profile));
		}
		super.render(bufferSource.getBuffer(WCRenderTypes.TRANSLUCENT_PARTICLE.apply(skinLocation)), camera, f);
		bufferSource.endBatch();
	}

	@Override
	protected float getU0() {
		return this.u0;
	}

	@Override
	protected float getU1() {
		return this.u1;
	}

	@Override
	protected float getV0() {
		return this.v0;
	}

	@Override
	protected float getV1() {
		return this.v1;
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.CUSTOM;
	}

	public static class Provider implements ParticleProvider<PlayerParticleOptions> {
		public Particle createParticle(PlayerParticleOptions options, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
			return new PlayerParticle(clientLevel, d, e, f, g, h, i, options.getProfile(), options.isFaceOnly());
		}
	}
}
