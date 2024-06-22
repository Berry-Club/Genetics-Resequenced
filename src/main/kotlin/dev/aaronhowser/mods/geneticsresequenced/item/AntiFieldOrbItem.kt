package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.item.components.BooleanComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

class AntiFieldOrbItem : Item(
    Properties().stacksTo(1)
) {

    companion object {

        private fun isEnabled(itemStack: ItemStack): Boolean {
            return itemStack.get(BooleanComponent.component)?.value ?: false
        }

        private fun toggleEnabled(itemStack: ItemStack) {
            itemStack.set(
                BooleanComponent.component,
                BooleanComponent(!isEnabled(itemStack))
            )
        }

        fun isActiveForPlayer(player: Player): Boolean {
            return player.inventory.items.any { it.item == ModItems.ANTI_FIELD_ORB.get() && isEnabled(it) }
        }

    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        toggleEnabled(pPlayer.getItemInHand(pUsedHand))
        return super.use(pLevel, pPlayer, pUsedHand)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isEnabled(pStack)
    }

    override fun appendHoverText(
        pStack: ItemStack,
        pContext: TooltipContext,
        pTooltipComponents: MutableList<Component>,
        pTooltipFlag: TooltipFlag
    ) {
        pTooltipComponents.add(
            Component
                .translatable(if (isEnabled(pStack)) "tooltip.geneticsresequenced.antifield_active" else "tooltip.geneticsresequenced.antifield_inactive")
                .withColor(ChatFormatting.GRAY)
        )

        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag)
    }

}