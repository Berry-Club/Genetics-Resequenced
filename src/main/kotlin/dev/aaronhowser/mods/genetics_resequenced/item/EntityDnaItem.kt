package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.status
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModDataComponents
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import java.util.function.Consumer

open class EntityDnaItem(properties: Properties) : Item(properties) {

	override fun interactLivingEntity(
		stack: ItemStack,
		player: Player,
		interactionTarget: LivingEntity,
		usedHand: InteractionHand
	): InteractionResult {
		if (!player.isCreative) {
			return super.interactLivingEntity(stack, player, interactionTarget, usedHand)
		}

		val newStack = stack.copy()
		val setWorked = setEntityType(newStack, interactionTarget.type)

		if (!setWorked) {
			player.status(ModMessageLang.CANT_SET_ENTITY.toComponent())

			return InteractionResult.PASS
		}

		player.setItemInHand(usedHand, newStack)

		return InteractionResult.SUCCESS
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipDisplay: TooltipDisplay,
		tooltipComponents: Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val entityType = getEntityType(stack)
		if (entityType != null) {
			val component =
				ModTooltipLang.CELL_MOB
					.toComponent(entityType.description)
					.withStyle(ChatFormatting.GRAY)
			tooltipComponents.accept(component)
		} else {
			val component =
				ModTooltipLang.CELL_NO_MOB
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			tooltipComponents.accept(component)
		}

		if (ClientUtil.playerIsCreative()) {
			val component =
				ModTooltipLang.CELL_CREATIVE
					.toComponent()
					.withStyle(ChatFormatting.GRAY)

			tooltipComponents.accept(component)
		}
	}

	companion object {

		@JvmField
		val ADDITIONALLY_INCLUDED_ENTITY_TYPES: MutableSet<EntityType<out LivingEntity>> = mutableSetOf(
			EntityType.PLAYER,
			EntityType.IRON_GOLEM,
			EntityType.SNOW_GOLEM,
			EntityType.VILLAGER
		)

		@JvmField
		val VALID_ENTITY_TYPES: MutableSet<EntityType<*>> =
			BuiltInRegistries.ENTITY_TYPE
				.filter { it.category != MobCategory.MISC || it in ADDITIONALLY_INCLUDED_ENTITY_TYPES }
				.toMutableSet()


		fun setEntityType(itemStack: ItemStack, entityType: EntityType<*>): Boolean {
			if (entityType !in VALID_ENTITY_TYPES) {
				return false
			}

			itemStack.set(
				ModDataComponents.ENTITY_TYPE,
				entityType
			)
			return true
		}

		fun getOrganicStack(entityType: EntityType<*>): ItemStack {
			val itemStack = ModItems.ORGANIC_MATTER.toStack()
			setEntityType(itemStack, entityType)
			return itemStack
		}

		fun getCell(entityType: EntityType<*>): ItemStack {
			val itemStack = ModItems.CELL.toStack()
			setEntityType(itemStack, entityType)
			return itemStack
		}

		fun hasEntity(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.ENTITY_TYPE)

		fun getEntityType(itemStack: ItemStack): EntityType<*>? {
			return itemStack.get(ModDataComponents.ENTITY_TYPE)
		}
	}
}
