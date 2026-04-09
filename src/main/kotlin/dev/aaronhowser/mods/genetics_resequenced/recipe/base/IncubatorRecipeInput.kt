package dev.aaronhowser.mods.genetics_resequenced.recipe.base

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.item.crafting.RecipeInput

class IncubatorRecipeInput(
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