package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.brewing.IBrewingRecipe

class SetPotionEntityRecipe : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false
        if (EntityDnaItem.hasEntity(pBottomSlot)) return false

        val inputPotion = OtherUtil.getPotion(pBottomSlot) ?: return false
        return inputPotion == ModPotions.CELL_GROWTH || inputPotion == ModPotions.MUTATION
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        if (pTopSlot.item != ModItems.CELL.get()) return false

        return EntityDnaItem.hasEntity(pTopSlot)
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val ingredientEntity = EntityDnaItem.getEntityType(pTopSlot) ?: return ItemStack.EMPTY

        val output = pBottomSlot.copy()
        EntityDnaItem.setEntityType(output, ingredientEntity)
        return output
    }
}