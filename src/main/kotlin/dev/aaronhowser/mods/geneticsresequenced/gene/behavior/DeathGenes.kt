package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.attachment.GeneCooldowns
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.clearSavedInventory
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.getSavedInventory
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory.Companion.saveInventory
import dev.aaronhowser.mods.geneticsresequenced.compatibility.curios.KeepCurioInventory
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModAdvancementSubProvider
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.neoforged.fml.ModList
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent
import net.neoforged.neoforge.event.level.ExplosionEvent
import java.util.*

object DeathGenes {

	//TODO: Test with grave mods
	fun saveInventory(player: Player) {
		val level = player.level()
		if (level.isClientSide
			|| level.gameRules.getBoolean(GameRules.RULE_KEEPINVENTORY)
			|| level.levelData.isHardcore
		) return

		if (!player.hasGene(ModGenes.KEEP_INVENTORY)) return

		val playerItems =
			(player.inventory.items + player.inventory.armor + player.inventory.offhand).filter { !it.isEmpty }

		player.saveInventory(playerItems)

		val curiosIsLoaded = ModList.get().isLoaded("curios")
		if (curiosIsLoaded) {
			KeepCurioInventory.saveCurios(player)
		}

		player.inventory.clearContent()
	}

	fun returnInventory(player: Player) {
		val items = player.getSavedInventory()
		if (items.isEmpty()) return

		items.forEach { itemStack: ItemStack ->
			if (!player.inventory.add(itemStack)) {
				val itemEntity = ItemEntity(player.level(), player.x, player.y, player.z, itemStack)
				player.level().addFreshEntity(itemEntity)
			}
		}

		player.clearSavedInventory()
	}

	fun handleEmeraldHeart(event: LivingDeathEvent) {
		val entity = event.entity
		if (!entity.hasGene(ModGenes.EMERALD_HEART)) return

		if (entity !is Player) {
			val itemEntity = ItemEntity(entity.level(), entity.x, entity.y, entity.z, ItemStack(Items.EMERALD, 1))
			entity.level().addFreshEntity(itemEntity)
			return
		}

		val putOnCooldown = GeneCooldowns.addCooldown(
			entity,
			ModGenes.EMERALD_HEART,
			ServerConfig.CONFIG.emeraldHeartCooldown.get()
		)

		if (!putOnCooldown) return

		entity.inventory.add(ItemStack(Items.EMERALD, 1))
	}

	private val RECENTLY_EXPLODED_ENTITIES: MutableSet<UUID> = mutableSetOf()

	private const val GUNPOWDER_REQUIRED = 5
	private const val EXPLOSION_STRENGTH = 3f

	fun handleExplosiveExit(event: LivingDeathEvent) {
		val entity = event.entity
		if (!entity.hasGene(ModGenes.EXPLOSIVE_EXIT)) return

		val shouldExplode = if (entity !is Player) {
			true
		} else {
			val amountGunpowder = entity.inventory.items.sumOf { if (it.item == Items.GUNPOWDER) it.count else 0 }
			amountGunpowder >= GUNPOWDER_REQUIRED
		}

		if (!shouldExplode) return

		RECENTLY_EXPLODED_ENTITIES.add(entity.uuid)

		entity.level().explode(
			entity,
			entity.x,
			entity.y,
			entity.z,
			EXPLOSION_STRENGTH,
			Level.ExplosionInteraction.NONE // What the heck does this do
		)

		RECENTLY_EXPLODED_ENTITIES.remove(entity.uuid)

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
		val exploderUuid = event.explosion.directSourceEntity?.uuid
		if (exploderUuid !in RECENTLY_EXPLODED_ENTITIES) return

		event.affectedEntities.removeAll { it !is LivingEntity }
		event.affectedBlocks.clear()
	}

	fun handleSlimyDeath(event: LivingDeathEvent) {
		if (event.isCanceled) return

		val entity = event.entity
		if (!entity.hasGene(ModGenes.SLIMY_DEATH)) return

		val putOnCooldown = GeneCooldowns.addCooldown(
			entity,
			ModGenes.SLIMY_DEATH,
			ServerConfig.CONFIG.slimyDeathCooldown.get()
		)

		if (!putOnCooldown) return

		val amount = entity.random.nextRange(3, 6)

		repeat(amount) {
			val supportSlime = SupportSlime(entity.level(), entity.uuid)

			val randomNearbyPosition = entity.position().add(
				entity.random.nextRange(-1.0, 1.0),
				0.0,
				entity.random.nextRange(-1.0, 1.0)
			)

			supportSlime.moveTo(randomNearbyPosition.x, randomNearbyPosition.y, randomNearbyPosition.z)
			entity.level().addFreshEntity(supportSlime)
		}

		event.isCanceled = true
		entity.health = entity.maxHealth * ServerConfig.CONFIG.slimyDeathHealthMultiplier.get().toFloat()

		if (entity is ServerPlayer) {
			AdvancementTriggers.completeAdvancement(entity, ModAdvancementSubProvider.TRIGGER_SLIMY_DEATH)
		}
	}

	fun handleExperienced(event: LivingExperienceDropEvent) {
		if (ModGenes.EXPERIENCED.isDisabled(event.entity.registryAccess())) return

		val entity = event.entity
		val multiplier = entity.getAttributeValue(ModAttributes.XP_DROP_MULTIPLIER)
		event.droppedExperience = Mth.ceil(event.droppedExperience * multiplier)
	}

}