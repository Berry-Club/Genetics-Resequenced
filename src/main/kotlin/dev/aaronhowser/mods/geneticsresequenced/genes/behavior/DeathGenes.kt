package dev.aaronhowser.mods.geneticsresequenced.genes.behavior

import dev.aaronhowser.mods.geneticsresequenced.advancements.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.curios.CuriosKeepInventory
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.entities.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.util.GeneCooldown
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Explosion
import net.minecraft.world.level.GameRules
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.level.ExplosionEvent
import net.minecraftforge.fml.ModList
import java.util.*
import kotlin.random.Random

object DeathGenes {

    private val playerInventoryMap: MutableMap<UUID, List<ItemStack>> = mutableMapOf()

    // TODO: Test with grave mods
    // TODO: Probably voids items if the server ends between death and respawn. Maybe drop items in the world if that happens?
    fun handleKeepInventory(player: LivingEntity) {
        if (!ModGenes.keepInventory.isActive) return

        if (player !is Player) return

        if (player.level.isClientSide) return
        if (player.level.gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)) return
        if (player.level.levelData.isHardcore) return

        // If they're dying, save their inventory to the map and then clear it so graves etc don't dupe it
        // If they're respawning, give them all the items in the saved map and remove them from the map
        val playerIsRespawning = playerInventoryMap.containsKey(player.uuid)

        val curiosIsLoaded = ModList.get().isLoaded("curios")

        if (playerIsRespawning) {
            val items = playerInventoryMap[player.uuid] ?: return
            items.forEach { itemStack: ItemStack ->
                if (!player.inventory.add(itemStack)) {
                    player.drop(itemStack, true)
                }
            }

            playerInventoryMap.remove(player.uuid)

            if (curiosIsLoaded) CuriosKeepInventory.loadPlayerCurios(player)
        } else {
            if (!player.hasGene(ModGenes.keepInventory)) return

            val allItems = player.inventory.items + player.inventory.armor + player.inventory.offhand
            val filtered = allItems.filter { !it.isEmpty }.map { it.copy() }

            playerInventoryMap[player.uuid] = filtered
            player.inventory.clearContent()

            if (curiosIsLoaded) CuriosKeepInventory.savePlayerCurios(player)
        }
    }

    private val emeraldHeartCooldown = GeneCooldown(
        ModGenes.emeraldHeart,
        ServerConfig.emeraldHeartCooldown.get()
    )

    fun handleEmeraldHeart(event: LivingDeathEvent) {
        if (!ModGenes.emeraldHeart.isActive) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.emeraldHeart)) return

        if (entity !is Player) {
            val itemEntity = ItemEntity(entity.level, entity.x, entity.y, entity.z, ItemStack(Items.EMERALD, 1))
            entity.level.addFreshEntity(itemEntity)
            return
        }

        val wasNotOnCooldown = emeraldHeartCooldown.add(entity)

        if (!wasNotOnCooldown) {
            entity.sendSystemMessage(Component.translatable("message.geneticsresequenced.emerald_heart.cooldown"))
            return
        }

        entity.inventory.add(ItemStack(Items.EMERALD, 1))
    }

    private val recentlyExplodedEntities: MutableSet<UUID> = mutableSetOf()

    private const val GUNPOWDER_REQUIRED = 5
    private const val EXPLOSION_STRENGTH = 3f
    fun handleExplosiveExit(event: LivingDeathEvent) {
        if (!ModGenes.explosiveExit.isActive) return

        val entity = event.entity
        if (!entity.hasGene(ModGenes.explosiveExit)) return

        val shouldExplode = if (entity !is Player) {
            true
        } else {
            val amountGunpowder = entity.inventory.items.sumOf { if (it.item == Items.GUNPOWDER) it.count else 0 }
            amountGunpowder >= GUNPOWDER_REQUIRED
        }

        if (!shouldExplode) return

        recentlyExplodedEntities.add(entity.uuid)

        entity.level.explode(
            entity,
            entity.x,
            entity.y,
            entity.z,
            EXPLOSION_STRENGTH,
            Explosion.BlockInteraction.NONE
        )

        recentlyExplodedEntities.remove(entity.uuid)

        if (entity is Player) {
            var amountGunpowderRemoved = 0
            for (stack in entity.inventory.items) {
                if (stack.item != Items.GUNPOWDER) continue

                while (stack.count > 0 && amountGunpowderRemoved < GUNPOWDER_REQUIRED) {
                    stack.shrink(1)
                    amountGunpowderRemoved++
                }

                if (amountGunpowderRemoved >= GUNPOWDER_REQUIRED) break
            }
        }

    }

    fun explosiveExitDetonation(event: ExplosionEvent.Detonate) {
        if (!ModGenes.explosiveExit.isActive) return
        if (event.isCanceled) return

        val exploderUuid = event.explosion.exploder?.uuid
        if (exploderUuid !in recentlyExplodedEntities) return

        event.affectedEntities.removeAll { it !is LivingEntity }
        event.affectedBlocks.clear()
    }

    private val slimyDeathCooldown = GeneCooldown(
        ModGenes.slimyDeath,
        ServerConfig.slimyDeathCooldown.get()
    )

    fun handleSlimyDeath(event: LivingDeathEvent) {
        if (!ModGenes.slimyDeath.isActive) return
        if (event.isCanceled) return

        val entity: LivingEntity = event.entity
        if (!entity.hasGene(ModGenes.slimyDeath)) return

        val newlyUsed = slimyDeathCooldown.add(entity)
        if (!newlyUsed) return

        val amount = Random.nextInt(1, 4)

        repeat(amount) {
            val supportSlime = SupportSlime(entity.level, entity.uuid)

            val randomNearbyPosition = entity.position().add(
                Random.nextDouble(-1.0, 1.0),
                0.0,
                Random.nextDouble(-1.0, 1.0)
            )

            supportSlime.moveTo(randomNearbyPosition.x, randomNearbyPosition.y, randomNearbyPosition.z)
            entity.level.addFreshEntity(supportSlime)
        }

        event.isCanceled = true
        entity.health = entity.maxHealth * ServerConfig.slimyDeathHealthMultiplier.get().toFloat()

        if (entity is ServerPlayer) {
            AdvancementTriggers.slimyDeathAdvancement(entity)
        }

    }

}