package dev.aaronhowser.mods.geneticsresequenced.entities.client

import com.mojang.authlib.minecraft.MinecraftProfileTexture
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.entities.SupportSlime
import net.minecraft.client.model.SlimeModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.SlimeRenderer
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
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

    override fun render(
        pEntity: SupportSlime,
        pEntityYaw: Float,
        pPartialTicks: Float,
        pMatrixStack: PoseStack,
        pBuffer: MultiBufferSource,
        pPackedLight: Int
    ) {

        val getOwnerUuid = pEntity.getOwnerUuid() ?: return
        val player = pEntity.level.getPlayerByUUID(getOwnerUuid) ?: return

        val propertyMap = player.gameProfile.properties
        var skinUrl: String? = null
        if (propertyMap.containsKey("textures")) {

            for (property in propertyMap["textures"]) {
                val sessionService = player.server!!.sessionService
                val textures = sessionService.getTextures(player.gameProfile, false)
                if (textures.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                    val skinTexture = textures[MinecraftProfileTexture.Type.SKIN]
                    skinUrl = skinTexture?.url
                }
            }
        }

        if (skinUrl != null) {
            println(skinUrl)
        }

        this.shadowRadius = 0.25f * pEntity.size.toFloat()
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight)
    }

    override fun scale(
        pLivingEntity: SupportSlime,
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
    override fun getTextureLocation(pEntity: SupportSlime): ResourceLocation {
        return SlimeRenderer.SLIME_LOCATION
    }
}