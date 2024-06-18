package dev.aaronhowser.mods.geneticsresequenced.entities.client

import com.mojang.authlib.GameProfile
import com.mojang.authlib.minecraft.MinecraftProfileTexture
import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.geneticsresequenced.entities.SupportSlime
import net.minecraft.client.Minecraft
import net.minecraft.client.model.SlimeModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.SlimeRenderer
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer
import net.minecraft.client.resources.DefaultPlayerSkin
import net.minecraft.core.UUIDUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import java.util.*

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

    private var ownerUuid: UUID? = null
    private var skinRl: ResourceLocation? = null
    private fun setSkinRl(pEntity: SupportSlime) {
        if (skinRl != null) return

        val getOwnerUuid = pEntity.getOwnerUuid() ?: return
        ownerUuid = getOwnerUuid
        val player = pEntity.level.getPlayerByUUID(getOwnerUuid) ?: return

        if (player is AbstractClientPlayer) {
            println("Player is AbstractClientPlayer")

            val resourceLocation = player.skinTextureLocation
            println("Resource Location: $resourceLocation")

            skinRl = resourceLocation
        }
    }

    override fun render(
        pEntity: SupportSlime,
        pEntityYaw: Float,
        pPartialTicks: Float,
        pMatrixStack: PoseStack,
        pBuffer: MultiBufferSource,
        pPackedLight: Int
    ) {
        setSkinRl(pEntity)

        this.shadowRadius = 0.25f * pEntity.size.toFloat()
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight)
    }

    override fun scale(
        pLivingEntity: SupportSlime,
        pMatrixStack: PoseStack,
        pPartialTickTime: Float
    ) {
        val f1 = pLivingEntity.size.toFloat()
        val f2 = Mth.lerp(pPartialTickTime, pLivingEntity.oSquish, pLivingEntity.squish) / (f1 * 0.5f + 1.0f)
        val f3 = 1.0f / (f2 + 1.0f)
        pMatrixStack.scale(f3 * f1, 1.0f / f3 * f1, f3 * f1)
    }

    override fun getRenderType(
        pLivingEntity: SupportSlime,
        pBodyVisible: Boolean,
        pTranslucent: Boolean,
        pGlowing: Boolean
    ): RenderType? {

        val ownerPlayer = ownerUuid?.let { pLivingEntity.level.getPlayerByUUID(it) } ?: return super.getRenderType(
            pLivingEntity,
            pBodyVisible,
            pTranslucent,
            pGlowing
        )

        val skinManager = Minecraft.getInstance().skinManager

        val ownerProfile: GameProfile = ownerPlayer.gameProfile
        val map = skinManager.getInsecureSkinInformation(ownerProfile)

        return if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            RenderType.entityTranslucent(
                skinManager.registerTexture(
                    map[MinecraftProfileTexture.Type.SKIN],
                    MinecraftProfileTexture.Type.SKIN
                )
            )
        } else {
            RenderType.entityCutoutNoCull(
                DefaultPlayerSkin.getDefaultSkin(
                    UUIDUtil.getOrCreatePlayerUUID(ownerProfile)
                )
            )
        }

    }

    /**
     * Returns the location of an entity's texture.
     */
    override fun getTextureLocation(pEntity: SupportSlime): ResourceLocation {
        return skinRl ?: SlimeRenderer.SLIME_LOCATION
    }
}