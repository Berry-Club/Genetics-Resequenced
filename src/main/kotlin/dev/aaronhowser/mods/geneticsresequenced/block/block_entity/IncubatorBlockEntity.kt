package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.DupeCellRecipe
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

//	override fun serverTick() {
//		if (!hasEnoughEnergy()) return
//		if (!hasRecipe()) {
//			currentProgress = 0
//			return
//		}
//
//		val isBrewing = currentProgress > 0
//
//		if (!isBrewing) {
//			currentProgress = 0
//			maxProgress = getTicksPerBrew()
//		}
//
//		energyStorage.extractEnergy(getEnergyCostPerTick(), false)
//		currentProgress += 1 + getAmountOfOverclocks()
//
//		while (currentProgress >= maxProgress) {
//			craftItem()
//			currentProgress -= maxProgress
//		}
//	}

	override fun hasRecipe(): Boolean {
		val level = level ?: return false

		val topStack = invWrapper.getStackInSlot(TOP_SLOT_INDEX)
		if (topStack.isEmpty) return false

		val bottomStacks = listOf(
			invWrapper.getStackInSlot(LEFT_BOTTLE_SLOT_INDEX),
			invWrapper.getStackInSlot(MIDDLE_BOTTLE_SLOT_INDEX),
			invWrapper.getStackInSlot(RIGHT_BOTTLE_SLOT_INDEX)
		)

		return bottomStacks.any { bottomStack ->
			if (bottomStack.isEmpty) return@any false

			val input = IncubatorRecipeInput(topStack, bottomStack, isHighTemp = true)
			AbstractIncubatorRecipe.hasIncubatorRecipe(level, input)
		}
	}

	override fun craftItem() {
		val topStack = invWrapper.getStackInSlot(TOP_SLOT_INDEX)

		val bottleSlots = listOf(
			LEFT_BOTTLE_SLOT_INDEX,
			MIDDLE_BOTTLE_SLOT_INDEX,
			RIGHT_BOTTLE_SLOT_INDEX
		)

		var onlyDupeCellRecipes = true

		for (slotIndex in bottleSlots) {
			val bottomStack = invWrapper.getStackInSlot(slotIndex)

			val recipeInput = IncubatorRecipeInput(topStack, bottomStack, isHighTemp = true)
			val incubatorRecipe = AbstractIncubatorRecipe.getIncubatorRecipe(level!!, recipeInput)

			if (incubatorRecipe != null) {
				if (incubatorRecipe !is DupeCellRecipe) onlyDupeCellRecipes = false

				val output = incubatorRecipe.assemble(recipeInput, level!!.registryAccess())

				if (!output.isEmpty) {
					invWrapper.setStackInSlot(slotIndex, output)
				}
			} else {
				val potionBrewing = level!!.potionBrewing()
				val hasMix = potionBrewing.hasMix(bottomStack, topStack)

				if (hasMix) {
					onlyDupeCellRecipes = false

					val output = potionBrewing.mix(topStack, bottomStack)

					if (!output.isEmpty) {
						invWrapper.setStackInSlot(slotIndex, output)
					}
				}
			}
		}

		if (!onlyDupeCellRecipes) topStack.shrink(1)
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

		fun getTicksPerBrew(): Int = ServerConfig.CONFIG.incubatorTicksPerBrew.get()
	}

}