package dev.aaronhowser.mods.geneticsresequenced.item

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

object DragonHealthCrystal : Item(
    Properties()
        .tab(GeneticsResequenced.MOD_TAB)
        .defaultDurability(1000)
) {

    override fun isValidRepairItem(pStack: ItemStack, pRepairCandidate: ItemStack): Boolean {
        return pRepairCandidate.`is`(Items.END_CRYSTAL)
    }

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