package de.tr7zw.firstperson.features.back;

import de.tr7zw.firstperson.features.Back;
import de.tr7zw.firstperson.util.SettingsUtil;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class WingFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

	public WingFeatureRenderer(
			FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRendererContext) {
		super(featureRendererContext);
		bone = new ModelPart(40, 40, 0, 0);
		bone.setPivot(0.0F, 12.0F, 0.0F);

		cube_r1 = new ModelPart(40, 40, 0, 0);
		cube_r1.setPivot(0.0F, 0.0F, 2.0F);
		bone.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 2.3562F, 0.0F);
		cube_r1.setTextureOffset(0, 0).addCuboid(-19.5F, -20.0F, 0.5F, 20.0F, 20.0F, 0.0F, 0.0F, false);

		cube_r2 = new ModelPart(40, 40, 0, 0);
		cube_r2.setPivot(0.0F, 0.0F, 2.0F);
		bone.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.7854F, 0.0F);
		cube_r2.setTextureOffset(0, 0).addCuboid(-19.5F, -20.0F, -0.5F, 20.0F, 20.0F, 0.0F, 0.0F, false);
		
		butterflyTexture = new Identifier("firstperson", "textures/features/back/butterfly.png");
		fantasyTexture = new Identifier("firstperson", "textures/features/back/fantasywings.png");
	}
	
	private final ModelPart bone;
	private final ModelPart cube_r1;
	private final ModelPart cube_r2;
	private final Identifier butterflyTexture;
	private final Identifier fantasyTexture;

	@Override
	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light,
			AbstractClientPlayerEntity abstractClientPlayerEntity, float limbAngle, float limbDistance, float tickDelta,
			float animationProgress, float headYaw, float headPitch) {
		RenderLayer renderlayer = null;
		if(SettingsUtil.hasEnabled(abstractClientPlayerEntity, Back.BUTTERFLY)) {
			renderlayer = RenderLayer.getEntityCutout(butterflyTexture);
		}else if(SettingsUtil.hasEnabled(abstractClientPlayerEntity, Back.FANTASYWINGS)) {
			renderlayer = RenderLayer.getEntityTranslucentCull(fantasyTexture);
		} else {
			return;
		}
		if (abstractClientPlayerEntity.hasSkinTexture() && !abstractClientPlayerEntity.isInvisible()) {
			VertexConsumer vertexConsumer = vertexConsumerProvider
					.getBuffer(renderlayer);
			int m = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

			matrixStack.push();
			renderWing(matrixStack, vertexConsumer, light, m, ((PlayerEntityModel<AbstractClientPlayerEntity>) this.getContextModel()));
			matrixStack.pop();
		}
	}
	
	public void renderWing(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, PlayerEntityModel<AbstractClientPlayerEntity> model) {
		matrices.push();
		model.torso.rotate(matrices);
		float timestep = System.currentTimeMillis()%4000;
		timestep /= 4000F;
		timestep *= 2*Math.PI;
		timestep = (float) Math.sin(timestep);
		setRotationAngle(cube_r1, 0.0F, 2.3562F + (timestep * 0.1F), 0.0F);
		setRotationAngle(cube_r2, 0.0F, 0.7854F + (-timestep * 0.1F), 0.0F);
		this.bone.render(matrices, vertices, light, overlay);
		matrices.pop();
	}
	
	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}