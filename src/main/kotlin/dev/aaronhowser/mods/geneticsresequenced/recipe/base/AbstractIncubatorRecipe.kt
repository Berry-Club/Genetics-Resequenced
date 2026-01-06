package dev.aaronhowser.mods.geneticsresequenced.recipe.base

import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeType
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
		list.add(this.topIngredient)
		list.add(this.bottomIngredient)

		return list
	}

	companion object {

		fun getIncubatorRecipes(recipeManager: RecipeManager): List<AbstractIncubatorRecipe> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.INCUBATOR.get())
		}

		fun isValidTopIngredient(level: Level, itemStack: ItemStack): Boolean {
			val usedInIncubatorRecipe = getIncubatorRecipes(level.recipeManager)
				.any { recipeHolder ->
					recipeHolder.topIngredient.test(itemStack)
				}

			val usedInBrewingRecipe = PotionBrewing.isIngredient(itemStack)

			return usedInIncubatorRecipe || usedInBrewingRecipe
		}

		fun isValidBottomIngredient(level: Level, itemStack: ItemStack): Boolean {
			val usedInIncubatorRecipe = getIncubatorRecipes(level.recipeManager).any { recipeHolder ->
				recipeHolder.bottomIngredient.test(itemStack)
			}

			val usedInBrewingRecipe = PotionBrewing.POTION_MIXES.any { mix ->
				PotionUtils.getPotion(itemStack) == mix.from.get()
			} || PotionBrewing.CONTAINER_MIXES.any { mix ->
				itemStack.isItem(mix.from)
			}

			return usedInIncubatorRecipe || usedInBrewingRecipe
		}

		fun getIncubatorRecipe(level: Level, incubatorRecipeInput: IncubatorRecipeInput): AbstractIncubatorRecipe? {
			return getIncubatorRecipes(level.recipeManager).find { recipeHolder ->
				recipeHolder.matches(incubatorRecipeInput, level)
			}
		}

		fun hasIncubatorRecipe(
			level: Level,
			incubatorRecipeInput: IncubatorRecipeInput
		): Boolean {
			return getIncubatorRecipe(level, incubatorRecipeInput) != null || incubatorRecipeInput.isValidPotionRecipe()
		}

	}

}