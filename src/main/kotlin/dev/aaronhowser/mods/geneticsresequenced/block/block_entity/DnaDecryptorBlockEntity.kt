package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.menu.dna_decryptor.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

class DnaDecryptorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.DNA_DECRYPTOR.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 32 }
	override val maxEnergy: Int = 60_000
	override val energyTransferRate: Int = 256

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, DEFAULT_INVENTORY_SIZE) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				INPUT_SLOT_INDEX -> stack.`is`(ModItems.DNA_HELIX)
				OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER)
				OUTPUT_SLOT_INDEX -> false
				else -> false
			}
		}
	}

	private var isNextGeneSet = false
	private var nextGeneHolder: Holder<Gene>? = null

	override fun hasRecipe(): Boolean {
		val level = this.level ?: return false

		val inputStack = container.getItem(INPUT_SLOT_INDEX)
		if (!inputStack.`is`(ModItems.DNA_HELIX)) return false

		if (inputStack.has(ModDataComponents.GENE)) return false

		val outputItem = getOutputFromInput(inputStack, level.registryAccess()) ?: return false
		return outputSlotHasRoom(outputItem)
	}

	private fun getOutputFromInput(inputStack: ItemStack, registries: HolderLookup.Provider): ItemStack? {
		val level = this.level ?: return null
		val possibleGenes = getPossibleGenes(inputStack, registries)

		val geneHolder: Holder<Gene>

		if (!isNextGeneSet) {
			val index = level.random.nextInt(possibleGenes.size)
			geneHolder = possibleGenes[index]

			nextGeneHolder = geneHolder
			isNextGeneSet = true
		} else {
			if (nextGeneHolder !in possibleGenes) {
				isNextGeneSet = false
				return null
			}

			if (nextGeneHolder != null) {
				geneHolder = nextGeneHolder!!
			} else {
				return null
			}
		}

		val helixStack = DnaHelixItem.getHelixStack(geneHolder)
		return helixStack
	}

	private fun getPossibleGenes(input: ItemStack, registries: HolderLookup.Provider): List<Holder<Gene>> {
		val basic = ModGenes.BASIC.getHolderOrThrow(registries)
		val mobType = EntityDnaItem.getEntityType(input) ?: return listOf(basic)

		val genesFromMob = EntityGenes.getGeneHolderWeights(mobType, registries)

		return genesFromMob
			.map { it.key }
			.filterNot { it.isDisabled }
	}

	override fun craftItem() {
		val level = this.level ?: return

		val inputStack = container.getItem(INPUT_SLOT_INDEX)
		val outputStack = getOutputFromInput(inputStack, level.registryAccess()) ?: return

		itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
		itemHandler.insertItem(OUTPUT_SLOT_INDEX, outputStack, false)

		isNextGeneSet = false
		nextGeneHolder = null
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return DnaDecryptorMenu(containerId, playerInventory, container, containerData)
	}
}