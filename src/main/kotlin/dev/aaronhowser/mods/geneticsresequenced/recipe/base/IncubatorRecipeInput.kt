package dev.aaronhowser.mods.geneticsresequenced.recipe.base

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionBrewing

class IncubatorRecipeInput(
	private val topItem: ItemStack,
	private val bottomItem: ItemStack,
	val isHighTemp: Boolean
) : Container {

	fun isValidPotionRecipe(): Boolean {
		return this.isHighTemp && PotionBrewing.hasMix(this.bottomItem, this.topItem)
	}

	val isLowTemp: Boolean = !this.isHighTemp

	fun getTopItem(): ItemStack = this.topItem.copy()
	fun getBottomItem(): ItemStack = this.bottomItem.copy()

	override fun getContainerSize(): Int = 2

	override fun isEmpty(): Boolean = this.topItem.isEmpty && this.bottomItem.isEmpty

	override fun getItem(index: Int): ItemStack {
		return when (index) {
			0 -> getTopItem()
			1 -> getBottomItem()
			else -> error("Invalid index $index")
		}
	}

	override fun removeItem(pSlot: Int, pAmount: Int): ItemStack {
		val stack = getItem(pSlot)

		if (stack.isEmpty) {
			return ItemStack.EMPTY
		}

		if (stack.count <= pAmount) {
			setItem(pSlot, ItemStack.EMPTY)
			return stack
		} else {
			val result = stack.split(pAmount)
			if (stack.count == 0) {
				setItem(pSlot, ItemStack.EMPTY)
			} else {
				setItem(pSlot, stack)
			}
			return result
		}
	}

	override fun removeItemNoUpdate(pSlot: Int): ItemStack {
		val stack = getItem(pSlot)
		setItem(pSlot, ItemStack.EMPTY)
		return stack
	}

	override fun setItem(pSlot: Int, pStack: ItemStack) {
		error("IncubatorRecipeInput is immutable")
	}

	override fun setChanged() {
		// Do nothing
	}

	override fun stillValid(pPlayer: Player): Boolean {
		return true
	}

	override fun clearContent() {
		// Do nothing
	}
}