package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getFirstItemStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toggleUnit
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.level.Level
import java.util.function.Consumer

class AntiFieldOrbItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResult {
		val stack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			stack.toggleUnit(ModDataComponents.IS_ACTIVE)
		}

		return InteractionResult.SUCCESS
	}

	override fun isFoil(stack: ItemStack): Boolean {
		return isActive(stack)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipDisplay: TooltipDisplay,
		tooltipComponents: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val componentString = if (isActive(stack)) {
			ModTooltipLang.ACTIVE
		} else {
			ModTooltipLang.INACTIVE
		}

		tooltipComponents.accept(
			componentString
				.toGrayComponent()
		)
	}

	companion object {
		private fun isActive(itemStack: ItemStack): Boolean {
			return itemStack.has(ModDataComponents.IS_ACTIVE)
		}

		fun isActiveForPlayer(player: Player): Boolean {
			return player.hasItem { it.isItem(ModItems.ANTI_FIELD_ORB) && isActive(it) }
		}
	}

}
