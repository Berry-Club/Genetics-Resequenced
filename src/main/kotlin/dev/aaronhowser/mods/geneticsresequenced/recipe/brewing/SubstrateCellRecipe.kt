package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.brewing.IBrewingRecipe

class SubstrateCellRecipe(
    val isGmoCell: Boolean = false
) : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val pInputPotion = OtherUtil.getPotion(pBottomSlot)
        return pInputPotion == ModPotions.SUBSTRATE
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        if (pTopSlot.item != if (isGmoCell) ModItems.GMO_CELL.get() else ModItems.CELL.get()) return false

        return EntityDnaItem.hasEntity(pTopSlot)
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val pIngredientEntity = EntityDnaItem.getEntityType(pTopSlot) ?: return ItemStack.EMPTY

        val outputCell: ItemStack

        if (isGmoCell) {
            val pIngredientGene = DnaHelixItem.getGene(pTopSlot) ?: return ItemStack.EMPTY

            outputCell = ModItems.GMO_CELL.toStack()
            GmoCell.setDetails(outputCell, pIngredientEntity, pIngredientGene)
        } else {
            outputCell = ModItems.CELL.toStack()
            EntityDnaItem.setEntityType(outputCell, pIngredientEntity)
        }

        return outputCell
    }

}