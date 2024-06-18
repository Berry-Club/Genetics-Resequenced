package dev.aaronhowser.mods.geneticsresequenced.entities.client

import com.mojang.blaze3d.vertex.PoseStack
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

    private fun getHead(pEntity: SupportSlime): ItemStack {
        val ownerUuid = pEntity.getOwnerUuid()
        val owner = ownerUuid?.let { pEntity.level.getPlayerByUUID(it) }

        if (owner == null) return ItemStack.EMPTY

        val ownerUsername = owner.gameProfile.name

        val headStack = Items.PLAYER_HEAD.itemStack
        headStack.tag?.putString("SkullOwner", ownerUsername)

        return headStack
    }

    override fun render(
        pEntity: SupportSlime,
        pEntityYaw: Float,
        pPartialTicks: Float,
        pMatrixStack: PoseStack,
        pBuffer: MultiBufferSource,
        pPackedLight: Int
    ) {

        itemRenderer.renderStatic(
            getHead(pEntity),
            TransformType.FIXED,
            pPackedLight,
            OverlayTexture.NO_OVERLAY,
            pMatrixStack,
            pBuffer,
            pEntity.id
        )

        this.shadowRadius = 0.25f * pEntity.size.toFloat()
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