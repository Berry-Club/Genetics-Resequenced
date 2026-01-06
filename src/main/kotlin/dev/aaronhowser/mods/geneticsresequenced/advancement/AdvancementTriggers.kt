package dev.aaronhowser.mods.geneticsresequenced.advancement

import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModAdvancementSubProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.advancements.Advancement
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

object AdvancementTriggers {

	private fun completeAdvancement(player: ServerPlayer, advancement: Advancement) {
		val progress = player.advancements.getOrStartProgress(advancement)
		if (progress.isDone) return

		val criteria = progress.remainingCriteria.iterator()

		while (criteria.hasNext()) {
			val criterion = criteria.next()
			player.advancements.award(advancement, criterion)
		}
	}

	fun geneAdvancements(player: ServerPlayer, geneHolder: Holder<Gene>, wasAdded: Boolean) {
		if (!wasAdded) return

		getAnyGeneAdvancement(player)

		when {
			geneHolder.isGene(ModGenes.CRINGE) -> getCringeGeneAdvancement(player)
			geneHolder.isGene(ModGenes.FLIGHT) -> getFlightGeneAdvancement(player)
			geneHolder.isGene(ModGenes.SCARE_SPIDERS) -> getAllScareGenes(player)
		}
	}

	private fun getAllScareGenes(player: ServerPlayer) {
		val scareGeneKeys =
			listOf(
				ModGenes.SCARE_SPIDERS,
				ModGenes.SCARE_CREEPERS,
				ModGenes.SCARE_SKELETONS,
				ModGenes.SCARE_ZOMBIES
			)

		if (scareGeneKeys.any { !player.hasGene(it) }) return

		val advancement = getAdvancement(player, ModAdvancementSubProvider.GET_ALL_SCARE_GENES) ?: return
		completeAdvancement(player, advancement)
	}

	private fun getFlightGeneAdvancement(player: ServerPlayer) {
		val advancement = getAdvancement(player, ModAdvancementSubProvider.GET_FLIGHT) ?: return
		completeAdvancement(player, advancement)
	}

	fun slimyDeathAdvancement(player: ServerPlayer) {
		val advancement = getAdvancement(player, ModAdvancementSubProvider.TRIGGER_SLIMY_DEATH) ?: return
		completeAdvancement(player, advancement)
	}

	private fun getCringeGeneAdvancement(player: ServerPlayer) {
		val advancement = getAdvancement(player, ModAdvancementSubProvider.GET_CRINGE) ?: return
		completeAdvancement(player, advancement)
	}

	private fun getAnyGeneAdvancement(player: ServerPlayer) {
		val advancement = getAdvancement(player, ModAdvancementSubProvider.GET_GENE) ?: return
		completeAdvancement(player, advancement)
	}

	fun getMilkedAdvancement(player: ServerPlayer) {
		val advancement = getAdvancement(player, ModAdvancementSubProvider.GET_MILKED) ?: return
		completeAdvancement(player, advancement)
	}

	fun getAdvancement(player: ServerPlayer, advancementId: ResourceLocation): Advancement? =
		player.server.advancements.getAdvancement(advancementId)

}