package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.genetics_resequenced.capability.GenesCapability.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
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

	override fun getMaxDamage(stack: ItemStack): Int = Mth.ceil(getConfiguredMaxDamage())
	override fun getDamage(stack: ItemStack): Int {
		return Mth.ceil(ItemStackNbt.getFloat(stack, DAMAGE_USED, 0f))
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
		val maxDamage = getConfiguredMaxDamage()
		val damageUsed = ItemStackNbt.getFloat(pStack, DAMAGE_USED, 0f)
		val damageRemaining = maxDamage - damageUsed
		pTooltipComponents.add(
			Component.literal("${damageRemaining.toInt()}/${maxDamage.toInt()}").withStyle(ChatFormatting.GRAY)
		)
	}

	companion object {
		private const val DAMAGE_USED = "genetics_resequenced:dragon_health_crystal_damage"

		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
		}

		fun handleIncomingDamage(event: LivingDamageEvent) {
			val entity = event.entity

			if (event.amount <= 0f) return
			if (entity.isClientSide) return
			if (!entity.hasGene(ModGenes.ENDER_DRAGON_HEALTH)) return

			val crystal = if (entity is Player) {
				entity.inventory.items.firstOrNull { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
					?: entity.handSlots.firstOrNull { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
			} else {
				entity.handSlots.firstOrNull { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
			}

			if (crystal == null) return

			val maxDamage = getConfiguredMaxDamage()
			val currentDamage = ItemStackNbt.getFloat(crystal, DAMAGE_USED, 0f)
			val amountToRemove = minOf(event.amount, (maxDamage - currentDamage).toFloat())

			event.amount -= amountToRemove

			val newStackDamage = currentDamage + amountToRemove
			ItemStackNbt.putFloat(crystal, DAMAGE_USED, newStackDamage)

			if (newStackDamage >= maxDamage) {
				crystal.shrink(1)
				entity.broadcastBreakEvent(entity.usedItemHand)
			}

			if (event.amount < 0f) event.amount = 0f
		}

		private fun getConfiguredMaxDamage(): Double {
			return ServerConfig.CONFIG.dragonHealthCrystalMaxDamage.get()
		}
	}

}