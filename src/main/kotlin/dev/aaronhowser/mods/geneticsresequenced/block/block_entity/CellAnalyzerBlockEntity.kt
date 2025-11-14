package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.menu.cell_analyzer.CellAnalyzerMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.InvWrapper
import java.util.function.IntSupplier

class CellAnalyzerBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.CELL_ANALYZER.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 32 }
	override val maxEnergy: Int = 60_000
	override val energyTransferRate: Int = 256

	override val invWrapper: InvWrapper = object : InvWrapper(container) {
		override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				INPUT_SLOT_INDEX -> stack.`is`(ModItems.ORGANIC_MATTER.get())
				OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER.get())
				OUTPUT_SLOT_INDEX -> false
				else -> false
			}
		}
	}

	override fun hasRecipe(): Boolean {
		val inputStack = invWrapper.getStackInSlot(INPUT_SLOT_INDEX)
		if (!inputStack.`is`(ModItems.ORGANIC_MATTER)) return false

		val mobType = EntityDnaItem.getEntityType(inputStack) ?: return false
		val potentialOutput = ModItems.CELL.get().defaultInstance
		val setWorked = EntityDnaItem.setEntityType(potentialOutput, mobType)

		if (!setWorked) return false

		return outputSlotHasRoom(potentialOutput)
	}

	private fun outputSlotHasRoom(potentialOutput: ItemStack): Boolean {
		val currentOutput = invWrapper.getStackInSlot(OUTPUT_SLOT_INDEX)
		if (currentOutput.isEmpty) return true

		if (!ItemStack.isSameItemSameComponents(potentialOutput, currentOutput)) return false

		val combinedCount = currentOutput.count + potentialOutput.count
		return combinedCount <= currentOutput.maxStackSize
	}

	//TODO: Make sure it works if things are in the output
	override fun craftItem() {
		val inputStack = invWrapper.getStackInSlot(INPUT_SLOT_INDEX)
		val mobType = EntityDnaItem.getEntityType(inputStack) ?: return

		val outputStack = ModItems.CELL.get().defaultInstance
		val setWorked = EntityDnaItem.setEntityType(outputStack, mobType)
		if (!setWorked) {
			GeneticsResequenced.LOGGER.error("A Cell Analyzer tried to set an invalid entity type at ${blockPos.x}, ${blockPos.y}, ${blockPos.z}: ${mobType.descriptionId}")
			return
		}

		invWrapper.extractItem(INPUT_SLOT_INDEX, 1, false)
		invWrapper.insertItem(OUTPUT_SLOT_INDEX, outputStack, false)
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return CellAnalyzerMenu(containerId, playerInventory, container, energyContainerData, progressContainerData)
	}
}