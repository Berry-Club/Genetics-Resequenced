package dev.aaronhowser.mods.geneticsresequenced.items

import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object AntiFieldOrbItem : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .stacksTo(1)
) {

    private const val ENABLED_NBT = "Enabled"
    private fun isEnabled(itemStack: ItemStack): Boolean {
        return itemStack.tag?.getBoolean(ENABLED_NBT) ?: false
    }

    private fun toggleEnabled(itemStack: ItemStack) {
        val tag = itemStack.orCreateTag
        tag.putBoolean(ENABLED_NBT, !isEnabled(itemStack))
    }

    override fun use(pLevel: Level, pPlayer: Player, pUsedHand: InteractionHand): InteractionResultHolder<ItemStack> {
        toggleEnabled(pPlayer.getItemInHand(pUsedHand))
        return super.use(pLevel, pPlayer, pUsedHand)
    }

    override fun isFoil(pStack: ItemStack): Boolean {
        return isEnabled(pStack)
    }

    fun isActiveForPlayer(player: Player): Boolean {
        return player.inventory.items.any { it.item === this && isEnabled(it) }
    }

}