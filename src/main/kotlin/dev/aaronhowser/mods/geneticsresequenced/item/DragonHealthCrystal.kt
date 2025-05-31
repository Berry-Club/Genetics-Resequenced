package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.isClient
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent

class DragonHealthCrystal : Item(
	Properties().component(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 1000f)
) {

	override fun getBreakingSound(): SoundEvent = SoundEvents.ENDER_DRAGON_HURT

	override fun isValidRepairItem(pStack: ItemStack, pRepairCandidate: ItemStack): Boolean {
		return pRepairCandidate.item === Items.END_CRYSTAL
	}

	companion object {
		fun handleIncomingDamage(event: LivingDamageEvent.Pre) {
			val enderDragonHealth = ModGenes.ENDER_DRAGON_HEALTH.getHolderOrThrow(event.entity.registryAccess())
			if (enderDragonHealth.isDisabled) return

			if (event.container.newDamage == 0f) return
			val entity = event.entity

			if (entity.isClient) return
			if (!entity.hasGene(ModGenes.ENDER_DRAGON_HEALTH)) return

			val heldStacks = entity.handSlots.toMutableSet()
			if (entity is Player) heldStacks += entity.inventory.items

			val healthCrystals = heldStacks.filter { it.`is`(ModItems.DRAGON_HEALTH_CRYSTAL) }
			if (healthCrystals.isEmpty()) return

			for (crystal in healthCrystals) {
				val damageLeft = crystal.get(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE) ?: continue
				val amountToRemove = minOf(event.container.newDamage, damageLeft)

				event.container.newDamage -= amountToRemove

				val newDamage = damageLeft - amountToRemove
				if (newDamage <= 0f) {
					crystal.shrink(1)
					entity.onEquippedItemBroken(crystal.item, entity.getEquipmentSlotForItem(crystal))
				}
			}

			if (event.container.newDamage < 0f) event.container.newDamage = 0f
		}
	}

}