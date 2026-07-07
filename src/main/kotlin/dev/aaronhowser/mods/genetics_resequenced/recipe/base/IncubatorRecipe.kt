package dev.aaronhowser.mods.genetics_resequenced.recipe.base

import dev.aaronhowser.mods.genetics_resequenced.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

abstract class IncubatorRecipe(
	val topIngredient: Ingredient,
	val bottomIngredient: Ingredient
) : Recipe<IncubatorRecipe.Input> {

	val ingredients: List<Ingredient>
		get() = listOf(topIngredient, bottomIngredient)

	final override fun getType(): RecipeType<out IncubatorRecipe> {
		return ModRecipeTypes.INCUBATOR.get()
	}

	override fun showNotification(): Boolean {
		return false
	}

	override fun group(): String {
		return ""
	}

	override fun placementInfo(): PlacementInfo {
		return PlacementInfo.create(ingredients)
	}

	override fun recipeBookCategory(): RecipeBookCategory {
		return RecipeBookCategories.CRAFTING_MISC
	}

	companion object {

		fun getIncubatorRecipes(
			recipeManager: RecipeManager
		): List<RecipeHolder<IncubatorRecipe>> {
			return recipeManager.recipeMap().byType(ModRecipeTypes.INCUBATOR.get()).toList()
		}

		fun isValidTopIngredient(
			level: Level,
			itemStack: ItemStack
		): Boolean {
			val recipeManager = level.server?.recipeManager ?: return false
			val usedInIncubatorRecipe = getIncubatorRecipes(recipeManager)
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
			val recipeManager = level.server?.recipeManager ?: return false
			val usedInIncubatorRecipe = getIncubatorRecipes(recipeManager).any { recipeHolder ->
				recipeHolder.value.bottomIngredient.test(itemStack)
			}

			val usedInBrewingRecipe = level.potionBrewing().isInput(itemStack)

			return usedInIncubatorRecipe || usedInBrewingRecipe
		}

		fun getIncubatorRecipe(
			level: Level,
			input: Input
		): IncubatorRecipe? {
			val recipeManager = level.server?.recipeManager ?: return null
			return getIncubatorRecipes(recipeManager).find { recipeHolder ->
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
		val isHighTemp: Boolean,
		val registryAccess: HolderLookup.Provider? = null
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
