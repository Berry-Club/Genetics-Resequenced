package dev.aaronhowser.mods.genetics_resequenced.block_entity.base

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.CombinedResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class SidedMachineItemHandler private constructor(
	handlers: Array<ResourceHandler<ItemResource>>
) : CombinedResourceHandler<ItemResource>(*handlers) {

	constructor(
		backingHandler: MachineItemHandler,
		slots: IntArray,
		canInsert: (slot: Int, stack: ItemStack) -> Boolean,
		canExtract: (slot: Int, stack: ItemStack) -> Boolean
	) : this(
		Array<ResourceHandler<ItemResource>>(slots.size) { index ->
			ContainerSlotResourceHandler(backingHandler, slots[index], canInsert, canExtract)
		}
	)

	constructor(
		backingHandler: MachineItemHandler,
		slot: Int,
		canInsert: (slot: Int, stack: ItemStack) -> Boolean,
		canExtract: (slot: Int, stack: ItemStack) -> Boolean
	) : this(backingHandler, intArrayOf(slot), canInsert, canExtract)

	private class ContainerSlotResourceHandler(
		private val backingHandler: MachineItemHandler,
		private val slot: Int,
		private val canInsert: (slot: Int, stack: ItemStack) -> Boolean,
		private val canExtract: (slot: Int, stack: ItemStack) -> Boolean
	) : ItemStackResourceHandler() {

		override fun getStack(): ItemStack {
			return backingHandler.getStackInSlot(slot)
		}

		override fun setStack(stack: ItemStack) {
			backingHandler.setStackInSlot(slot, stack)
		}

		override fun isValid(resource: ItemResource): Boolean {
			val stack = resource.toStack()
			return canInsert(slot, stack) && backingHandler.isItemValid(slot, stack)
		}

		override fun getCapacity(resource: ItemResource): Int {
			val slotLimit = backingHandler.getSlotLimit(slot)
			return if (resource.isEmpty) {
				slotLimit
			} else {
				minOf(slotLimit, resource.maxStackSize)
			}
		}

		override fun extract(
			index: Int,
			resource: ItemResource,
			amount: Int,
			transaction: TransactionContext
		): Int {
			val stack = backingHandler.getStackInSlot(slot)
			if (stack.isEmpty || !canExtract(slot, stack)) return 0

			return super.extract(index, resource, amount, transaction)
		}

		override fun onRootCommit(snapshot: ItemStack) {
			backingHandler.setChanged()
		}
	}
}
