package com.syszee.workshopcore.common.particles;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.syszee.workshopcore.core.WorkshopCore;
import com.syszee.workshopcore.core.registry.WCParticleTypes;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class PlayerParticleOptions implements ParticleOptions {
	public static final Deserializer<PlayerParticleOptions> DESERIALIZER = new Deserializer<>() {
		//TODO: Fix this? I think?
		public PlayerParticleOptions fromCommand(ParticleType<PlayerParticleOptions> particleType, StringReader stringReader) throws CommandSyntaxException {
			stringReader.expect(' ');
			int i = stringReader.getCursor();

			while(stringReader.canRead() && stringReader.peek() != ' ') {
				stringReader.skip();
			}

			String string = stringReader.getString().substring(i, stringReader.getCursor());
			Optional<GameProfile> optional = WorkshopCore.server.getProfileCache().get(string);
			stringReader.expect(' ');
			return new PlayerParticleOptions(optional.orElseThrow(GameProfileArgument.ERROR_UNKNOWN_PLAYER::create), stringReader.readBoolean());
		}

		public PlayerParticleOptions fromNetwork(ParticleType<PlayerParticleOptions> particleType, FriendlyByteBuf friendlyByteBuf) {
			return new PlayerParticleOptions(friendlyByteBuf.readOptional(FriendlyByteBuf::readGameProfile).orElse(null), friendlyByteBuf.readBoolean());
		}
	};

	private Supplier<GameProfile> profile = () -> null;
	private final boolean faceOnly;

	public PlayerParticleOptions(@Nullable GameProfile profile, boolean faceOnly) {
		if (profile != null) {
			this.profile = () -> profile;
			SkullBlockEntity.updateGameprofile(profile, loadedProfile -> this.profile = () -> loadedProfile);
		}
		this.faceOnly = faceOnly;
	}

	@Override
	public ParticleType<?> getType() {
		return WCParticleTypes.PLAYER;
	}

	@Override
	public void writeToNetwork(FriendlyByteBuf friendlyByteBuf) {
		friendlyByteBuf.writeOptional(Optional.ofNullable(this.profile.get()), FriendlyByteBuf::writeGameProfile);
		friendlyByteBuf.writeBoolean(this.faceOnly);
	}

	@Override
	public String writeToString() {
		return Registry.PARTICLE_TYPE.getKey(this.getType()) + " " + "{" +
				"profile=" + this.profile +
				", faceOnly=" + this.faceOnly +
				'}';
	}

	public Supplier<GameProfile> getProfile() {
		return this.profile;
	}

	public boolean isFaceOnly() {
		return this.faceOnly;
	}
}
