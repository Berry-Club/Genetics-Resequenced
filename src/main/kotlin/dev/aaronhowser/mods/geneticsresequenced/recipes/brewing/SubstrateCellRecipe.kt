package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing

import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraftforge.common.brewing.IBrewingRecipe

class SubstrateCellRecipe : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val pInputPotion = PotionUtils.getPotion(pBottomSlot)
        return pInputPotion == ModPotions.SUBSTRATE
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        if (pTopSlot.item != ModItems.CELL.get()) return false

        return EntityDnaItem.hasEntity(pTopSlot)
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val pIngredientEntity = EntityDnaItem.getEntityType(pTopSlot) ?: return ItemStack.EMPTY

        val outputCell = ModItems.CELL.itemStack
        EntityDnaItem.setMob(outputCell, pIngredientEntity)

        return outputCell
    }
}