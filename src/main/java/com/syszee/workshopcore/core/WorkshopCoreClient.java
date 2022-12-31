package com.syszee.workshopcore.core;

import com.syszee.workshopcore.client.model.CoinModel;
import com.syszee.workshopcore.client.render.BodyRenderer;
import com.syszee.workshopcore.client.render.CoinRenderer;
import com.syszee.workshopcore.core.networking.WCNetworking;
import com.syszee.workshopcore.core.registry.WCEntityTypes;
import com.syszee.workshopcore.core.registry.WCParticleTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public final class WorkshopCoreClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		EntityModelLayerRegistry.registerModelLayer(CoinModel.LAYER_LOCATION, CoinModel::createBodyLayer);

		EntityRendererRegistry.register(WCEntityTypes.COIN, CoinRenderer::new);
		EntityRendererRegistry.register(WCEntityTypes.BODY, BodyRenderer::new);

		WCParticleTypes.registerFactories();
		WCNetworking.client();
	}
}
