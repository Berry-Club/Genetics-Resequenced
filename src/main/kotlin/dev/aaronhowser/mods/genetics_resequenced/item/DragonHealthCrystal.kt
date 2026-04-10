package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import java.util.function.Consumer

class DragonHealthCrystal(properties: Properties) : Item(properties) {

	override fun getBreakingSound(): SoundEvent = SoundEvents.ENDER_DRAGON_HURT

	override fun isDamageable(stack: ItemStack): Boolean = true
	override fun isBarVisible(stack: ItemStack): Boolean = getDamage(stack) > 0

	override fun getMaxDamage(stack: ItemStack): Int = Mth.ceil(MAX_DAMAGE)
	override fun getDamage(stack: ItemStack): Int {
		val damageRemaining = stack.getOrDefault(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0f)
		return Mth.ceil(MAX_DAMAGE - damageRemaining)
	}

	override fun isValidRepairItem(pStack: ItemStack, pRepairCandidate: ItemStack): Boolean {
		return pRepairCandidate.item === Items.END_CRYSTAL
	}

	override fun appendHoverText(
		itemStack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val maxDamage = MAX_DAMAGE
		val damageLeft = stack.getOrDefault(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0f)
		tooltipComponents.add(
			Component.literal("${damageLeft.toInt()}/${maxDamage.toInt()}").withStyle(ChatFormatting.GRAY)
		)
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, MAX_DAMAGE)
		}

		const val MAX_DAMAGE = 1000f

		fun handleIncomingDamage(event: LivingDamageEvent.Pre) {
			val entity = event.entity

			if (event.container.newDamage <= 0f) return
			if (entity.isClientSide) return
			if (!entity.hasGene(ModGenes.ENDER_DRAGON_HEALTH)) return

			val heldStacks = entity.handSlots.toMutableSet()
			if (entity is Player) heldStacks += entity.inventory.items

			val healthCrystals = heldStacks.filter { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
			if (healthCrystals.isEmpty()) return

			for (crystal in healthCrystals) {
				val damageLeft = crystal.get(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE) ?: continue
				val amountToRemove = minOf(event.container.newDamage, damageLeft)

				event.container.newDamage -= amountToRemove

				val newStackDamage = damageLeft - amountToRemove
				crystal.set(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, newStackDamage)
				if (newStackDamage <= 0f) {
					crystal.shrink(1)
					entity.onEquippedItemBroken(crystal.item, entity.getEquipmentSlotForItem(crystal))
				}

				if (event.container.newDamage <= 0f) break
			}

			if (event.container.newDamage < 0f) event.container.newDamage = 0f
		}
	}

}