package dev.aaronhowser.mods.geneticsresequenced.recipe.base

import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

abstract class IncubatorRecipe(
	val topIngredient: Ingredient,
	val bottomIngredient: Ingredient
) : Recipe<IncubatorRecipe.Input> {

	override fun canCraftInDimensions(width: Int, height: Int): Boolean = true

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

		fun getIncubatorRecipes(
			recipeManager: RecipeManager
		): List<RecipeHolder<IncubatorRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.INCUBATOR.get())
		}

		fun isValidTopIngredient(
			level: Level,
			itemStack: ItemStack
		): Boolean {
			val usedInIncubatorRecipe = getIncubatorRecipes(level.recipeManager)
				.any { recipeHolder ->
					recipeHolder.value.topIngredient.test(itemStack)
				}

			val usedInBrewingRecipe = level.potionBrewing().isIngredient(itemStack)

			return usedInIncubatorRecipe || usedInBrewingRecipe
		}

		fun isValidBottomIngredient(
			level: Level,
			itemStack: ItemStack
		): Boolean {
			val usedInIncubatorRecipe = getIncubatorRecipes(level.recipeManager).any { recipeHolder ->
				recipeHolder.value.bottomIngredient.test(itemStack)
			}

			val usedInBrewingRecipe = level.potionBrewing().isInput(itemStack)

			return usedInIncubatorRecipe || usedInBrewingRecipe
		}

		fun getIncubatorRecipe(
			level: Level,
			input: Input
		): IncubatorRecipe? {
			return getIncubatorRecipes(level.recipeManager).find { recipeHolder ->
				recipeHolder.value.matches(input, level)
			}?.value
		}

		fun hasIncubatorRecipe(
			level: Level,
			input: Input
		): Boolean {
			return getIncubatorRecipe(level, input) != null || input.isValidPotionRecipe(level.potionBrewing())
		}

	}

	class Input(
		private val topItem: ItemStack,
		private val bottomItem: ItemStack,
		val isHighTemp: Boolean
	) : RecipeInput {

		fun isValidPotionRecipe(potionBrewing: PotionBrewing): Boolean {
			return this.isHighTemp && potionBrewing.hasMix(this.bottomItem, this.topItem)
		}

		val isLowTemp: Boolean = !this.isHighTemp

		fun getTopItem(): ItemStack = this.topItem.copy()
		fun getBottomItem(): ItemStack = this.bottomItem.copy()

		override fun getItem(index: Int): ItemStack {
			return when (index) {
				0 -> getTopItem()
				1 -> getBottomItem()
				else -> error("Invalid index $index")
			}
		}

		override fun size(): Int = 2
	}

}