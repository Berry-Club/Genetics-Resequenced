package dev.aaronhowser.mods.geneticsresequenced.advancement

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModAdvancementSubProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

object AdvancementTriggers {

	fun geneAdvancements(
		player: ServerPlayer,
		geneHolder: Holder<Gene>,
		wasAdded: Boolean
	) {
		if (!wasAdded) return

		completeAdvancement(player, ModAdvancementSubProvider.GET_GENE)
		completeAllScareGenesAdvancement(player)

		val otherAdvancement = when {
			geneHolder.isGene(ModGenes.CRINGE) -> ModAdvancementSubProvider.GET_CRINGE
			geneHolder.isGene(ModGenes.FLIGHT) -> ModAdvancementSubProvider.GET_FLIGHT
			else -> null
		}

		if (otherAdvancement != null) {
			completeAdvancement(player, otherAdvancement)
		}
	}

	private fun completeAllScareGenesAdvancement(player: ServerPlayer) {
		val scareGeneKeys =
			listOf(
				ModGenes.SCARE_SPIDERS,
				ModGenes.SCARE_CREEPERS,
				ModGenes.SCARE_SKELETONS,
				ModGenes.SCARE_ZOMBIES
			)

		if (scareGeneKeys.any { !player.hasGene(it) }) return

		completeAdvancement(player, ModAdvancementSubProvider.GET_ALL_SCARE_GENES)
	}

	fun ResourceLocation.getAdvancement(player: ServerPlayer): AdvancementHolder? {
		return player.server.advancements.get(this)
	}

	fun completeAdvancement(player: ServerPlayer, advancementId: ResourceLocation) {
		val advancement = advancementId.getAdvancement(player) ?: return
		completeAdvancement(player, advancement)
	}

	fun completeAdvancement(player: ServerPlayer, advancement: AdvancementHolder) {
		val progress = player.advancements.getOrStartProgress(advancement)
		if (progress.isDone) return

		val criteria = progress.remainingCriteria.iterator()

		while (criteria.hasNext()) {
			val criterion = criteria.next()
			player.advancements.award(advancement, criterion)
		}
	}

}