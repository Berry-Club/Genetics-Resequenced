package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules

object DeathGenes {

    private val playerInventoryMap = mutableMapOf<Player, List<ItemStack>>()

    //TODO: Test with grave mods
    fun handleKeepInventory(player: Player) {
        if (player.level.gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)) return

        // If they're dying, save their inventory to the map and then clear it so graves etc don't dupe it
        // If they're respawning, give them all the items in the saved map and remove them from the map
        val playerIsRespawning = playerInventoryMap.containsKey(player)

        if (playerIsRespawning) {
            val items = playerInventoryMap[player] ?: return

            items.forEach { itemStack: ItemStack ->
                if (!player.inventory.add(itemStack)) {
                    player.drop(itemStack, true)
                }
            }

            playerInventoryMap.remove(player)
        } else {
            if (player.getGenes()?.hasGene(EnumGenes.KEEP_INVENTORY) != true) return
            playerInventoryMap[player] = player.inventory.items + player.inventory.armor + player.inventory.offhand
            player.inventory.clearContent()
        }
    }

}