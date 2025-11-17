package dev.aaronhowser.mods.geneticsresequenced.menu

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.EnergyContainerData
import dev.aaronhowser.mods.irregular_implements.menu.MenuWithInventory
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

abstract class MachineMenu(
	menuType: MenuType<*>,
	id: Int,
	playerInventory: Inventory,
	protected val energyContainerData: ContainerData
) : MenuWithInventory(menuType, id, playerInventory) {

	protected val level: Level = playerInventory.player.level()

	protected open val inventoryX = 8
	protected open val inventoryY = 90
	protected abstract val amountSlots: Int

	init {
		addPlayerInventorySlots()
		addDataSlots(energyContainerData)
	}

	fun getCurrentEnergy(): Int = energyContainerData.get(EnergyContainerData.CURRENT_ENERGY_INDEX)
	fun getMaxEnergy(): Int = energyContainerData.get(EnergyContainerData.MAX_ENERGY_INDEX)

	fun addPlayerInventorySlots() {
		addPlayerInventorySlots(inventoryY)
	}

	abstract fun getPercentDone(): Float

	// CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
	// must assign a slot number to each of the slots used by the GUI.
	// For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
	// Each time we add a Slot to the container, it automatically increases the slotIndex, which means
	//  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
	//  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
	//  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)

	override fun quickMoveStack(playerIn: Player, index: Int): ItemStack {
		val sourceSlot = slots.getOrNull(index)
		if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY //EMPTY_ITEM
		val sourceStack = sourceSlot.item
		val copyOfSourceStack = sourceStack.copy()

		// Check if the slot clicked is one of the vanilla container slots
		if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!moveItemStackTo(
					sourceStack,
					TE_INVENTORY_FIRST_SLOT_INDEX,
					TE_INVENTORY_FIRST_SLOT_INDEX + amountSlots,
					false
				)
			) {
				return ItemStack.EMPTY // EMPTY_ITEM
			}
		} else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + amountSlots) {
			// This is a TE slot so merge the stack into the players inventory
			if (!moveItemStackTo(
					sourceStack,
					VANILLA_FIRST_SLOT_INDEX,
					VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
					false
				)
			) {
				return ItemStack.EMPTY
			}
		} else {
			GeneticsResequenced.LOGGER.error("Invalid slotIndex: $index")
			return ItemStack.EMPTY
		}
		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.count == 0) {
			sourceSlot.set(ItemStack.EMPTY)
		} else {
			sourceSlot.setChanged()
		}
		sourceSlot.onTake(playerIn, sourceStack)
		return copyOfSourceStack
	}

	companion object {
		private const val HOTBAR_SLOT_COUNT = 9
		private const val PLAYER_INVENTORY_ROW_COUNT = 3
		private const val PLAYER_INVENTORY_COLUMN_COUNT = 9
		private const val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT
		private const val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT
		private const val VANILLA_FIRST_SLOT_INDEX = 0
		private const val TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT
	}

}