package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.DupeCellRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.InvWrapper
import java.util.function.IntSupplier
import kotlin.math.min

class AdvancedIncubatorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : CraftingMachineBlockEntity(ModBlockEntityTypes.ADVANCED_INCUBATOR.get(), pos, blockState) {

	override val baseEnergyCostPerTick: IntSupplier = IntSupplier { 10 }
	override val maxEnergy: Int = 50_000
	override val energyTransferRate: Int = 500

	private var isHighTemperature: Boolean = false
		set(value) {
			if (field != value) {
				field = value
				currentProgress = 0
				setChanged()
			}
		}

	override val container: ImprovedSimpleContainer = object : ImprovedSimpleContainer(this, INVENTORY_SIZE) {
		override fun setChanged() {
			super.setChanged()
			currentProgress = 0
		}
	}

	//TODO: Reset brew time when overclock changed

	override val itemHandler: InvWrapper = object : InvWrapper(container) {
		override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
			val level = level ?: return false

			return when (slot) {
				TOP_SLOT_INDEX -> AbstractIncubatorRecipe.isValidTopIngredient(level, stack)

				LEFT_BOTTLE_SLOT_INDEX,
				MIDDLE_BOTTLE_SLOT_INDEX,
				RIGHT_BOTTLE_SLOT_INDEX -> AbstractIncubatorRecipe.isValidBottomIngredient(level, stack)

				OVERCLOCKER_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER)

				CHORUS_SLOT_INDEX -> stack.`is`(Items.CHORUS_FRUIT)

				else -> false
			}
		}
	}

	private var subTicks = 0
	override fun serverTick() {
		if (!hasRecipe()) {
			subTicks = 0
			currentProgress = 0
			return
		}

		if (isHighTemperature) {
			energyStorage.extractEnergy(getEnergyCostPerTick(), false)
			currentProgress += 1 + getAmountOfOverclocks()
		} else {
			subTicks += 1 + getAmountOfOverclocks()

			val ticksOverMax = subTicks - getIncubatorLowTemperatureTickFactor()
			if (ticksOverMax >= 0) {
				subTicks = ticksOverMax
				energyStorage.extractEnergy(getEnergyCostPerTick(), false)
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
			val input = IncubatorRecipeInput(topStack, bottomStack, isHighTemp = isHighTemperature)
			AbstractIncubatorRecipe.hasIncubatorRecipe(level!!, input)
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

			val incubatorInput = IncubatorRecipeInput(
				topStack,
				bottomStack,
				isHighTemp = isHighTemperature
			)

			val incubatorRecipe = AbstractIncubatorRecipe.getIncubatorRecipe(level!!, incubatorInput)

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


	private fun gmoRecipeOutput(gmoRecipe: GmoRecipe, input: IncubatorRecipeInput): ItemStack {
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

		val nextFloat = level.random.nextFloat()

		return if (nextFloat <= finalChance) {
			gmoRecipe.assemble(input, level.registryAccess())
		} else {
			gmoRecipe.getFailure(level.registryAccess())
		}
	}

	private fun nonGmoRecipeOutput(incubatorRecipe: AbstractIncubatorRecipe): ItemStack {
		val level = level ?: return ItemStack.EMPTY

		val output = incubatorRecipe.assemble(
			IncubatorRecipeInput(
				itemHandler.getStackInSlot(TOP_SLOT_INDEX),
				itemHandler.getStackInSlot(LEFT_BOTTLE_SLOT_INDEX),
				isHighTemp = this.isHighTemperature
			),
			level.registryAccess()
		)

		return output
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		TODO("Not yet implemented")
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putBoolean(IS_HIGH_TEMPERATURE_TAG, isHighTemperature)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		isHighTemperature = tag.getBoolean(IS_HIGH_TEMPERATURE_TAG)
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

		fun getIncubatorLowTemperatureTickFactor(): Int = ServerConfig.CONFIG.incubatorLowTempTickFactor.get()
	}

}