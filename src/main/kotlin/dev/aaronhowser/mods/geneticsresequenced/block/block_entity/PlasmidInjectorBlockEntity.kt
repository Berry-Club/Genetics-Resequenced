package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_injector.PlasmidInjectorMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

class PlasmidInjectorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.PLASMID_INJECTOR.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 32 }
	override val maxEnergy: Int = 60_000
	override val energyTransferRate: Int = 256

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, DEFAULT_INVENTORY_SIZE) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				INPUT_SLOT_INDEX -> {
					val isIncompletePlasmid = stack.`is`(ModItems.PLASMID) && PlasmidItem.isComplete(stack)
					isIncompletePlasmid || (stack.`is`(ModItems.ANTI_PLASMID))
				}

				OUTPUT_SLOT_INDEX ->
					SyringeItem.hasBlood(stack) && !SyringeItem.isContaminated(stack)

				OVERCLOCK_SLOT_INDEX ->
					stack.item == ModItems.OVERCLOCKER.get()

				else -> false
			}
		}
	}

	override fun hasRecipe(): Boolean {
		val plasmidStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		val syringeStack = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

		if (!syringeStack.isSyringe()) return false

		if (plasmidStack.`is`(ModItems.PLASMID)) {
			val plasmidGene = PlasmidItem.getGene(plasmidStack) ?: return false
			if (!PlasmidItem.isComplete(plasmidStack)) return false
			return SyringeItem.canAddGene(syringeStack, plasmidGene)
		}

		if (plasmidStack.`is`(ModItems.ANTI_PLASMID)) {
			val antiPlasmidAntigene = PlasmidItem.getGene(plasmidStack) ?: return false
			return SyringeItem.canAddAntigene(syringeStack, antiPlasmidAntigene)
		}

		return false
	}

	override fun craftItem() {
		val plasmidStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		val syringeStack = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

		val plasmidGene = PlasmidItem.getGene(plasmidStack) ?: return

		if (plasmidStack.`is`(ModItems.PLASMID)) {
			SyringeItem.addGene(syringeStack, plasmidGene)
		} else if (plasmidStack.`is`(ModItems.ANTI_PLASMID)) {
			SyringeItem.addAntigene(syringeStack, plasmidGene)
		}

		itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return PlasmidInjectorMenu(containerId, playerInventory, this.container, this.containerData)
	}
}