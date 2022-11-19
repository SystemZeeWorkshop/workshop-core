package com.syszee.workshopcore.core.registry;

import com.syszee.workshopcore.common.entity.Coin;
import com.syszee.workshopcore.core.WorkshopCore;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;

public class WCEntityTypes {
	public static final EntityType<Coin> COIN = register("coin", FabricEntityTypeBuilder.create(MobCategory.MISC, Coin::new).fireImmune().dimensions(EntityDimensions.scalable(3.0F, 0.5F)));

	public static <T extends Entity> EntityType<T> register(String name, FabricEntityTypeBuilder<T> builder) {
		return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(WorkshopCore.MOD_ID, name), builder.build());
	}

	public static void register() {}
}
