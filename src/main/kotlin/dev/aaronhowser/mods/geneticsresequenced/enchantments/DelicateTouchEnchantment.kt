package dev.aaronhowser.mods.geneticsresequenced.enchantments

import dev.aaronhowser.mods.geneticsresequenced.registries.ModEnchantments
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.enchantment.Enchantment

object DelicateTouchEnchantment : Enchantment(
    Rarity.COMMON,
    ModEnchantments.ScraperEnchantmentCategory,
    arrayOf(
        EquipmentSlot.OFFHAND,
        EquipmentSlot.MAINHAND
    )
) {

    override fun getMinCost(pLevel: Int): Int = 5
    override fun getMaxCost(pLevel: Int): Int = 15

}