package dev.aaronhowser.mods.geneticsresequenced.genebehavior

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.event.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraftforge.event.entity.living.LivingDeathEvent
import java.util.*

object DeathGenes {

    private val playerInventoryMap = mutableMapOf<Player, List<ItemStack>>()

    // TODO: Test with grave mods
    // TODO: Probably voids items if the server ends between death and respawn. Maybe drop items in the world if that happens?
    fun handleKeepInventory(player: LivingEntity) {

        if (player !is Player) return

        if (player.level.gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)) return

        if (player.level.levelData.isHardcore) return

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

    private const val EMERALD_HEART_COOLDOWN = 20 * 60 * 5      // 5 minutes, also TODO: Config

    // UUID because that's persistent across deaths and idk if Player is
    private val emeraldHeartCooldown = mutableSetOf<UUID>()

    fun handleEmeraldHeart(event: LivingDeathEvent) {

        val entity = event.entity
        if (entity.getGenes()?.hasGene(EnumGenes.EMERALD_HEART) != true) return

        if (entity !is Player) {
            val itemEntity = ItemEntity(entity.level, entity.x, entity.y, entity.z, ItemStack(Items.EMERALD, 1))
            entity.level.addFreshEntity(itemEntity)
            return
        }

        val entityUuid = entity.uuid

        val isNotOnCooldown = emeraldHeartCooldown.add(entityUuid)
        if (!isNotOnCooldown) {
            entity.sendSystemMessage(Component.literal("Emerald Heart is on cooldown!"))
            return
        }

        entity.inventory.add(ItemStack(Items.EMERALD, 1))

        ModScheduler.scheduleTaskInTicks(EMERALD_HEART_COOLDOWN) {
            emeraldHeartCooldown.remove(entityUuid)
        }
    }

}