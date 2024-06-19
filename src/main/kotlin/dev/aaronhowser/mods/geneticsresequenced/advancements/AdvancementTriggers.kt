package dev.aaronhowser.mods.geneticsresequenced.advancements

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
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
        if (wasAdded) getAnyGeneAdvancement(player)

    }

    private fun getAnyGeneAdvancement(player: ServerPlayer) {
        val advancement = player.server.advancements.getAdvancement(OtherUtil.modResource("guide/get_gene")) ?: return
        completeAdvancement(player, advancement)
    }

}