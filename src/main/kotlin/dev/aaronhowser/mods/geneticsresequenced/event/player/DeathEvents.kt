package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer.Companion.removeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object DeathEvents {

    @SubscribeEvent(
        priority = EventPriority.HIGHEST    //So that graves etc don't dupe contents if you have the save inventory gene
    )
    fun keepInventory(event: LivingDeathEvent) {
        if (event.isCanceled) return
        DeathGenes.handleKeepInventory(event.entity)
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        DeathGenes.handleKeepInventory(event.entity)

        removeNegativeGenesOnDeath(event)
    }

    private fun removeNegativeGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.entity
        val playerGenes = player.genes

        if (playerGenes.isEmpty()) return

        val negativeGenes = playerGenes.filter { it.isNegative }
        if (negativeGenes.isEmpty()) return

        val component = Component
            .translatable("message.geneticsresequenced.death_negative_gene_removal")
            .withColor(ChatFormatting.GRAY)

        player.sendSystemMessage(component)
        player.removeGenes(*negativeGenes.toTypedArray())
    }

}