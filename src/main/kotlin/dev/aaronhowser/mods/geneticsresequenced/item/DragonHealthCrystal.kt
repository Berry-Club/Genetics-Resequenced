package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getFirstItemStack
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
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
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent

class DragonHealthCrystal(properties: Properties) : Item(properties) {

	override fun getBreakingSound(): SoundEvent = SoundEvents.ENDER_DRAGON_HURT

	override fun isDamageable(stack: ItemStack): Boolean = true
	override fun isBarVisible(stack: ItemStack): Boolean = getDamage(stack) > 0

	override fun getMaxDamage(stack: ItemStack): Int = Mth.ceil(getMaxDamage())
	override fun getDamage(stack: ItemStack): Int {
		val usedDamage = stack.getOrDefault(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0.0)
		return Mth.ceil(usedDamage)
	}

	override fun isValidRepairItem(stack: ItemStack, repairCandidate: ItemStack): Boolean {
		return repairCandidate.item === Items.END_CRYSTAL
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val maxDamage = getMaxDamage()
		val currentDamage = stack.getOrDefault(ModDataComponents.DRAGON_HEALTH_CRYSTAL_DAMAGE, 0.0)
		val remainingHealth = maxDamage - currentDamage

		tooltipComponents.add(
			Component.literal(
				"${Mth.ceil(remainingHealth)} / ${Mth.ceil(maxDamage)}"
			).withStyle(ChatFormatting.GRAY)
		)
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
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
				entity.handSlots.firstOrNull { it.isItem(ModItems.DRAGON_HEALTH_CRYSTAL) }
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