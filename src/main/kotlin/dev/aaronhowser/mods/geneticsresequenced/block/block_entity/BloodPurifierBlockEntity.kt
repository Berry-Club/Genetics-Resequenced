package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.InvWrapper
import java.util.function.IntSupplier

class BloodPurifierBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.BLOOD_PURIFIER.get(), pos, blockState) {

	override val maxEnergy: Int = 60_000
	override val energyTransferRate: Int = 256
	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 32 }

	override val containerSize: Int = 3

	override val invWrapper = object : InvWrapper(container) {
		override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				INPUT_SLOT_INDEX -> SyringeItem.hasBlood(stack)
				OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER)
				OUTPUT_SLOT_INDEX -> false
				else -> false
			}
		}


	}

	override fun getItemHandler(direction: Direction?): InvWrapper? {
		return super.getItemHandler(direction)
	}

	override fun hasRecipe(): Boolean {
		TODO("Not yet implemented")
	}

	override fun craftItem() {
		TODO("Not yet implemented")
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		TODO("Not yet implemented")
	}

}