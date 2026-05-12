package dev.aaronhowser.mods.geneticsresequenced.client.renderer.entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import net.minecraft.client.model.SkullModelBase
import net.minecraft.client.model.SlimeModel
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.SlimeRenderer
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.level.block.SkullBlock
import java.util.*

class SupportSlimeRenderer(
	context: EntityRendererProvider.Context
) : MobRenderer<SupportSlime, SlimeModel<SupportSlime>>(
	context,
	SlimeModel(context.bakeLayer(ModelLayers.SLIME)),
	0.25f
) {

	private val skullModel: SkullModelBase =
		SkullBlockRenderer.createSkullRenderers(context.modelSet)
			.getValue(SkullBlock.Types.PLAYER)

	init {
		addLayer(SlimeOuterLayer(this, context.modelSet))
	}

	private val skinRenderTypesByOwner: MutableMap<UUID, RenderType> = mutableMapOf()

	private fun getPlayerSkinRenderType(entity: SupportSlime): RenderType? {
		val ownerUuid = entity.ownerUuid ?: return null

		val existing = skinRenderTypesByOwner[ownerUuid]
		if (existing != null) return existing

		val owner = entity.level().getPlayerByUUID(ownerUuid) ?: return null

		val skinRenderType = SkullBlockRenderer.getRenderType(
			SkullBlock.Types.PLAYER,
			ResolvableProfile(owner.gameProfile)
		)

		skinRenderTypesByOwner[ownerUuid] = skinRenderType
		return skinRenderType
	}

	override fun render(
		entity: SupportSlime,
		entityYaw: Float,
		partialTicks: Float,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int
	) {

		val skinRenderType = getPlayerSkinRenderType(entity) ?: return

		poseStack.withPose {
			val scale = entity.size.toFloat()

			poseStack.translate(-scale / 2.0, 0.0, -scale / 2.0)
			poseStack.scale(scale, scale, scale)

			val lerpedRot = 180 + Mth.rotLerp(partialTicks, entity.yRotO, entity.yRot)

			SkullBlockRenderer.renderSkull(
				null,
				lerpedRot,
				0f,
				poseStack,
				buffer,
				packedLight,
				skullModel,
				skinRenderType
			)
		}
	}

	override fun getTextureLocation(entity: SupportSlime): ResourceLocation {
		return SlimeRenderer.SLIME_LOCATION
	}

}