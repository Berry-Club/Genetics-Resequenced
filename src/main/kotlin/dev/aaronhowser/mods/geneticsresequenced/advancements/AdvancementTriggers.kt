package dev.aaronhowser.mods.geneticsresequenced.advancements

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.advancements.Advancement
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

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

    fun decryptDnaAdvancement(player: ServerPlayer, stack: ItemStack) {
        if (stack.item != ModItems.DNA_HELIX.get()) return
        if (stack.getGene() == null) return

        val advancement =
            player.server.advancements.getAdvancement(OtherUtil.modResource("guide/decrypt_dna")) ?: return
        completeAdvancement(player, advancement)
    }

    fun geneAdvancements(player: ServerPlayer, gene: Gene, wasAdded: Boolean) {
        if (wasAdded) {
            getAnyGeneAdvancement(player)

            when (gene) {
                ModGenes.cringe -> getCringeGeneAdvancement(player)
                ModGenes.flight -> getFlightGeneAdvancement(player)
                ModGenes.scareSpiders -> getAllScareGenes(player)
            }

        }
    }

    private fun getAllScareGenes(player: ServerPlayer) {
        val genes =
            listOf(ModGenes.scareSpiders, ModGenes.scareCreepers, ModGenes.scareSkeletons, ModGenes.scareZombies)
        if (genes.any { !player.hasGene(it) }) return

        val advancement =
            player.server.advancements.getAdvancement(OtherUtil.modResource("guide/get_all_scare_genes")) ?: return
        completeAdvancement(player, advancement)
    }

    private fun getFlightGeneAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.getAdvancement(OtherUtil.modResource("guide/get_flight")) ?: return
        completeAdvancement(player, advancement)
    }

    fun slimyDeathAdvancement(player: ServerPlayer) {
        val advancement =
            player.server.advancements.getAdvancement(OtherUtil.modResource("guide/trigger_slimy_death")) ?: return
        completeAdvancement(player, advancement)
    }

    private fun getCringeGeneAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.getAdvancement(OtherUtil.modResource("guide/get_cringe")) ?: return
        completeAdvancement(player, advancement)
    }

    private fun getAnyGeneAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.getAdvancement(OtherUtil.modResource("guide/get_gene")) ?: return
        completeAdvancement(player, advancement)
    }

    fun getMilkedAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.getAdvancement(OtherUtil.modResource("guide/get_milked")) ?: return
        completeAdvancement(player, advancement)
    }

    fun blackDeath(player: ServerPlayer, stack: ItemStack) {

        if (stack.item == ModItems.DNA_HELIX.get() || stack.item == ModItems.PLASMID.get()) {
            if (stack.getGene() != ModGenes.blackDeath) return
        } else if (stack.isSyringe()) {
            if (!SyringeItem.getGenes(stack).contains(ModGenes.blackDeath)) return
        } else return

        val advancement =
            player.server.advancements.getAdvancement(OtherUtil.modResource("guide/black_death")) ?: return
        completeAdvancement(player, advancement)
    }

}