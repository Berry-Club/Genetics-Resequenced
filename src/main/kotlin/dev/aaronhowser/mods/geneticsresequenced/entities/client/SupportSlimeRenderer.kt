package dev.aaronhowser.mods.geneticsresequenced.entities.client

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Quaternion
import dev.aaronhowser.mods.geneticsresequenced.entities.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.client.model.SlimeModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.SlimeRenderer
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

//TODO: What if it looks like your skin's head???
@OnlyIn(Dist.CLIENT)
class SupportSlimeRenderer(
    context: EntityRendererProvider.Context
) : MobRenderer<SupportSlime, SlimeModel<SupportSlime>>
    (
    context,
    SlimeModel(
        context.bakeLayer(ModelLayers.SLIME)
    ),
    0.25f
) {
    init {
        this.addLayer(SlimeOuterLayer(this, context.modelSet))
    }

    private val itemRenderer: ItemRenderer = context.itemRenderer

    private var headStack: ItemStack? = null

    private fun getHead(pEntity: SupportSlime): ItemStack {
        if (headStack != null) return headStack!!

        val ownerUuid = pEntity.getOwnerUuid()
        val owner = ownerUuid?.let { pEntity.level.getPlayerByUUID(it) }

        if (owner == null) return ItemStack.EMPTY

        val ownerUsername = owner.gameProfile.name

        val newHeadStack = Items.PLAYER_HEAD.itemStack
        newHeadStack.getOrCreateTag().putString("SkullOwner", ownerUsername)

        headStack = newHeadStack
        return newHeadStack
    }

    override fun render(
        pEntity: SupportSlime,
        pEntityYaw: Float,
        pPartialTicks: Float,
        pPoseStack: PoseStack,
        pBuffer: MultiBufferSource,
        pPackedLight: Int
    ) {

        val scale = 2f * pEntity.size

        pPoseStack.translate(0.0, pEntity.size.toDouble() / 2, 0.0)
        pPoseStack.scale(scale, scale, scale)

        val lookAngle = pEntity.lookAngle
        pPoseStack.mulPose(
            Quaternion.fromXYZ(
                lookAngle.x.toFloat(),
                lookAngle.y.toFloat(),
                lookAngle.z.toFloat(),
            )
        )

        itemRenderer.renderStatic(
            getHead(pEntity),
            TransformType.FIXED,
            pPackedLight,
            OverlayTexture.NO_OVERLAY,
            pPoseStack,
            pBuffer,
            pEntity.id
        )
    }

    override fun scale(
        pLivingEntity: SupportSlime,
        pMatrixStack: PoseStack,
        pPartialTickTime: Float
    ) {
        pMatrixStack.scale(0.999f, 0.999f, 0.999f)
        pMatrixStack.translate(0.0, 0.001, 0.0)
        val sizeFactor = pLivingEntity.size.toFloat()
        val squishFactor = Mth.lerp(
            pPartialTickTime,
            pLivingEntity.oSquish,
            pLivingEntity.squish
        ) / (sizeFactor * 0.5f + 1.0f)
        val inverseSquish = 1.0f / (squishFactor + 1.0f)
        pMatrixStack.scale(inverseSquish * sizeFactor, 1.0f / inverseSquish * sizeFactor, inverseSquish * sizeFactor)
    }

    /**
     * Returns the location of an entity's texture.
     */
    override fun getTextureLocation(pEntity: SupportSlime): ResourceLocation {
        return SlimeRenderer.SLIME_LOCATION
    }
}