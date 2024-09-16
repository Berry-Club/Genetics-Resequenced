package dev.aaronhowser.mods.geneticsresequenced.advancement

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

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

    fun decryptDnaAdvancement(player: ServerPlayer, stack: ItemStack) {
        if (stack.item != ModItems.DNA_HELIX.get()) return
        if (!DnaHelixItem.hasGene(stack)) return

        val advancement =
            player.server.advancements.get(OtherUtil.modResource("guide/decrypt_dna")) ?: return
        completeAdvancement(player, advancement)
    }

    fun geneAdvancements(player: ServerPlayer, geneHolder: Holder<Gene>, wasAdded: Boolean) {
        if (wasAdded) {
            getAnyGeneAdvancement(player)

            when (geneHolder) {
                ModGenes.CRINGE -> getCringeGeneAdvancement(player)
                ModGenes.FLIGHT -> getFlightGeneAdvancement(player)
                ModGenes.SCARE_SPIDERS -> getAllScareGenes(player)
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

        val advancement =
            player.server.advancements.get(OtherUtil.modResource("guide/get_all_scare_genes")) ?: return
        completeAdvancement(player, advancement)
    }

    private fun getFlightGeneAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.get(OtherUtil.modResource("guide/get_flight"))
            ?: return
        completeAdvancement(player, advancement)
    }

    fun slimyDeathAdvancement(player: ServerPlayer) {
        val advancement =
            player.server.advancements.get(OtherUtil.modResource("guide/trigger_slimy_death"))
                ?: return
        completeAdvancement(player, advancement)
    }

    private fun getCringeGeneAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.get(OtherUtil.modResource("guide/get_cringe"))
            ?: return
        completeAdvancement(player, advancement)
    }

    private fun getAnyGeneAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.get(OtherUtil.modResource("guide/get_gene"))
            ?: return
        completeAdvancement(player, advancement)
    }

    fun getMilkedAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.get(OtherUtil.modResource("guide/get_milked"))
            ?: return
        completeAdvancement(player, advancement)
    }

    fun blackDeath(player: ServerPlayer, stack: ItemStack) {

        if (stack.item == ModItems.DNA_HELIX.get() || stack.item == ModItems.PLASMID.get()) {
            if (DnaHelixItem.getGene(stack) != ModGenes.BLACK_DEATH.get()) return
        } else if (stack.isSyringe()) {
            if (!SyringeItem.getGenes(stack).contains(ModGenes.BLACK_DEATH.get())) return
        } else return

        val advancement =
            player.server.advancements.get(OtherUtil.modResource("guide/black_death")) ?: return
        completeAdvancement(player, advancement)
    }


}