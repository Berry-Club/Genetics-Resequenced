package dev.aaronhowser.mods.geneticsresequenced.entity.client

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.config.ClientConfig
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.client.model.SlimeModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.SlimeRenderer
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ResolvableProfile
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import org.joml.Vector3f


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
        val owner = ownerUuid?.let { pEntity.level().getPlayerByUUID(it) }

        if (owner == null) return ItemStack.EMPTY

        val ownerProfile = owner.gameProfile
        val ownerProfileComponent = ResolvableProfile(ownerProfile)

        val newHeadStack = Items.PLAYER_HEAD.itemStack
        newHeadStack.set(
            DataComponents.PROFILE,
            ownerProfileComponent
        )

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

        if (ClientConfig.supportSlimeRenderDebug.get()) {
            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight)
        }

        /**
         * FIXME:
         *  Hitbox is a bit broken.
         *  This isn't super important because they never attack players, but still.
         */
        pPoseStack.translate(
            0.0,
            pEntity.size.toDouble() / 4,
            0.0
        )
        pPoseStack.scale(
            pEntity.size.toFloat(),
            pEntity.size.toFloat(),
            pEntity.size.toFloat()
        )

        //FIXME: Rotate heads to where they're looking
        val lerpedRotY = Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.yRot)
        val vectorPositiveY = Vector3f(0f, 1f, 0f)
//        val quaternion = Quaternionf().rotateY(lerpedRotY)
//        pPoseStack.mulPose(quaternion)

        itemRenderer.renderStatic(
            getHead(pEntity),
            ItemDisplayContext.FIXED,
            pPackedLight,
            OverlayTexture.NO_OVERLAY,
            pPoseStack,
            pBuffer,
            pEntity.level(),
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