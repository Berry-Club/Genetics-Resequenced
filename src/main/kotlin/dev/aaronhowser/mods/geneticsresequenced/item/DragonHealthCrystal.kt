package dev.aaronhowser.mods.geneticsresequenced.item

import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class DragonHealthCrystal : Item(
    Properties().durability(1000)
) {

    override fun getBreakingSound(): SoundEvent = SoundEvents.ENDER_DRAGON_HURT

    override fun isValidRepairItem(pStack: ItemStack, pRepairCandidate: ItemStack): Boolean {
        return pRepairCandidate.item === Items.END_CRYSTAL
    }

}