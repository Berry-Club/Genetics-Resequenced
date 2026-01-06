package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.getComponent
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.hasComponent
import dev.aaronhowser.mods.aaron.data_component.PseudoDataComponent.Companion.setComponent
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeDataComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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

open class EntityDnaItem(properties: Properties) : Item(properties) {

	override fun interactLivingEntity(
		pStack: ItemStack,
		pPlayer: Player,
		pInteractionTarget: LivingEntity,
		pUsedHand: InteractionHand
	): InteractionResult {
		if (!pPlayer.isCreative) return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)

		val newStack = pStack.copy()
		val setWorked = setEntityType(newStack, pInteractionTarget.type)

		if (!setWorked) {
			pPlayer.displayClientMessage(
				ModMessageLang.CANT_SET_ENTITY.toComponent(),
				true
			)

			return InteractionResult.PASS
		}

		pPlayer.setItemInHand(pUsedHand, newStack)

		return InteractionResult.SUCCESS
	}

	override fun appendHoverText(
		pStack: ItemStack,
		pContext: TooltipContext,
		pTooltipComponents: MutableList<Component>,
		pTooltipFlag: TooltipFlag
	) {
		val entityType = getEntityType(pStack)
		if (entityType != null) {
			val component =
				ModTooltipLang.CELL_MOB
					.toComponent(entityType.description)
					.withStyle(ChatFormatting.GRAY)
			pTooltipComponents.add(component)
		} else {
			val component =
				ModTooltipLang.CELL_NO_MOB
					.toComponent()
					.withStyle(ChatFormatting.GRAY)
			pTooltipComponents.add(component)
		}

		try {
			if (ClientUtil.playerIsCreative()) {
				val component =
					ModTooltipLang.CELL_CREATIVE
						.toComponent()
						.withStyle(ChatFormatting.GRAY)

				pTooltipComponents.add(component)
			}
		} catch (e: Exception) {
			GeneticsResequenced.LOGGER.error("EntityDnaItem isCreative check failed", e)
		}

		super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag)
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

			itemStack.setComponent(EntityTypeDataComponent(entityType))

			return true
		}

		fun getOrganicStack(entityType: EntityType<*>): ItemStack {
			val itemStack = ModItems.ORGANIC_MATTER.getDefaultInstance()
			setEntityType(itemStack, entityType)
			return itemStack
		}

		fun getCell(entityType: EntityType<*>): ItemStack {
			val itemStack = ModItems.CELL.getDefaultInstance()
			setEntityType(itemStack, entityType)
			return itemStack
		}

		fun hasEntity(itemStack: ItemStack): Boolean = itemStack.hasComponent(EntityTypeDataComponent.Type)

		fun getEntityType(itemStack: ItemStack): EntityType<*>? {
			return itemStack.getComponent(EntityTypeDataComponent.Type)?.entityType
		}
	}
}
