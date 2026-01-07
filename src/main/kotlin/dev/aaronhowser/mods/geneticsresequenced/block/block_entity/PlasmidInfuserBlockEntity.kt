package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.AaronExtensions.isTrue
import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.components.GeneDataComponent
import dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

class PlasmidInfuserBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.PLASMID_INFUSER.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 32 }
	override val maxEnergy: Int = 60_000
	override val energyTransferRate: Int = 256

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, DEFAULT_INVENTORY_SIZE) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				INPUT_SLOT_INDEX -> stack.isItem(ModItems.DNA_HELIX)
				OVERCLOCK_SLOT_INDEX -> stack.isItem(ModItems.OVERCLOCKER)
				OUTPUT_SLOT_INDEX -> stack.isItem(ModItems.PLASMID)
				else -> false
			}
		}
	}

	override fun hasRecipe(): Boolean {
		val inputHelix = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		val outputPlasmid = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

		if (!inputHelix.isItem(ModItems.DNA_HELIX) || !outputPlasmid.isItem(ModItems.PLASMID)) return false

		if (PlasmidItem.isComplete(outputPlasmid)) return false

		val plasmidGeneHolder = PlasmidItem.getGeneRk(outputPlasmid)
		val inputGeneHolder = GeneDataComponent.getGeneRk(inputHelix)

		val helixIsBasic = inputGeneHolder?.isGene(ModGenes.BASIC).isTrue()

		// If the Plasmid is unset, it can only accept a Helix that's neither basic nor null
		if (plasmidGeneHolder == null) {
			return !helixIsBasic && inputGeneHolder != null
		}

		if (!helixIsBasic) {
			if (inputGeneHolder != plasmidGeneHolder) return false
		}

		return true
	}

	override fun craftItem() {
		val inputHelix = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
		val outputPlasmid = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

		val plasmidGeneHolder = PlasmidItem.getGeneRk(outputPlasmid)
		val inputGeneRk = GeneDataComponent.getGeneRk(inputHelix) ?: return

		// If Plasmid is unset, set it to the Helix's gene and initialize the amount
		if (plasmidGeneHolder == null) {
			PlasmidItem.setGene(outputPlasmid, inputGeneRk, 0)

			itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
			return
		}

		val increaseAmount = when {
			inputGeneRk.isGene(ModGenes.BASIC) -> 1
			inputGeneRk.isGene(plasmidGeneHolder) -> 2
			else -> return
		}

		PlasmidItem.increaseDnaPoints(outputPlasmid, increaseAmount)

		itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return PlasmidInfuserMenu(containerId, playerInventory, container, containerData)
	}
}