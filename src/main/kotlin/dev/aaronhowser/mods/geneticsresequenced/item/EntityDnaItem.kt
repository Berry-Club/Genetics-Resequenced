package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeItemComponent
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag

class EntityDnaItem : Item(Properties()) {

    companion object {

        fun setMob(itemStack: ItemStack, entityType: EntityType<*>) {
            itemStack.set(EntityTypeItemComponent.component, EntityTypeItemComponent(entityType))
        }

        fun ItemStack.setMob(entityType: EntityType<*>): ItemStack {
            setMob(this, entityType)
            return this
        }

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            return itemStack.get(EntityTypeItemComponent.component)?.entity
        }
    }

    override fun interactLivingEntity(
        pStack: ItemStack,
        pPlayer: Player,
        pInteractionTarget: LivingEntity,
        pUsedHand: InteractionHand
    ): InteractionResult {

        if (!pPlayer.isCreative) return super.interactLivingEntity(pStack, pPlayer, pInteractionTarget, pUsedHand)

        val newStack = pStack.copy().setMob(pInteractionTarget.type)
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
            val component = Component
                .translatable(
                    ModLanguageProvider.Tooltips.CELL_MOB,
                    entityType.description
                )
                .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(component)
        } else {
            val component = Component
                .translatable(ModLanguageProvider.Tooltips.CELL_NO_MOB)
                .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(component)
        }

        try {
            if (ClientUtil.playerIsCreative()) {
                val component =
                    Component
                        .translatable(ModLanguageProvider.Tooltips.CELL_CREATIVE)
                        .withColor(ChatFormatting.GRAY)

                pTooltipComponents.add(component)
            }
        } catch (e: Exception) {
            GeneticsResequenced.LOGGER.error("EntityDnaItem isCreative check failed", e)
        }

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag)
    }

}