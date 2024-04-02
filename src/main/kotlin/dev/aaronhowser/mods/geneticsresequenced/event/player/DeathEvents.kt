package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapabilityProvider
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
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
        if (event.original.level.isClientSide) return

        if (event.isWasDeath) {
            event.original.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { oldGenes ->
                event.entity.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).ifPresent { newGenes ->
                    newGenes.setGeneList(oldGenes.getGeneList())
                }
            }
        }
    }

    @SubscribeEvent(
        priority = EventPriority.HIGHEST    //So that graves etc don't dupe contents if you have the save inventory gene
    )
    fun onPlayerDeath(event: LivingDeathEvent) {

        val entity = event.entity

        if (entity.level.isClientSide) return
        if (entity !is Player) return

        handleKeepInventory(entity)
    }

    private val playerInventoryMap = mutableMapOf<Player, List<ItemStack>>()

    private fun handleKeepInventory(player: Player) {
        if (player.level.gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)) return

        // If they're dying, save their inventory to the map and then clear it so graves etc don't dupe it
        // If they're respawning, give them all the items in the saved map and remove them from the map
        val playerIsRespawning = playerInventoryMap.containsKey(player)

        if (playerIsRespawning) {
            playerInventoryMap[player]?.forEach { player.addItem(it) }
            playerInventoryMap.remove(player)
        } else {
            if (player.getGenes()?.hasGene(EnumGenes.SAVE_INVENTORY) != true) return
            playerInventoryMap[player] = player.inventory.items.toList()
            player.inventory.clearContent()
        }
    }

    @SubscribeEvent
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        if (event.entity.level.isClientSide) return

        handleKeepGenesOnDeath(event)
        handleKeepInventory(event.entity)
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