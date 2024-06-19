package dev.aaronhowser.mods.geneticsresequenced.util

import dev.aaronhowser.mods.geneticsresequenced.events.CustomEvents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import net.minecraft.world.item.ItemStack
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

class InventoryListener(
    private val serverPlayer: ServerPlayer
) : ContainerListener {

    override fun slotChanged(container: AbstractContainerMenu, index: Int, stack: ItemStack) {
        FORGE_BUS.post(CustomEvents.PlayerInventoryChangeEvent(serverPlayer, index, stack))
    }

    override fun dataChanged(p0: AbstractContainerMenu, p1: Int, p2: Int) {}

    companion object {

        fun startListening(player: ServerPlayer) {
            if (allListeners.any { it.serverPlayer == player }) return
            val listener = InventoryListener(player)
            player.containerMenu.addSlotListener(listener)
            allListeners.add(listener)
        }

        fun stopListening(player: ServerPlayer) {
            allListeners.removeIf { it.serverPlayer == player }
        }

        private val allListeners = mutableSetOf<InventoryListener>()
    }

}