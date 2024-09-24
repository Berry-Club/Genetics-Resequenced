package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import net.minecraft.core.NonNullList
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe

abstract class IncubatorRecipe : Recipe<IncubatorRecipeInput> {
    override fun canCraftInDimensions(p0: Int, p1: Int): Boolean = true

    abstract val ingredients: List<Ingredient>
    override fun getIngredients(): NonNullList<Ingredient> {
        val list = NonNullList.create<Ingredient>()
        list.addAll(ingredients)

        return list
    }
}