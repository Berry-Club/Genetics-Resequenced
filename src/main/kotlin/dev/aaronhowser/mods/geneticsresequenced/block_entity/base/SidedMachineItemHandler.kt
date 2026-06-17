package dev.aaronhowser.mods.geneticsresequenced.block_entity.base

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class SidedMachineItemHandler(
	private val backingHandler: IItemHandler,
	private val slots: IntArray,
	private val canInsert: (slot: Int, stack: ItemStack) -> Boolean,
	private val canExtract: (slot: Int, stack: ItemStack) -> Boolean
) : IItemHandler {

	constructor(
		backingHandler: IItemHandler,
		slot: Int,
		canInsert: (slot: Int, stack: ItemStack) -> Boolean,
		canExtract: (slot: Int, stack: ItemStack) -> Boolean
	) : this(backingHandler, intArrayOf(slot), canInsert, canExtract)

	override fun getSlots(): Int = slots.size

	override fun getStackInSlot(slot: Int): ItemStack {
		return backingHandler.getStackInSlot(toContainerSlot(slot))
	}

	override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
		val containerSlot = toContainerSlot(slot)
		if (stack.isEmpty || !canInsert(containerSlot, stack)) return stack

		return backingHandler.insertItem(containerSlot, stack, simulate)
	}

	override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
		val containerSlot = toContainerSlot(slot)
		val stack = backingHandler.getStackInSlot(containerSlot)
		if (stack.isEmpty || !canExtract(containerSlot, stack)) return ItemStack.EMPTY

		return backingHandler.extractItem(containerSlot, amount, simulate)
	}

	override fun getSlotLimit(slot: Int): Int {
		return backingHandler.getSlotLimit(toContainerSlot(slot))
	}

	override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
		val containerSlot = toContainerSlot(slot)
		return canInsert(containerSlot, stack) && backingHandler.isItemValid(containerSlot, stack)
	}

	private fun toContainerSlot(slot: Int): Int {
		require(slot in slots.indices) {
			"Slot $slot is not in sided inventory range [0, ${slots.size})"
		}

		return slots[slot]
	}

}