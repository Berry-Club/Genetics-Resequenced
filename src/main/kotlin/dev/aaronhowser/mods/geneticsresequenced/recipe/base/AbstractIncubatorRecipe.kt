package dev.aaronhowser.mods.geneticsresequenced.recipe.base

import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

abstract class AbstractIncubatorRecipe(
    val topIngredient: Ingredient,
    val bottomIngredient: Ingredient
) : Recipe<IncubatorRecipeInput> {

    override fun canCraftInDimensions(p0: Int, p1: Int): Boolean = true

    final override fun getType(): RecipeType<*> {
        return ModRecipeTypes.INCUBATOR.get()
    }

    override fun getIngredients(): NonNullList<Ingredient> {
        val list = NonNullList.create<Ingredient>()
        list.add(topIngredient)
        list.add(bottomIngredient)

        return list
    }

    companion object {

        fun getIncubatorRecipes(recipeManager: RecipeManager): List<RecipeHolder<AbstractIncubatorRecipe>> {
            return recipeManager.getAllRecipesFor(ModRecipeTypes.INCUBATOR.get())
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
            return getIncubatorRecipe(level, incubatorRecipeInput) != null || incubatorRecipeInput.isValidPotionRecipe(level.potionBrewing())
        }

    }

}