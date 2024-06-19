package dev.aaronhowser.mods.geneticsresequenced.util

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerListener
import net.minecraft.world.item.ItemStack
import net.minecraftforge.eventbus.api.Event
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

class InventoryListener(
    private val serverPlayer: ServerPlayer
) : ContainerListener {

    override fun slotChanged(container: AbstractContainerMenu, index: Int, stack: ItemStack) {
        FORGE_BUS.post(PlayerInventoryChangeEvent(serverPlayer, index, stack))
    }

    override fun dataChanged(p0: AbstractContainerMenu, p1: Int, p2: Int) {}

    companion object {
        data class PlayerInventoryChangeEvent(
            val player: ServerPlayer,
            val slot: Int,
            val stack: ItemStack
        ) : Event()

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