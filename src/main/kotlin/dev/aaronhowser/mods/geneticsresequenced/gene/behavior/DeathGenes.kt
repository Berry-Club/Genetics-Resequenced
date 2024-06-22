package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameRules
import net.neoforged.fml.ModList
import java.util.*

object DeathGenes {

    private val playerInventoryMap: MutableMap<UUID, List<ItemStack>> = mutableMapOf()

    // TODO: Test with grave mods
    // TODO: Probably voids items if the server ends between death and respawn. Maybe drop items in the world if that happens?
    fun handleKeepInventory(player: LivingEntity) {
        if (!ModGenes.keepInventory.isActive) return

        if (player !is Player) return

        player.level().apply {
            if (isClientSide) return
            if (gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)) return
            if (levelData.isHardcore) return
        }

        // If they're dying, save their inventory to the map and then clear it so graves etc don't dupe it
        // If they're respawning, give them all the items in the saved map and remove them from the map
        val playerIsRespawning = playerInventoryMap.containsKey(player.uuid)

        //TODO: Implement this
        val curiosIsLoaded = ModList.get().isLoaded("curios")

        if (playerIsRespawning) {
            val items = playerInventoryMap[player.uuid] ?: return
            items.forEach { itemStack: ItemStack ->
                if (!player.inventory.add(itemStack)) {
                    player.drop(itemStack, true)
                }
            }

            playerInventoryMap.remove(player.uuid)

//            if (curiosIsLoaded) CuriosKeepInventory.loadPlayerCurios(player)
        } else {
            if (!player.hasGene(ModGenes.keepInventory)) return

            val allItems = player.inventory.items + player.inventory.armor + player.inventory.offhand
            val filtered = allItems.filter { !it.isEmpty }.map { it.copy() }

            playerInventoryMap[player.uuid] = filtered
            player.inventory.clearContent()

//            if (curiosIsLoaded) CuriosKeepInventory.savePlayerCurios(player)
        }
    }

}