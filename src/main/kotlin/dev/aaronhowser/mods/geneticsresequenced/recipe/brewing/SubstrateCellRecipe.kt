package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.brewing.IBrewingRecipe

class SubstrateCellRecipe : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val pInputPotion = OtherUtil.getPotion(pBottomSlot)
        return pInputPotion == ModPotions.SUBSTRATE
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        if (pTopSlot.item != ModItems.CELL.get()) return false

        return EntityDnaItem.hasMob(pTopSlot)
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val pIngredientEntity = EntityDnaItem.getEntityType(pTopSlot) ?: return ItemStack.EMPTY

        val outputCell = ModItems.CELL.toStack()
        EntityDnaItem.setEntityType(outputCell, pIngredientEntity)

        return outputCell
    }

}