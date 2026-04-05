package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem.Companion.setEntityType
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_extractor.DnaExtractorMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

class DnaExtractorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.DNA_EXTRACTOR.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 32 }
	override val maxEnergy: Int = 60_000
	override val energyTransferRate: Int = 256

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, DEFAULT_INVENTORY_SIZE) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				INPUT_SLOT_INDEX -> stack.isItem(ModItems.CELL) || stack.isItem(ModItems.GMO_CELL)
				OVERCLOCK_SLOT_INDEX -> stack.isItem(ModItems.OVERCLOCKER)
				OUTPUT_SLOT_INDEX -> true
				else -> false
			}
		}
	}

	override fun hasRecipe(): Boolean {
		val inputStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		if (!inputStack.isItem(ModItems.CELL) && !inputStack.isItem(ModItems.GMO_CELL)) {
			return false
		}

		val outputStack = getOutputFromInput(inputStack) ?: return false
		return outputSlotHasRoom(outputStack)
	}

	private fun getOutputFromInput(input: ItemStack): ItemStack? {
		if (input.isItem(ModItems.GMO_CELL)) {
			val geneHolder = DnaHelixItem.getGeneHolder(input) ?: return null
			return DnaHelixItem.getHelixStack(geneHolder)
		}

		if (input.isItem(ModItems.CELL)) {
			val mobType = EntityDnaItem.getEntityType(input) ?: return null
			val dnaStack = ModItems.DNA_HELIX.toStack()

			val setWorked = setEntityType(dnaStack, mobType)
			if (!setWorked) {
				GeneticsResequenced.LOGGER.error("A DNA Extractor tried to set an invalid entity type at ${blockPos.x}, ${blockPos.y}, ${blockPos.z}: ${mobType.descriptionId}")
				return null
			}

			return dnaStack
		}

		return null
	}

	override fun craftItem() {
		val inputStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		val outputStack = getOutputFromInput(inputStack) ?: return

		itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
		itemHandler.insertItem(OUTPUT_SLOT_INDEX, outputStack, false)
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return DnaExtractorMenu(containerId, playerInventory, container, containerData)
	}
}