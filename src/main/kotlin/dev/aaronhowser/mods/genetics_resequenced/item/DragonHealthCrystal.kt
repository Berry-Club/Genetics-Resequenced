package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.genetics_resequenced.capability.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ItemStackNbt
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraftforge.event.entity.living.LivingDamageEvent

class DragonHealthCrystal(properties: Properties) : Item(properties) {

	override fun isDamageable(stack: ItemStack): Boolean = true
	override fun isBarVisible(stack: ItemStack): Boolean = getDamage(stack) > 0

	override fun getMaxDamage(stack: ItemStack): Int = Mth.ceil(MAX_DAMAGE)
	override fun getDamage(stack: ItemStack): Int {
		val damageRemaining = ItemStackNbt.getFloat(stack, DAMAGE_REMAINING, 0f)
		return Mth.ceil(MAX_DAMAGE - damageRemaining)
	}

	override fun isValidRepairItem(pStack: ItemStack, pRepairCandidate: ItemStack): Boolean {
		return pRepairCandidate.item === Items.END_CRYSTAL
	}

	override fun appendHoverText(
		pStack: ItemStack,
		pLevel: Level?,
		pTooltipComponents: MutableList<Component>,
		pIsAdvanced: TooltipFlag
	) {
		val maxDamage = MAX_DAMAGE
		val damageRemaining = ItemStackNbt.getFloat(pStack, DAMAGE_REMAINING, 0f)
		pTooltipComponents.add(
			Component.literal("${damageRemaining.toInt()}/${maxDamage.toInt()}").withStyle(ChatFormatting.GRAY)
		)
	}

	companion object {
		private const val DAMAGE_REMAINING = "genetics_resequenced:dragon_health_crystal_damage"

		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
		}

		const val MAX_DAMAGE = 1000f

		fun handleIncomingDamage(event: LivingDamageEvent) {
			val entity = event.entity

			if (event.amount <= 0f) return
			if (entity.isClientSide) return
			if (!entity.hasGene(ModGenes.ENDER_DRAGON_HEALTH)) return

			val heldStacks = entity.handSlots.toMutableSet()
			if (entity is Player) heldStacks += entity.inventory.items

			val healthCrystals = heldStacks.filter { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
			if (healthCrystals.isEmpty()) return

			for (crystal in healthCrystals) {
				val damageLeft = ItemStackNbt.getFloat(crystal, DAMAGE_REMAINING, 0f)
				val amountToRemove = minOf(event.amount, damageLeft)

				event.amount -= amountToRemove

				val newStackDamage = damageLeft - amountToRemove
				ItemStackNbt.putFloat(crystal, DAMAGE_REMAINING, newStackDamage)

				if (newStackDamage <= 0f) {
					crystal.shrink(1)
					entity.broadcastBreakEvent(entity.usedItemHand)
				}

				if (event.amount <= 0f) break
			}

			if (event.amount < 0f) event.amount = 0f
		}
	}

}