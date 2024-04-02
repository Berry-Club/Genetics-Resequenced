package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object DeathEvents {

    @SubscribeEvent
    fun onPlayerCloned(event: PlayerEvent.Clone) {
        if (event.original.level.isClientSide) return
        if (!event.isWasDeath) return

        event.original.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { oldGenes ->
            event.original.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { newGenes ->
                newGenes.setGeneList(oldGenes.getGeneList())
            }
        }
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        if (event.entity.level.isClientSide) return
        handleKeepGenesOnDeath(event)
    }

    private fun handleKeepGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
        if (ServerConfig.keepGenesOnDeath.get()) return

        val player = event.entity
        val playerGenes = player.getGenes() ?: return

        if (playerGenes.getGeneList().isEmpty()) return

        val component = Component
            .literal("Genetics Resequenced")
            .withStyle {
                it
                    .withColor(ChatFormatting.DARK_PURPLE)
                    .withHoverEvent(
                        HoverEvent(
                            HoverEvent.Action.SHOW_TEXT, Component
                                .literal(playerGenes.getGeneList().joinToString(", "))
                        )
                    )
            }
            .append(
                Component
                    .literal(": Your genes have been removed on death.")
                    .withStyle {
                        it
                            .withColor(ChatFormatting.GRAY)
                    }
            )
        player.sendSystemMessage(component)

        playerGenes.removeAllGenes()
    }

}