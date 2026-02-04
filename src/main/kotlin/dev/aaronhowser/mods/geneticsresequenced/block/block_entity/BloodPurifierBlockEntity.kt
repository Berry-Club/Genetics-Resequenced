package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.menu.blood_purifier.BloodPurifierMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

class BloodPurifierBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.BLOOD_PURIFIER.get(), pos, blockState) {

	override val maxEnergy: Int = 60_000
	override val energyTransferRate: Int = 256
	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 32 }

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, DEFAULT_INVENTORY_SIZE) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				INPUT_SLOT_INDEX -> SyringeItem.hasBlood(stack)
				OVERCLOCK_SLOT_INDEX -> stack.isItem(ModItems.OVERCLOCKER)
				OUTPUT_SLOT_INDEX -> true
				else -> false
			}
		}
	}

	override fun hasRecipe(): Boolean {
		val outputStack = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)
		if (outputStack.isNotEmpty()) return false

		val inputStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		return inputStack.isNotEmpty() && SyringeItem.isContaminated(inputStack)
	}

	override fun craftItem() {
		if (!hasRecipe()) return

		val syringeStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		SyringeItem.setContaminated(syringeStack, value = false)

		itemHandler.insertItem(OUTPUT_SLOT_INDEX, syringeStack.copy(), false)
		itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return BloodPurifierMenu(containerId, playerInventory, container, containerData)
	}

}