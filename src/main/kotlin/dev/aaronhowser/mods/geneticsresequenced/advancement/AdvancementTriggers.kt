package dev.aaronhowser.mods.geneticsresequenced.advancement

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer

object AdvancementTriggers {

    private fun completeAdvancement(player: ServerPlayer, advancement: AdvancementHolder) {

        val progress = player.advancements.getOrStartProgress(advancement)
        if (progress.isDone) return

        val criteria = progress.remainingCriteria.iterator()

        while (criteria.hasNext()) {
            val criterion = criteria.next()
            player.advancements.award(advancement, criterion)
        }

    }

    fun geneAdvancements(player: ServerPlayer, geneHolder: Holder<Gene>, wasAdded: Boolean) {
        if (wasAdded) {
            getAnyGeneAdvancement(player)

            when {
                geneHolder.isGene(ModGenes.CRINGE) -> getCringeGeneAdvancement(player)
                geneHolder.isGene(ModGenes.FLIGHT) -> getFlightGeneAdvancement(player)
                geneHolder.isGene(ModGenes.SCARE_SPIDERS) -> getAllScareGenes(player)
            }
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

        val advancement = getAdvancement(player, "guide/get_all_scare_genes") ?: return
        completeAdvancement(player, advancement)
    }

    private fun getFlightGeneAdvancement(player: ServerPlayer) {
        val advancement = getAdvancement(player, "guide/get_flight") ?: return
        completeAdvancement(player, advancement)
    }

    fun slimyDeathAdvancement(player: ServerPlayer) {
        val advancement = getAdvancement(player, "guide/trigger_slimy_death") ?: return
        completeAdvancement(player, advancement)
    }

    private fun getCringeGeneAdvancement(player: ServerPlayer) {
        val advancement = getAdvancement(player, "guide/get_cringe") ?: return
        completeAdvancement(player, advancement)
    }

    private fun getAnyGeneAdvancement(player: ServerPlayer) {
        val advancement = getAdvancement(player, "guide/get_gene") ?: return
        completeAdvancement(player, advancement)
    }

    fun getMilkedAdvancement(player: ServerPlayer) {
        val advancement = getAdvancement(player, "guide/get_milked") ?: return
        completeAdvancement(player, advancement)
    }

    fun getAdvancement(player: ServerPlayer, advancementName: String): AdvancementHolder? =
        player.server.advancements.get(OtherUtil.modResource(advancementName))

}