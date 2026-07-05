package dev.aaronhowser.mods.genetics_resequenced.client.renderer

import com.google.common.reflect.TypeToken
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.config.ClientConfig
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.util.context.ContextKey
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.client.event.RenderLivingEvent
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent
import org.joml.SimplexNoise

object GeneRenderChanges {

	private val HAS_CRINGE = ContextKey<Boolean>(GeneticsResequenced.modResource("has_cringe"))
	private val CLINGS_TO_CEILING = ContextKey<Boolean>(GeneticsResequenced.modResource("clings_to_ceiling"))

	fun registerRenderStateModifiers(event: RegisterRenderStateModifiersEvent) {
		event.registerEntityModifier(
			object : TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, *>>() {},
			::extractLivingEntityGeneState
		)
	}

	private fun extractLivingEntityGeneState(entity: LivingEntity, state: LivingEntityRenderState) {
		state.setRenderData(HAS_CRINGE, entity.hasGene(ModGenes.CRINGE))
		state.setRenderData(CLINGS_TO_CEILING, OtherGenes.shouldClingToCeiling(entity))
	}

	fun shakeFromCringe(event: RenderLivingEvent.Pre<*, *, *>) {
		if (!ClientConfig.CONFIG.doesCringeShake.get()) return

		val state = event.renderState
		if (!state.getRenderDataOrDefault(HAS_CRINGE, false)) return

		val shakeAmplitude = ClientConfig.CONFIG.cringeShakeAmplitude.get()
		val shakeSpeed = ClientConfig.CONFIG.cringeShakeSpeed.get().toFloat()

		val shake = state.ageInTicks * shakeSpeed

		val dx = SimplexNoise.noise(0f, shake) * shakeAmplitude
		val dy = SimplexNoise.noise(10f, shake) * shakeAmplitude
		val dz = SimplexNoise.noise(20f, shake) * shakeAmplitude

		event.poseStack.pushPose()
		event.poseStack.translate(dx, dy, dz)
	}

	fun shakeFromCringePost(event: RenderLivingEvent.Post<*, *, *>) {
		if (!ClientConfig.CONFIG.doesCringeShake.get()) return

		val state = event.renderState
		if (!state.getRenderDataOrDefault(HAS_CRINGE, false)) return

		event.poseStack.popPose()
	}

	fun spiderClimbFlip(event: RenderLivingEvent.Pre<*, *, *>) {
		val state = event.renderState

		if (state.getRenderDataOrDefault(CLINGS_TO_CEILING, false)) {
			val poseStack = event.poseStack
			poseStack.pushPose()
			poseStack.translate(0.0, state.boundingBoxHeight.toDouble(), 0.0)
			poseStack.scale(1.0f, -1.0f, 1.0f)
		}
	}

	fun spiderClimbFlipPost(event: RenderLivingEvent.Post<*, *, *>) {
		val state = event.renderState

		if (state.getRenderDataOrDefault(CLINGS_TO_CEILING, false)) {
			event.poseStack.popPose()
		}
	}

}
