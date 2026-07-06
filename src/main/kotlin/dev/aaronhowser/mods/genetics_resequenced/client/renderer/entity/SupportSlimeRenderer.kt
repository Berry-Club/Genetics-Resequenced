package dev.aaronhowser.mods.genetics_resequenced.client.renderer.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.genetics_resequenced.entity.SupportSlime
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.monster.slime.SlimeModel
import net.minecraft.client.model.`object`.skull.SkullModelBase
import net.minecraft.client.renderer.PlayerSkinRenderCache
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.MobRenderer
import net.minecraft.client.renderer.entity.state.SlimeRenderState
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth
import net.minecraft.world.item.component.ResolvableProfile
import net.minecraft.world.level.block.SkullBlock

class SupportSlimeRenderer(
	context: EntityRendererProvider.Context
) : MobRenderer<SupportSlime, SupportSlimeRenderer.State, SlimeModel>(
	context,
	SlimeModel(context.bakeLayer(ModelLayers.SLIME)),
	0.25f
) {

	private val skullModel: SkullModelBase =
		SkullBlockRenderer.createModel(context.modelSet, SkullBlock.Types.PLAYER)
			?: error("Missing player skull model")

	private val playerSkinRenderCache: PlayerSkinRenderCache = context.playerSkinRenderCache

	override fun createRenderState(): State {
		return State()
	}

	override fun extractRenderState(entity: SupportSlime, state: State, partialTicks: Float) {
		super.extractRenderState(entity, state, partialTicks)

		val ownerProfile = entity.ownerUuid
			?.let { ownerUuid -> entity.level().getPlayerByUUID(ownerUuid) }
			?.gameProfile
			?.let(ResolvableProfile::createResolved)

		state.ownerSkinRenderType = ownerProfile
			?.let(playerSkinRenderCache::getOrDefault)
			?.renderType()

		state.skullYaw = 180f - Mth.rotLerp(partialTicks, entity.yRotO, entity.yRot)
		state.squish = Mth.lerp(partialTicks, entity.oSquish, entity.squish)
		state.size = entity.size
	}

	override fun submit(
		state: State,
		poseStack: PoseStack,
		submitNodeCollector: SubmitNodeCollector,
		camera: CameraRenderState
	) {
		val skinRenderType = state.ownerSkinRenderType ?: return

		poseStack.withPose {
			poseStack.translate(0f, 0.5f, 0f)
			scale(state, poseStack)
			poseStack.mulPose(Axis.YP.rotationDegrees(state.skullYaw))

			SkullBlockRenderer.submitSkull(
				0f,
				poseStack,
				submitNodeCollector,
				state.lightCoords,
				skullModel,
				skinRenderType,
				state.outlineColor,
				null
			)
		}
	}

	override fun scale(state: State, poseStack: PoseStack) {
		val s = 0.999f
		poseStack.scale(s, s, s)
		poseStack.translate(0f, 0.001f, 0f)
		val size = state.size
		val ss = state.squish / (size * 0.5f + 1f)
		val w = 1f / (ss + 1f)
		poseStack.scale(w * size, 1f / w * size, w * size)
	}

	override fun getTextureLocation(state: State): Identifier {
		return SLIME_LOCATION
	}

	class State : SlimeRenderState() {
		var ownerSkinRenderType: RenderType? = null
		var skullYaw: Float = 0f
	}

	companion object {
		val SLIME_LOCATION: Identifier = Identifier.withDefaultNamespace("textures/entity/slime/slime.png")
	}

}
