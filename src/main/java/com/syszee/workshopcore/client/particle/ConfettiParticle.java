package com.syszee.workshopcore.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ConfettiParticle extends TextureSheetParticle {
	private int growthTimer = 7;

	public ConfettiParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
		super(clientLevel, d, e, f, g, Math.random(), i);
		this.setLifetime(20 + clientLevel.getRandom().nextInt(20, 20 * 5));
		this.scale(0.25F);
		this.gravity = 1.3F;
		this.xd *= 1.6;
		this.yd *= 3.6D;
		this.zd *= 1.6;
	}

	@Override
	public void tick() {
		super.tick();

		if (--this.growthTimer > 0) {
			this.scale(1.219F);
		}

		this.oRoll = this.roll;
		if (!this.onGround) {
			this.roll += Mth.PI * (Math.abs(this.yd + 0.1D) + 0.05D);
			if (this.y == this.yo && this.yd < 0.0D) {
				this.lifetime = Math.max(this.lifetime - 10, this.age);
			}
		}
	}

	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprite;

		public Provider(SpriteSet spriteSet) {
			this.sprite = spriteSet;
		}

		@Override
		public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
			ConfettiParticle confettiParticle = new ConfettiParticle(clientLevel, d, e, f, g, h, i);
			confettiParticle.pickSprite(this.sprite);
			return confettiParticle;
		}
	}

	@Override
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
	}
}
