package dev.aaronhowser.mods.genetics_resequenced.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.genetics_resequenced.block_entity.base.container_data.EnergyProgressContainerData
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.menu.advanced_incubator.AdvancedIncubatorMenu
import dev.aaronhowser.mods.genetics_resequenced.recipe.base.IncubatorRecipe
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.DupeCellRecipe
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import java.util.function.IntSupplier
import kotlin.math.min

class AdvancedIncubatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.ADVANCED_INCUBATOR.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 10 }
	override val maxEnergy: Int = 50_000
	override val energyTransferRate: Int = 500

	var isHighTemperature: Boolean = false
		private set(value) {
			if (field != value) {
				field = value
				currentProgress = 0
				setChanged()
			}
		}

	override val container: ImprovedSimpleContainer =
		object : ImprovedSimpleContainer(this, INVENTORY_SIZE) {
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

					CHORUS_SLOT_INDEX -> stack.isItem(Items.CHORUS_FRUIT)

					else -> false
				}
			}
		}

	override val containerData: ContainerData = object : EnergyProgressContainerData(
		energyStorage, ::currentProgress, ::maxProgress
	) {

		override fun get(index: Int): Int {
			return when (index) {
				IS_HIGH_TEMPERATURE_INDEX -> if (isHighTemperature) 1 else 0
				else -> super.get(index)
			}
		}

		override fun set(index: Int, value: Int) {
			when (index) {
				IS_HIGH_TEMPERATURE_INDEX -> isHighTemperature = value != 0
				else -> super.set(index, value)
			}
		}

		override fun getCount(): Int = CONTAINER_DATA_SIZE
	}

	override val inputHandler: ResourceHandler<ItemResource> = insertOnlyHandler(TOP_SLOT_INDEX)
	private val bottleHandler: ResourceHandler<ItemResource> = insertAndExtractHandler(
		LEFT_BOTTLE_SLOT_INDEX,
		MIDDLE_BOTTLE_SLOT_INDEX,
		RIGHT_BOTTLE_SLOT_INDEX
	)
	override val overclockHandler: ResourceHandler<ItemResource> = insertAndExtractHandler(OVERCLOCKER_SLOT_INDEX)
	override val outputHandler: ResourceHandler<ItemResource> = bottleHandler

	override fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource>? {
		return when (direction) {
			Direction.UP -> inputHandler
			else -> bottleHandler
		}
	}

	override fun getAmountOfOverclocks(): Int {
		return container.getItem(OVERCLOCKER_SLOT_INDEX).count
	}

	private var subTicks = 0
	override fun serverTick() {
		if (!hasRecipe()) {
			subTicks = 0
			currentProgress = 0
			return
		}

		maxProgress = IncubatorBlockEntity.getTicksPerBrew()

		if (isHighTemperature) {
			extractEnergy(getEnergyCostPerTick())
			currentProgress += 1 + getAmountOfOverclocks()
		} else {
			subTicks += 1 + getAmountOfOverclocks()

			val tickFactor = ServerConfig.CONFIG.incubatorLowTempTickFactor.get()
			val ticksOverMax = subTicks - tickFactor
			if (ticksOverMax >= 0) {
				subTicks = ticksOverMax
				extractEnergy(getEnergyCostPerTick())
				currentProgress += 1
			}
		}

		if (currentProgress >= maxProgress) {
			craftItem()
		}
	}

	override fun hasRecipe(): Boolean {
		if (!hasEnoughEnergy()) return false

		val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)
		if (topStack.isEmpty) return false

		val bottomStacks = listOf(
			itemHandler.getStackInSlot(LEFT_BOTTLE_SLOT_INDEX),
			itemHandler.getStackInSlot(MIDDLE_BOTTLE_SLOT_INDEX),
			itemHandler.getStackInSlot(RIGHT_BOTTLE_SLOT_INDEX)
		)

		return bottomStacks.any { bottomStack ->
			if (bottomStack.isEmpty) return@any false
			val input = IncubatorRecipe.Input(topStack, bottomStack, isHighTemp = isHighTemperature)
			IncubatorRecipe.hasIncubatorRecipe(level!!, input)
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

			val incubatorInput = IncubatorRecipe.Input(
				topStack,
				bottomStack,
				isHighTemp = isHighTemperature,
				registryAccess = level!!.registryAccess()
			)

			val incubatorRecipe = IncubatorRecipe.getIncubatorRecipe(level!!, incubatorInput)

			if (incubatorRecipe != null) {
				if (incubatorRecipe !is DupeCellRecipe) onlyDupeCellRecipes = false

				val output = if (incubatorRecipe is GmoRecipe) {
					gmoRecipeOutput(incubatorRecipe, incubatorInput)
				} else {
					nonGmoRecipeOutput(incubatorRecipe)
				}

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


	private fun gmoRecipeOutput(gmoRecipe: GmoRecipe, input: IncubatorRecipe.Input): ItemStack {
		val level = level ?: return ItemStack.EMPTY

		val chanceDecreasePerOverclocker = ServerConfig.CONFIG.incubatorOverclockerChanceDecrease.get().toFloat()
		val chanceIncreasePerChorus = ServerConfig.CONFIG.incubatorChorusFruitChanceIncrease.get().toFloat()

		// The base chance
		val geneChance = gmoRecipe.geneChance

		// Reduce the chance based on the amount of Overclockers (1.0 means no change)
		val overclockerChanceFactor = 1f - getAmountOfOverclocks() * chanceDecreasePerOverclocker
		val reducedChance = (geneChance * overclockerChanceFactor).coerceIn(0f, 1f)

		// Increase the chance based on the amount of Chorus Fruit
		val chorusRequiredForMaxChance = Mth.ceil((1f - reducedChance) / chanceIncreasePerChorus)
		val chorusAvailable = itemHandler.getStackInSlot(CHORUS_SLOT_INDEX).count
		val chorusUsed = min(chorusRequiredForMaxChance, chorusAvailable)

		itemHandler.getStackInSlot(CHORUS_SLOT_INDEX).shrink(chorusUsed)

		val chorusBoost = chorusUsed * chanceIncreasePerChorus
		val finalChance = reducedChance + chorusBoost

		return if (level.random.chance(finalChance)) {
			gmoRecipe.assemble(input)
		} else {
			gmoRecipe.getFailure(level.registryAccess())
		}
	}

	private fun nonGmoRecipeOutput(incubatorRecipe: IncubatorRecipe): ItemStack {
		val level = level ?: return ItemStack.EMPTY

		val output = incubatorRecipe.assemble(
			IncubatorRecipe.Input(
				itemHandler.getStackInSlot(TOP_SLOT_INDEX),
				itemHandler.getStackInSlot(LEFT_BOTTLE_SLOT_INDEX),
				isHighTemp = this.isHighTemperature
			)
		)

		return output
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return AdvancedIncubatorMenu(containerId, playerInventory, this.container, this.containerData)
	}

	override fun saveAdditional(output: ValueOutput) {
		super.saveAdditional(output)
		output.putBoolean(IS_HIGH_TEMPERATURE_TAG, isHighTemperature)
	}

	override fun loadAdditional(input: ValueInput) {
		super.loadAdditional(input)
		isHighTemperature = input.getBooleanOr(IS_HIGH_TEMPERATURE_TAG, false)
	}

	companion object {
		const val IS_HIGH_TEMPERATURE_TAG = "IsHighTemperature"

		const val INVENTORY_SIZE = 6

		const val TOP_SLOT_INDEX = 0
		const val LEFT_BOTTLE_SLOT_INDEX = 1
		const val MIDDLE_BOTTLE_SLOT_INDEX = 2
		const val RIGHT_BOTTLE_SLOT_INDEX = 3
		const val CHORUS_SLOT_INDEX = 4
		const val OVERCLOCKER_SLOT_INDEX = 5

		const val CONTAINER_DATA_SIZE = 5
		const val IS_HIGH_TEMPERATURE_INDEX = 4
	}

}
