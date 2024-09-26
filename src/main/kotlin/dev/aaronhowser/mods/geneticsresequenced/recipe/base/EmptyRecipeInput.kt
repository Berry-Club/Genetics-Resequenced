package dev.aaronhowser.mods.geneticsresequenced.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

class EmptyRecipeInput : RecipeInput {
    override fun getItem(p0: Int): ItemStack = ItemStack.EMPTY
    override fun size(): Int = 0
}