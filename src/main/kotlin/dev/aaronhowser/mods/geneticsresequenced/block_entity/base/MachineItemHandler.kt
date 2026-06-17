package dev.aaronhowser.mods.geneticsresequenced.block_entity.base

import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable

class MachineItemHandler(
	private val container: Container
) : IItemHandlerModifiable {

	override fun getSlots(): Int = container.containerSize

	override fun getStackInSlot(slot: Int): ItemStack {
		validateSlot(slot)
		return container.getItem(slot)
	}

	override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
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

	override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
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

	override fun getSlotLimit(slot: Int): Int {
		validateSlot(slot)
		return container.maxStackSize
	}

	override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
		validateSlot(slot)
		return container.canPlaceItem(slot, stack)
	}

	override fun setStackInSlot(slot: Int, stack: ItemStack) {
		validateSlot(slot)
		container.setItem(slot, stack)
	}

	private fun validateSlot(slot: Int) {
		require(slot in 0..<container.containerSize) {
			"Slot $slot is not in inventory range [0, ${container.containerSize})"
		}
	}

}