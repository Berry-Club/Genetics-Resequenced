package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing

import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraftforge.common.brewing.IBrewingRecipe

class SetPotionEntityRecipe : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false
        if (EntityDnaItem.hasEntity(pBottomSlot)) return false

        val inputPotion = PotionUtils.getPotion(pBottomSlot)
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
        EntityDnaItem.setMob(output, ingredientEntity)
        return output
    }
}