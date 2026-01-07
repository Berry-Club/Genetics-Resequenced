package dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator

import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.CraftingContainerData
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
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import java.text.NumberFormat

class CoalGeneratorMenu(
	id: Int,
	playerInventory: Inventory,
	private val coalGeneratorContainer: Container,
	private val craftingContainerData: ContainerData
) : MachineMenu(ModMenuTypes.COAL_GENERATOR.get(), id, playerInventory, craftingContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CoalGeneratorBlockEntity.CONTAINER_SIZE),
		SimpleContainerData(CraftingContainerData.CRAFTING_CONTAINER_DATA_SIZE)
	)

	override val amountSlots: Int = CoalGeneratorBlockEntity.CONTAINER_SIZE

	fun getMaxBurnTime(): Int = craftingContainerData.get(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX)
	fun getBurnTimeRemaining(): Int = craftingContainerData.get(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX)

	fun isBurning(): Boolean = getBurnTimeRemaining() > 0

	init {
		checkContainerSize(coalGeneratorContainer, CoalGeneratorBlockEntity.CONTAINER_SIZE)
		addSlots()
	}

	override fun addSlots() {
		val slot = FilteredSlot(coalGeneratorContainer, CoalGeneratorBlockEntity.INPUT_SLOT_INDEX, 52, 40) {
			ForgeHooks.getBurnTime(it, RecipeType.SMELTING) > 0
		}
		addSlot(slot)
	}

	override fun getPercentDone(): Float {
		val max = getMaxBurnTime()
		if (max == 0) return 0f

		return 1f - (getBurnTimeRemaining().toFloat() / max.toFloat())
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