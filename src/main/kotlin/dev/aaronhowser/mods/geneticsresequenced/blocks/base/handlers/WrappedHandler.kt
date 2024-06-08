package dev.aaronhowser.mods.geneticsresequenced.blocks.base.handlers

import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.IItemHandlerModifiable

class WrappedHandler(
    private val handler: IItemHandlerModifiable,
    private val canExtract: (Int) -> Boolean,           // Int is the slot id
    private val canInsert: (Int, ItemStack) -> Boolean  // Int is the slot id, ItemStack is the stack
) : IItemHandlerModifiable {
    override fun setStackInSlot(slot: Int, stack: ItemStack) {
        handler.setStackInSlot(slot, stack)
    }

    override fun getSlots(): Int {
        return handler.slots
    }

    override fun getStackInSlot(slot: Int): ItemStack {
        return handler.getStackInSlot(slot)
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        return if (canInsert(slot, stack)) handler.insertItem(slot, stack, simulate) else stack
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        return if (canExtract(slot)) handler.extractItem(slot, amount, simulate) else ItemStack.EMPTY
    }

    override fun getSlotLimit(slot: Int): Int {
        return handler.getSlotLimit(slot)
    }

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return canInsert(slot, stack) && handler.isItemValid(slot, stack)
    }
}