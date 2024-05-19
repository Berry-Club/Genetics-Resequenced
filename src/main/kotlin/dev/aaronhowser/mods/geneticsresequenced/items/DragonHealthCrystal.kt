package dev.aaronhowser.mods.geneticsresequenced.items

import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level

class DragonHealthCrystal : Item(
    Properties()
        .tab(ModItems.MOD_TAB)
        .defaultDurability(1000)
) {

    companion object {
        fun playBreakSound(level: Level, x: Double, y: Double, z: Double) {
            level.playSound(
                null,
                x, y, z,
                SoundEvents.ENDER_DRAGON_HURT,
                SoundSource.PLAYERS,
                1f,
                1f
            )
        }
    }

    override fun isValidRepairItem(pStack: ItemStack, pRepairCandidate: ItemStack): Boolean {
        return pRepairCandidate.`is`(Items.END_CRYSTAL)
    }

    override fun canApplyAtEnchantingTable(stack: ItemStack, enchantment: Enchantment): Boolean {
        if (enchantment in listOf(Enchantments.UNBREAKING, Enchantments.MENDING, Enchantments.VANISHING_CURSE)) {
            return false
        }
        return super.canApplyAtEnchantingTable(stack, enchantment)
    }

}