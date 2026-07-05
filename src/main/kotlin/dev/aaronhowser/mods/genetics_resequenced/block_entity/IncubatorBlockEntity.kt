package dev.aaronhowser.mods.genetics_resequenced.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.menu.incubator.IncubatorMenu
import dev.aaronhowser.mods.genetics_resequenced.recipe.base.IncubatorRecipe
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.DupeCellRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.IntSupplier

class IncubatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.INCUBATOR.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 10 }
	override val maxEnergy: Int = 50_000
	override val energyTransferRate: Int = 500

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, INVENTORY_SIZE) {
		override fun setChanged() {
			super.setChanged()
			currentProgress = 0
		}

		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			val level = level ?: return false

			return when (slot) {
				TOP_SLOT_INDEX -> IncubatorRecipe.isValidTopIngredient(level, stack)

				LEFT_BOTTLE_SLOT_INDEX,
				MIDDLE_BOTTLE_SLOT_INDEX,
				RIGHT_BOTTLE_SLOT_INDEX -> IncubatorRecipe.isValidBottomIngredient(level, stack)

				OVERCLOCKER_SLOT_INDEX -> stack.isItem(ModItems.OVERCLOCKER)

				else -> false
			}
		}
	}

	override val inputHandler: ResourceHandler<ItemResource> = insertOnlyHandler(TOP_SLOT_INDEX)
	private val bottleHandler: ResourceHandler<ItemResource> = insertAndExtractHandler(
		LEFT_BOTTLE_SLOT_INDEX,
		MIDDLE_BOTTLE_SLOT_INDEX,
		RIGHT_BOTTLE_SLOT_INDEX
	)

	override fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource>? {
		return when (direction) {
			Direction.UP -> inputHandler
			Direction.DOWN -> outputHandler
			else -> bottleHandler
		}
	}

	override fun getAmountOfOverclocks(): Int {
		return container.getItem(OVERCLOCKER_SLOT_INDEX).count
	}

	override fun serverTick() {
		if (!hasEnoughEnergy()) return
		if (!hasRecipe()) {
			currentProgress = 0
			return
		}

		maxProgress = getTicksPerBrew()

		extractEnergy(getEnergyCostPerTick())
		currentProgress += 1 + getAmountOfOverclocks()

		while (currentProgress >= maxProgress) {
			craftItem()
			currentProgress -= maxProgress
		}
	}

	override fun hasRecipe(): Boolean {
		val level = level ?: return false

		val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)
		if (topStack.isEmpty) return false

		val bottomStacks = listOf(
			itemHandler.getStackInSlot(LEFT_BOTTLE_SLOT_INDEX),
			itemHandler.getStackInSlot(MIDDLE_BOTTLE_SLOT_INDEX),
			itemHandler.getStackInSlot(RIGHT_BOTTLE_SLOT_INDEX)
		)

		return bottomStacks.any { bottomStack ->
			if (bottomStack.isEmpty) return@any false

			val input = IncubatorRecipe.Input(topStack, bottomStack, isHighTemp = true)
			IncubatorRecipe.hasIncubatorRecipe(level, input)
		}
	}

	override fun craftItem() {
		val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)

		val bottleSlots = listOf(
			LEFT_BOTTLE_SLOT_INDEX,
			MIDDLE_BOTTLE_SLOT_INDEX,
			RIGHT_BOTTLE_SLOT_INDEX
		)

		var onlyDupeCellRecipes = true

		for (slotIndex in bottleSlots) {
			val bottomStack = itemHandler.getStackInSlot(slotIndex)

			val recipeInput = IncubatorRecipe.Input(topStack, bottomStack, isHighTemp = true)
			val incubatorRecipe = IncubatorRecipe.getIncubatorRecipe(level!!, recipeInput)

			if (incubatorRecipe != null) {
				if (incubatorRecipe !is DupeCellRecipe) onlyDupeCellRecipes = false

				val output = incubatorRecipe.assemble(recipeInput)

				if (output.isNotEmpty()) {
					itemHandler.setStackInSlot(slotIndex, output)
				}
			} else {
				val potionBrewing = level!!.potionBrewing()
				val hasMix = potionBrewing.hasMix(bottomStack, topStack)

				if (hasMix) {
					onlyDupeCellRecipes = false

					val output = potionBrewing.mix(topStack, bottomStack)

					if (output.isNotEmpty()) {
						itemHandler.setStackInSlot(slotIndex, output)
					}
				}
			}
		}

		if (!onlyDupeCellRecipes) topStack.shrink(1)
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return IncubatorMenu(containerId, playerInventory, container, containerData)
	}

	companion object {
		const val INVENTORY_SIZE = 5

		const val TOP_SLOT_INDEX = 0
		const val LEFT_BOTTLE_SLOT_INDEX = 1
		const val MIDDLE_BOTTLE_SLOT_INDEX = 2
		const val RIGHT_BOTTLE_SLOT_INDEX = 3
		const val OVERCLOCKER_SLOT_INDEX = 4

		fun getTicksPerBrew(): Int = ServerConfig.CONFIG.incubatorTicksPerBrew.get()
	}

}
