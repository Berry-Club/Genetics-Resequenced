package dev.aaronhowser.mods.genetics_resequenced.block_entity.base

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class MachineItemHandler(
	private val container: Container
) {

	fun getSlots(): Int = container.containerSize

	fun getStackInSlot(slot: Int): ItemStack {
		validateSlot(slot)
		return container.getItem(slot)
	}

	fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
		validateSlot(slot)
		if (stack.isEmpty) return ItemStack.EMPTY

		val existingStack = container.getItem(slot)
		if (!existingStack.isEmpty && !ItemStack.isSameItemSameComponents(existingStack, stack)) {
			return stack
		}

		val insertLimit = minOf(getSlotLimit(slot), stack.maxStackSize)
		val availableSpace = if (existingStack.isEmpty) insertLimit else insertLimit - existingStack.count
		if (availableSpace <= 0) return stack

		val amountToInsert = minOf(stack.count, availableSpace)

		if (!simulate) {
			if (existingStack.isEmpty) {
				container.setItem(slot, stack.copyWithCount(amountToInsert))
			} else {
				existingStack.grow(amountToInsert)
				container.setChanged()
			}
		}

		return if (stack.count == amountToInsert) {
			ItemStack.EMPTY
		} else {
			stack.copyWithCount(stack.count - amountToInsert)
		}
	}

	fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
		validateSlot(slot)
		if (amount <= 0) return ItemStack.EMPTY

		val existingStack = container.getItem(slot)
		if (existingStack.isEmpty) return ItemStack.EMPTY

		val amountToExtract = minOf(amount, existingStack.count)

		return if (simulate) {
			existingStack.copyWithCount(amountToExtract)
		} else {
			container.removeItem(slot, amountToExtract)
		}
	}

	fun getSlotLimit(slot: Int): Int {
		validateSlot(slot)
		return container.maxStackSize
	}

	fun isItemValid(slot: Int, stack: ItemStack): Boolean {
		validateSlot(slot)
		return container.canPlaceItem(slot, stack)
	}

	fun setStackInSlot(slot: Int, stack: ItemStack) {
		validateSlot(slot)
		container.setItem(slot, stack)
	}

	fun setChanged() {
		container.setChanged()
	}

	private fun validateSlot(slot: Int) {
		require(slot in 0..<container.containerSize) {
			"Slot $slot is not in inventory range [0, ${container.containerSize})"
		}
	}

}
