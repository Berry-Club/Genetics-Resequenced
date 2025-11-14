package dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.CoalGeneratorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import java.text.NumberFormat

class CoalGeneratorMenu(
	id: Int,
	playerInventory: Inventory,
	private val coalGeneratorContainer: Container,
	private val containerData: ContainerData,
	energyContainerData: ContainerData
) : MachineMenu(ModMenuTypes.COAL_GENERATOR.get(), id, playerInventory, energyContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CoalGeneratorBlockEntity.CONTAINER_SIZE),
		SimpleContainerData(CoalGeneratorBlockEntity.CONTAINER_DATA_SIZE),
		SimpleContainerData(MachineBlockEntity.ENERGY_CONTAINER_DATA_SIZE)
	)

	override val amountSlots: Int = CoalGeneratorBlockEntity.CONTAINER_SIZE

	var maxBurnTime: Int
		get() = containerData.get(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX)
		set(value) = containerData.set(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX, value)

	var burnTimeRemaining: Int
		get() = containerData.get(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX)
		set(value) = containerData.set(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX, value)

	init {
		checkContainerSize(coalGeneratorContainer, CoalGeneratorBlockEntity.CONTAINER_SIZE)
		addDataSlots(containerData)

		addPlayerInventorySlots(inventoryY)
		addSlots()
	}

	fun isBurning(): Boolean = burnTimeRemaining > 0

	override fun addSlots() {
		val slot = Slot(coalGeneratorContainer, CoalGeneratorBlockEntity.INPUT_SLOT_INDEX, 52, 40)
		addSlot(slot)
	}

	override fun getPercentDone(): Float {
		if (maxBurnTime == 0) return 0f

		return 1f - (burnTimeRemaining.toFloat() / maxBurnTime.toFloat())
	}

	override fun stillValid(player: Player): Boolean = coalGeneratorContainer.stillValid(player)

	companion object {
		fun showFuelTooltip(event: ItemTooltipEvent) {
			val itemStack = event.itemStack
			val fuelPer = itemStack.getBurnTime(RecipeType.SMELTING)
			if (fuelPer <= 0) return

			val feProducedPer = CoalGeneratorBlockEntity.getEnergyPerTick() * fuelPer
			val feStringPer = NumberFormat.getNumberInstance().format(feProducedPer)

			event.toolTip.add(
				1, Component.literal("$feStringPer FE").withStyle(ChatFormatting.GRAY)
			)

			val amount = itemStack.count
			if (amount > 1) {
				val feProducedTotal = feProducedPer * amount
				val feStringTotal = NumberFormat.getNumberInstance().format(feProducedTotal)

				event.toolTip.add(
					2,
					ModTooltipLang.COAL_GEN_TOTAL_FE
						.toComponent(feStringTotal)
						.withStyle(ChatFormatting.GRAY)
				)
			}

		}
	}
}