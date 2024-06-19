package dev.aaronhowser.mods.geneticsresequenced.util

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import net.minecraft.world.item.ItemStack

class InventoryListener(
    private val serverPlayer: ServerPlayer
): ContainerListener {
    override fun slotChanged(p0: AbstractContainerMenu, p1: Int, p2: ItemStack) {
        TODO("Not yet implemented")
    }

    override fun dataChanged(p0: AbstractContainerMenu, p1: Int, p2: Int) {}

}