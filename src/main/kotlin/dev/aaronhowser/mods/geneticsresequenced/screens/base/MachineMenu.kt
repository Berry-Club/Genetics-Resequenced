package dev.aaronhowser.mods.geneticsresequenced.screens.base

import dev.aaronhowser.mods.geneticsresequenced.blocks.base.InventoryAndEnergyBlockEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class MachineMenu(
    menuType: MenuType<*>,
    val blockEntity: InventoryAndEnergyBlockEntity,
    id: Int,
    inventory: Inventory
) : AbstractContainerMenu(menuType, id) {

    protected val level: Level = inventory.player.level

    abstract override fun quickMoveStack(playerIn: Player, index: Int): ItemStack
    abstract override fun stillValid(pPlayer: Player): Boolean

    protected open val topLeftInventoryX = 8
    protected open val topLeftInventoryY = 84

    // Adds the 27 slots of the player inventory
    protected fun addPlayerInventory(playerInventory: Inventory) {
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                addSlot(
                    Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        topLeftInventoryX + column * 18,
                        topLeftInventoryY + row * 18
                    )
                )
            }
        }
    }

    // Adds the 9 slots of the player hotbar
    protected fun addPlayerHotbar(playerInventory: Inventory) {
        for (column in 0 until 9) {
            addSlot(
                Slot(
                    playerInventory,
                    column,
                    topLeftInventoryX + column * 18,
                    topLeftInventoryY + 4 + 3 * 18
                )
            )
        }
    }

    companion object {

    }

}