package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
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

	override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		toggleEnabled(pPlayer.getItemInHand(pUsedHand))
		return super.use(pLevel, pPlayer, pUsedHand)
	}

	override fun isFoil(pStack: ItemStack): Boolean = isEnabled(pStack)

	override fun appendHoverText(
		pStack: ItemStack,
		pLevel: Level?,
		pTooltipComponents: MutableList<Component>,
		pIsAdvanced: TooltipFlag
	) {
		val componentString = if (isEnabled(pStack)) {
			ModTooltipLang.ACTIVE
		} else {
			ModTooltipLang.INACTIVE
		}

		pTooltipComponents.add(
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
			return player.inventory.items.any { it.isItem(ModItems.ANTI_FIELD_ORB) && isEnabled(it) }
		}
	}

}