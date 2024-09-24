package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.Level

abstract class IncubatorRecipe : Recipe<IncubatorRecipeInput> {
    override fun canCraftInDimensions(p0: Int, p1: Int): Boolean = true

    abstract val ingredients: List<Ingredient>
    override fun getIngredients(): NonNullList<Ingredient> {
        val list = NonNullList.create<Ingredient>()
        list.addAll(ingredients)

        return list
    }

    companion object {

        fun getIncubatorRecipes(level: Level): List<RecipeHolder<IncubatorRecipe>> {
            return getIncubatorRecipes(level.recipeManager)
        }

        @Suppress("UNCHECKED_CAST")
        fun getIncubatorRecipes(recipeManager: RecipeManager): List<RecipeHolder<IncubatorRecipe>> {
            return recipeManager.recipes.mapNotNull { if (it.value is IncubatorRecipe) it as? RecipeHolder<IncubatorRecipe> else null }
        }

        fun isValidIngredient(level: Level, itemStack: ItemStack): Boolean {
            return getIncubatorRecipes(level).any { recipeHolder ->
                recipeHolder.value.ingredients.any { ingredient -> ingredient.test(itemStack) }
            }
        }

        fun getIncubatorRecipe(level: Level, incubatorRecipeInput: IncubatorRecipeInput): IncubatorRecipe? {
            return getIncubatorRecipes(level).find { recipeHolder ->
                recipeHolder.value.matches(incubatorRecipeInput, level)
            }?.value
        }

        fun getIncubatorRecipe(level: Level, topStack: ItemStack, bottomStack: ItemStack): IncubatorRecipe? {
            return getIncubatorRecipe(level, IncubatorRecipeInput(topStack, bottomStack))
        }
    }

}