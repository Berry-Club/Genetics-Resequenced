package dev.aaronhowser.mods.geneticsresequenced.events.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.default_genes.behavior.DeathGenes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object DeathEvents {

    @SubscribeEvent
    fun onPlayerCloned(event: PlayerEvent.Clone) {
        if (event.isWasDeath) {
            event.original.apply {
                reviveCaps()

                getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { oldGenes ->
                    event.entity.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { newGenes ->
                        newGenes.setGeneList(oldGenes.getGeneList())
                    }
                }

                invalidateCaps()
            }
        }
    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST    //So that graves etc don't dupe contents if you have the save inventory gene
    )
    fun keepInventory(event: LivingDeathEvent) {
        if (event.isCanceled) return
        DeathGenes.handleKeepInventory(event.entity)
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        handleKeepGenesOnDeath(event)
        removeNegativeGenesOnDeath(event)

        DeathGenes.handleKeepInventory(event.entity)
    }

    @SubscribeEvent
    fun clone(event: PlayerEvent.Clone) {
        if (!event.isWasDeath) return

        val original = event.original
        val originalGenes = original.getGenes() ?: return
        if (!originalGenes.hasGene(DefaultGenes.keepInventory)) return

        val originalInventory = original.inventory

        val new = event.entity

    }

    private fun handleKeepGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
        if (ServerConfig.keepGenesOnDeath.get()) return

        val player = event.entity
        val playerGenes = player.getGenes() ?: return

        if (playerGenes.getGeneList().isEmpty()) return

        val component = Component
            .translatable("message.geneticsresequenced.death_gene_removal")
            .withColor(ChatFormatting.GRAY)

        player.sendSystemMessage(component)
        playerGenes.removeAllGenes()
    }

    private fun removeNegativeGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
        val player = event.entity
        val playerGenes = player.getGenes() ?: return

        if (playerGenes.getGeneList().isEmpty()) return

        val negativeGenes = playerGenes.getGeneList().filter { it.isNegative }
        if (negativeGenes.isEmpty()) return

        val component = Component
            .translatable("message.geneticsresequenced.death_negative_gene_removal")
            .withColor(ChatFormatting.GRAY)

        player.sendSystemMessage(component)
        playerGenes.removeGenes(negativeGenes)
    }

}