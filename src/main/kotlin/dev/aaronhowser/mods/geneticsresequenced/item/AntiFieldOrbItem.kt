package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toggleUnit
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AntiFieldOrbItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack?> {
		if (!level.isClientSide) {
			val stack = player.getItemInHand(usedHand)
			stack.toggleUnit(ModDataComponents.IS_ACTIVE)
		}

		return super.use(level, player, usedHand)
	}

	override fun isFoil(stack: ItemStack): Boolean {
		return isActive(stack)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val componentString = if (isActive(stack)) {
			ModTooltipLang.ACTIVE
		} else {
			ModTooltipLang.INACTIVE
		}

		tooltipComponents.add(
			componentString
				.toComponent()
				.withStyle(ChatFormatting.GRAY)
		)
	}

	companion object {
		private fun isActive(itemStack: ItemStack): Boolean {
			return itemStack.has(ModDataComponents.IS_ACTIVE)
		}

		fun isActiveForPlayer(player: Player): Boolean {
			return player.inventory.items.any { it.isItem(ModItems.ANTI_FIELD_ORB) && isActive(it) }
		}
	}

}