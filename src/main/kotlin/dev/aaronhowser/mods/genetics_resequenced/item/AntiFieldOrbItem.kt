package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.ChatFormatting
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

	override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
		if (!level.isClientSide) {
			toggleEnabled(player.getItemInHand(hand))
		}

		return InteractionResult.SUCCESS
	}

	override fun isFoil(pStack: ItemStack): Boolean = isEnabled(pStack)

	override fun appendHoverText(
		itemStack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val componentString = if (isEnabled(itemStack)) {
			ModTooltipLang.ACTIVE
		} else {
			ModTooltipLang.INACTIVE
		}

		builder.accept(
			componentString
				.toComponent()
				.withStyle(ChatFormatting.GRAY)
		)
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.IS_ACTIVE, false)
		}

		private fun isEnabled(itemStack: ItemStack): Boolean {
			return itemStack.getOrDefault(ModDataComponents.IS_ACTIVE, false)
		}

		private fun toggleEnabled(itemStack: ItemStack) {
			itemStack.set(
				ModDataComponents.IS_ACTIVE,
				!isEnabled(itemStack)
			)
		}

		fun isActiveForPlayer(player: Player): Boolean {
			return player.inventory.contains { it.isHolder(ModItems.ANTI_FIELD_ORB) && isEnabled(it) }
		}
	}

}