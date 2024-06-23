package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.removeAllGenes
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
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

        handleKeepGenesOnDeath(event)
        removeNegativeGenesOnDeath(event)
    }

    private fun handleKeepGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
        if (ServerConfig.keepGenesOnDeath.get()) return

        val player = event.entity
        val playerGenes = player.genes

        if (playerGenes.isEmpty()) return

        val component = Component
            .translatable(ModLanguageProvider.Messages.DEATH_GENE_REMOVAL)
            .withColor(ChatFormatting.GRAY)

        player.sendSystemMessage(component)
        player.removeAllGenes()
    }

    private fun removeNegativeGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.entity
        val playerGenes = player.genes

        if (playerGenes.isEmpty()) return

        val negativeGenes = playerGenes.filter { it.isNegative }
        if (negativeGenes.isEmpty()) return

        val component = Component
            .translatable(ModLanguageProvider.Messages.DEATH_NEGATIVE_GENE_REMOVAL)
            .withColor(ChatFormatting.GRAY)

        player.sendSystemMessage(component)

        for (gene in negativeGenes) {
            player.removeGene(gene)
        }
    }

}