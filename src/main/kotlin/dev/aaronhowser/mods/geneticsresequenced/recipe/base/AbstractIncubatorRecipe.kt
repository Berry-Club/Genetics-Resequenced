package dev.aaronhowser.mods.geneticsresequenced.recipe.base

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.level.Level

abstract class AbstractIncubatorRecipe(
    val topIngredient: Ingredient,
    val bottomIngredient: Ingredient
) : Recipe<IncubatorRecipeInput> {

    override fun canCraftInDimensions(p0: Int, p1: Int): Boolean = true

    override fun getIngredients(): NonNullList<Ingredient> {
        val list = NonNullList.create<Ingredient>()
        list.add(topIngredient)
        list.add(bottomIngredient)

        return list
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun getIncubatorRecipes(recipeManager: RecipeManager): List<RecipeHolder<AbstractIncubatorRecipe>> {
            return recipeManager.recipes.mapNotNull {
                if (it.value is AbstractIncubatorRecipe) it as? RecipeHolder<AbstractIncubatorRecipe> else null
            }
        }

        fun isValidTopIngredient(level: Level, itemStack: ItemStack): Boolean {
            val usedInIncubatorRecipe = getIncubatorRecipes(level.recipeManager).any { recipeHolder ->
                recipeHolder.value.topIngredient.test(itemStack)
            }

            val usedInBrewingRecipe = level.potionBrewing().isIngredient(itemStack)

            return usedInIncubatorRecipe || usedInBrewingRecipe
        }

        fun isValidBottomIngredient(level: Level, itemStack: ItemStack): Boolean {
            val usedInIncubatorRecipe = getIncubatorRecipes(level.recipeManager).any { recipeHolder ->
                recipeHolder.value.bottomIngredient.test(itemStack)
            }

            val usedInBrewingRecipe = level.potionBrewing().isInput(itemStack)

            return usedInIncubatorRecipe || usedInBrewingRecipe
        }

        fun getIncubatorRecipe(level: Level, incubatorRecipeInput: IncubatorRecipeInput): AbstractIncubatorRecipe? {
            return getIncubatorRecipes(level.recipeManager).find { recipeHolder ->
                recipeHolder.value.matches(incubatorRecipeInput, level)
            }?.value
        }

        fun hasIncubatorRecipe(
            level: Level,
            incubatorRecipeInput: IncubatorRecipeInput
        ): Boolean {
            return getIncubatorRecipe(level, incubatorRecipeInput) != null
        }

    }

}