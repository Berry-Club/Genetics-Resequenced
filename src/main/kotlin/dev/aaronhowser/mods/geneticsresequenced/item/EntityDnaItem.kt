package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.item.components.EntityTypeComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
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

        fun hasEntity(itemStack: ItemStack): Boolean = itemStack.has(ModDataComponents.ENTITY_TYPE_COMPONENT.get())

        fun setMob(itemStack: ItemStack, entityType: EntityType<*>) {
            itemStack.set(ModDataComponents.ENTITY_TYPE_COMPONENT.get(), EntityTypeComponent(entityType))
        }

        fun ItemStack.setMob(entityType: EntityType<*>): ItemStack {
            setMob(this, entityType)
            return this
        }

        fun getEntityType(itemStack: ItemStack): EntityType<*>? {
            if (!hasEntity(itemStack)) return null

            return itemStack.get(ModDataComponents.ENTITY_TYPE_COMPONENT.get())?.entity
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
                .translatable("tooltip.geneticsresequenced.dna_item.filled", entityType.description)
                .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(component)
        } else {
            val component = Component
                .translatable("tooltip.geneticsresequenced.dna_item.empty")
                .withColor(ChatFormatting.GRAY)
            pTooltipComponents.add(component)
        }

    }

}