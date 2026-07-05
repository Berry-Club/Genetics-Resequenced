package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getFirstItemStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
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

	override fun isDamageable(stack: ItemStack): Boolean = true
	override fun isBarVisible(stack: ItemStack): Boolean = getDamage(stack) > 0

	override fun getMaxDamage(stack: ItemStack): Int = Mth.ceil(getMaxDamage())
	override fun getDamage(stack: ItemStack): Int {
		val usedDamage = stack.getOrDefault(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0.0)
		return Mth.ceil(usedDamage)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipDisplay: TooltipDisplay,
		tooltipComponents: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val maxDamage = getMaxDamage()
		val currentDamage = stack.getOrDefault(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0.0)
		val remainingHealth = maxDamage - currentDamage

		tooltipComponents.accept(
			Component.literal(
				"${Mth.ceil(remainingHealth)} / ${Mth.ceil(maxDamage)}"
			).withStyle(ChatFormatting.GRAY)
		)
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.repairable(Items.END_CRYSTAL)
				.component(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0.0)
		}

		fun handleIncomingDamage(event: LivingDamageEvent.Pre) {
			val entity = event.entity

			if (event.container.newDamage <= 0f) return
			if (entity.isClientSide) return
			if (!entity.hasGene(ModGenes.ENDER_DRAGON_HEALTH)) return

			val crystalStack = if (entity is Player) {
				entity.getFirstItemStack { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
			} else {
				listOf(entity.mainHandItem, entity.offhandItem)
					.firstOrNull { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
			}

			if (crystalStack == null) return

			val maxDamage = getMaxDamage()

			val currentDamage = crystalStack.getOrDefault(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0.0)
			val damageLeft = maxDamage - currentDamage

			val amountToRemove = minOf(event.container.newDamage, damageLeft.toFloat())
			event.container.newDamage -= amountToRemove

			val newStackDamage = currentDamage + amountToRemove
			crystalStack.set(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, newStackDamage)
			if (newStackDamage >= maxDamage) {
				crystalStack.hurtAndBreak(1, entity, entity.getEquipmentSlotForItem(crystalStack))
			}

			if (event.container.newDamage < 0f) {
				event.container.newDamage = 0f
			}
		}

		fun getMaxDamage(): Double = ServerConfig.CONFIG.dragonHealthCrystalMaxDamage.get()
	}

}
