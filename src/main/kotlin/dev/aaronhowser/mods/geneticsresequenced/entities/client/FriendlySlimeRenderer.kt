package dev.aaronhowser.mods.geneticsresequenced.entities.client

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.entities.FriendlySlime
import net.minecraft.client.model.SlimeModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
class FriendlySlimeRenderer(
    context: EntityRendererProvider.Context
) : MobRenderer<FriendlySlime, SlimeModel<FriendlySlime>>
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

    override fun render(
        pEntity: FriendlySlime,
        pEntityYaw: Float,
        pPartialTicks: Float,
        pMatrixStack: PoseStack,
        pBuffer: MultiBufferSource,
        pPackedLight: Int
    ) {
        this.shadowRadius = 0.25f * pEntity.size.toFloat()
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight)
    }

    protected override fun scale(
        pLivingEntity: FriendlySlime,
        pMatrixStack: PoseStack,
        pPartialTickTime: Float
    ) {
        val f = 0.999f
        pMatrixStack.scale(0.999f, 0.999f, 0.999f)
        pMatrixStack.translate(0.0, 0.001, 0.0)
        val f1 = pLivingEntity.size.toFloat()
        val f2 = Mth.lerp(pPartialTickTime, pLivingEntity.oSquish, pLivingEntity.squish) / (f1 * 0.5f + 1.0f)
        val f3 = 1.0f / (f2 + 1.0f)
        pMatrixStack.scale(f3 * f1, 1.0f / f3 * f1, f3 * f1)
    }

    /**
     * Returns the location of an entity's texture.
     */
    override fun getTextureLocation(pEntity: FriendlySlime): ResourceLocation {
        return SLIME_LOCATION
    }

    companion object {
        private val SLIME_LOCATION = ResourceLocation("textures/entity/slime/slime.png")
    }
}