package dev.aaronhowser.mods.geneticsresequenced.entities.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.Minecraft
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.world.entity.LivingEntity

class SupportSlimePlayerHeadLayer<T : LivingEntity>(
    renderer: RenderLayerParent<T, SupportSlimeModel<T>>,
    entityModelSet: EntityModelSet
) : RenderLayer<T, SupportSlimeModel<T>>(renderer) {

    val model: EntityModel<T> = SupportSlimeModel(
        entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)
    )

    override fun render(
        poseStack: PoseStack,
        buffer: MultiBufferSource,
        packedLight: Int,
        livingEntity: T,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTicks: Float,
        ageInTicks: Float,
        headYaw: Float,
        headPitch: Float
    ) {

        val minecraft = Minecraft.getInstance()
        val isGlowingAndInvisible = minecraft.shouldEntityAppearGlowing(livingEntity) && !livingEntity.isInvisible

        if (!isGlowingAndInvisible && livingEntity.isInvisible) return

        val vertexConsumer: VertexConsumer = if (isGlowingAndInvisible) {
            buffer.getBuffer(RenderType.outline(this.getTextureLocation(livingEntity)))
        } else {
            buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(livingEntity)))
        }

        (this.parentModel as SupportSlimeModel).copyPropertiesTo(this.model)

        this.model.apply {
            prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks)
            setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch)
            renderToBuffer(
                poseStack,
                vertexConsumer,
                packedLight,
                LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0f),
                1.0f,
                1.0f,
                1.0f,
                1.0f
            )
        }


    }
}