package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
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

open class EntityDnaItem : Item(Properties()) {

    companion object {

        fun setEntityType(itemStack: ItemStack, entityType: EntityType<*>): Boolean {
            if (entityType !in validEntityTypes) {
                return false
            }

            itemStack.set(
                ModDataComponents.ENTITY_TYPE_COMPONENT,
                entityType
            )
            return true
        }

        fun hasEntity(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.ENTITY_TYPE_COMPONENT)

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            return itemStack.get(ModDataComponents.ENTITY_TYPE_COMPONENT)
        }

        val includeTheseEntityTypes = mutableSetOf(
            EntityType.PLAYER,
            EntityType.IRON_GOLEM,
            EntityType.SNOW_GOLEM,
            EntityType.VILLAGER
        )

        val validEntityTypes: MutableSet<EntityType<*>> =
            BuiltInRegistries.ENTITY_TYPE
                .filter { it.category != MobCategory.MISC || it in includeTheseEntityTypes }
                .toMutableSet()

        fun getOrganicMatterStack(entityType: EntityType<*>): ItemStack {
            val itemStack = ModItems.ORGANIC_MATTER.toStack()
            return if (setEntityType(itemStack, entityType)) itemStack else ItemStack.EMPTY
        }

        fun getAllOrganicMatterStacks(): List<ItemStack> {
            return validEntityTypes.map { getOrganicMatterStack(it) }
        }

    }

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
                ModLanguageProvider.Messages.CANT_SET_ENTITY.toComponent(),
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
                ModLanguageProvider.Tooltips.CELL_MOB
                    .toComponent(entityType.description)
                    .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(component)
        } else {
            val component =
                ModLanguageProvider.Tooltips.CELL_NO_MOB
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(component)
        }

        try {
            if (ClientUtil.playerIsCreative()) {
                val component =
                    ModLanguageProvider.Tooltips.CELL_CREATIVE
                        .toComponent()
                        .withColor(ChatFormatting.GRAY)

                pTooltipComponents.add(component)
            }
        } catch (e: Exception) {
            GeneticsResequenced.LOGGER.error("EntityDnaItem isCreative check failed", e)
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag)
    }

}