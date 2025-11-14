package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
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

class IncubatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.INCUBATOR.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 10 }
	override val maxEnergy: Int = 50_000
	override val energyTransferRate: Int = 500

	override val containerSize: Int = INVENTORY_SIZE

	override val invWrapper: InvWrapper = object : InvWrapper(container) {
		override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
			val level = level ?: return false

			return when (slot) {
				TOP_SLOT_INDEX -> AbstractIncubatorRecipe.isValidTopIngredient(level, stack)

				LEFT_BOTTLE_SLOT_INDEX,
				MIDDLE_BOTTLE_SLOT_INDEX,
				RIGHT_BOTTLE_SLOT_INDEX -> AbstractIncubatorRecipe.isValidBottomIngredient(level, stack)

				OVERCLOCKER_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER)

				else -> false
			}
		}
	}

	override fun hasRecipe(): Boolean {
		val topStack = invWrapper.getStackInSlot(TOP_SLOT_INDEX)
		if (topStack.isEmpty) return false
	}

	override fun craftItem() {
		TODO("Not yet implemented")
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		TODO("Not yet implemented")
	}

	companion object {
		const val INVENTORY_SIZE = 5

		const val TOP_SLOT_INDEX = 0
		const val LEFT_BOTTLE_SLOT_INDEX = 1
		const val MIDDLE_BOTTLE_SLOT_INDEX = 2
		const val RIGHT_BOTTLE_SLOT_INDEX = 3
		const val OVERCLOCKER_SLOT_INDEX = 4
	}

}