package dev.aaronhowser.mods.geneticsresequenced.client.renderer

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.neoforged.neoforge.client.event.RenderLivingEvent
import org.joml.SimplexNoise

object GeneRenderChanges {

	fun shakeFromCringe(event: RenderLivingEvent.Pre<*, *>) {
		val entity = event.entity
		if (!entity.hasGene(ModGenes.CRINGE)) return

		val shakeAmplitude = 0.09
		val shakeSpeed = 20f

		val time = entity.tickCount + event.partialTick
		val shake = time * shakeSpeed

		val dx = SimplexNoise.noise(0f, shake) * shakeAmplitude
		val dy = SimplexNoise.noise(10f, shake) * shakeAmplitude
		val dz = SimplexNoise.noise(20f, shake) * shakeAmplitude

		event.poseStack.pushPose()
		event.poseStack.translate(dx, dy, dz)
	}

	fun shakeFromCringePost(event: RenderLivingEvent.Post<*, *>) {
		val entity = event.entity
		if (!entity.hasGene(ModGenes.CRINGE)) return

		event.poseStack.popPose()
	}

	fun spiderClimbFlip(event: RenderLivingEvent.Pre<*, *>) {
		val entity = event.entity

		if (OtherGenes.shouldClingToCeiling(entity)) {
			val poseStack = event.poseStack
			poseStack.pushPose()
			poseStack.translate(0.0, entity.bbHeight.toDouble(), 0.0)
			poseStack.scale(1.0f, -1.0f, 1.0f)
		}
	}

	fun spiderClimbFlipPost(event: RenderLivingEvent.Post<*, *>) {
		val entity = event.entity

		if (OtherGenes.shouldClingToCeiling(entity)) {
			event.poseStack.popPose()
		}
	}

}