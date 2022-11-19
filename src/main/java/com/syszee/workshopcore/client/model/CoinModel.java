package com.syszee.workshopcore.client.model;

import com.syszee.workshopcore.common.entity.Coin;
import com.syszee.workshopcore.core.WorkshopCore;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class CoinModel extends HierarchicalModel<Coin> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(WorkshopCore.MOD_ID, "coin"), "main");
	private static final AnimationDefinition FLIP_TAILS_ANIMATION = AnimationDefinition.Builder.withLength(3f).addAnimation("coin", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2f, KeyframeAnimations.posVec(0f, 132f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4f, KeyframeAnimations.posVec(0f, 120f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8f, KeyframeAnimations.posVec(0f, 11f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2f, KeyframeAnimations.posVec(0f, 2f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.08f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("coin", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.92f, KeyframeAnimations.degreeVec(-1430f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8f, KeyframeAnimations.degreeVec(-2730f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.92f, KeyframeAnimations.degreeVec(-2700f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2f, KeyframeAnimations.degreeVec(-2695f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.08f, KeyframeAnimations.degreeVec(-2700f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).build();
	public static final AnimationDefinition FLIP_HEADS_ANIMATION = AnimationDefinition.Builder.withLength(3f).addAnimation("coin", new AnimationChannel(AnimationChannel.Targets.POSITION, new Keyframe(0f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.2f, KeyframeAnimations.posVec(0f, 132f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.4f, KeyframeAnimations.posVec(0f, 120f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8f, KeyframeAnimations.posVec(0f, 11f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.92f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2f, KeyframeAnimations.posVec(0f, 2f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.08f, KeyframeAnimations.posVec(0f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).addAnimation("coin", new AnimationChannel(AnimationChannel.Targets.ROTATION, new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(0.92f, KeyframeAnimations.degreeVec(-1430f, 0f, 0f), AnimationChannel.Interpolations.CATMULLROM), new Keyframe(1.8f, KeyframeAnimations.degreeVec(-2552.5f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(1.92f, KeyframeAnimations.degreeVec(-2520f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2f, KeyframeAnimations.degreeVec(-2515f, 0f, 0f), AnimationChannel.Interpolations.LINEAR), new Keyframe(2.08f, KeyframeAnimations.degreeVec(-2520f, 0f, 0f), AnimationChannel.Interpolations.LINEAR))).build();
	private final ModelPart root;

	public CoinModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition coin = partdefinition.addOrReplaceChild("coin", CubeListBuilder.create().texOffs(0, 0).addBox(-24.0F, -4.0F, -24.0F, 48.0F, 8.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Coin coin, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root.getAllParts().forEach(ModelPart::resetPose);
		this.animate(coin.flipAnimationState, coin.isHeads() ? FLIP_HEADS_ANIMATION : FLIP_TAILS_ANIMATION, ageInTicks);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}
