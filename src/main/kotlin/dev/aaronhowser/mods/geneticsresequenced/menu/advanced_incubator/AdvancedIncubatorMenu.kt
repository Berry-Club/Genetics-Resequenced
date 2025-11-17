package dev.aaronhowser.mods.geneticsresequenced.menu.advanced_incubator

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.menu.MenuWithButtons
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.AdvancedIncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.CommonComponents
import net.minecraft.util.Mth
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Items
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import kotlin.math.min

class AdvancedIncubatorMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.ADVANCED_INCUBATOR.get(), containerId, playerInventory, machineContainer, craftingContainerData), MenuWithButtons {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(AdvancedIncubatorBlockEntity.INVENTORY_SIZE),
		SimpleContainerData(AdvancedIncubatorBlockEntity.CONTAINER_DATA_SIZE)
	)

	init {
		addSlots()

		checkContainerDataCount(craftingContainerData, AdvancedIncubatorBlockEntity.CONTAINER_DATA_SIZE)
	}

	fun isHighTemperature(): Boolean {
		return machineContainerData.get(AdvancedIncubatorBlockEntity.IS_HIGH_TEMPERATURE_INDEX) == 1
	}

	fun setIsHighTemperature(value: Boolean) {
		machineContainerData.set(AdvancedIncubatorBlockEntity.IS_HIGH_TEMPERATURE_INDEX, if (value) 1 else 0)
	}

	override fun addSlots() {
		val level = AaronClientUtil.localLevel ?: return

		val topSlot = FilteredSlot(machineContainer, AdvancedIncubatorBlockEntity.TOP_SLOT_INDEX, 83, 21) { AbstractIncubatorRecipe.isValidTopIngredient(level, it) }
		val leftBottleSlot = FilteredSlot(machineContainer, AdvancedIncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 55) { AbstractIncubatorRecipe.isValidBottomIngredient(level, it) }
		val middleBottleSlot = FilteredSlot(machineContainer, AdvancedIncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 62) { AbstractIncubatorRecipe.isValidBottomIngredient(level, it) }
		val rightBottleSlot = FilteredSlot(machineContainer, AdvancedIncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 55) { AbstractIncubatorRecipe.isValidBottomIngredient(level, it) }
		val chorusSlot = FilteredSlot(machineContainer, AdvancedIncubatorBlockEntity.CHORUS_SLOT_INDEX, 141, 30) { it.`is`(Items.CHORUS_FRUIT) }

		val overclockerSlot = FilteredSlot(machineContainer, AdvancedIncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 141, 60) { it.`is`(ModItems.OVERCLOCKER) }

		this.addSlot(topSlot)
		this.addSlot(leftBottleSlot)
		this.addSlot(middleBottleSlot)
		this.addSlot(rightBottleSlot)
		this.addSlot(overclockerSlot)
		this.addSlot(chorusSlot)
	}

	override fun handleButtonPressed(buttonId: Int) {
		when (buttonId) {
			CYCLE_TEMPERATURE_BUTTON_ID -> {
				val newTemperature = !isHighTemperature()
				setIsHighTemperature(newTemperature)
			}
		}
	}

	companion object {
		const val CYCLE_TEMPERATURE_BUTTON_ID = 0

		fun showChanceTooltip(event: ItemTooltipEvent) {
			val level = AaronClientUtil.localLevel ?: return

			val potionStack = event.itemStack

			val potion = OtherUtil.getPotion(potionStack) ?: return
			if (potion != ModPotions.CELL_GROWTH && potion != ModPotions.MUTATION) return

			val player = event.entity ?: return
			val menu = player.containerMenu as? AdvancedIncubatorMenu ?: return

			val topStack = menu.machineContainer.getItem(AdvancedIncubatorBlockEntity.TOP_SLOT_INDEX)

			val recipe = GmoRecipe.getGmoRecipe(
				level,
				topStack,
				potionStack,
				menu.isHighTemperature()   // TODO: See if this works
			) ?: return

			val chanceDecreasePerOverclocker = ServerConfig.CONFIG.incubatorOverclockerChanceDecrease.get().toFloat()
			val chanceIncreasePerChorus = ServerConfig.CONFIG.incubatorChorusFruitChanceIncrease.get().toFloat()

			val baseChance = recipe.geneChance

			val amountOverclockers = menu.machineContainer
				.getItem(AdvancedIncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX)
				.count
			val overclockerChanceFactor =
				1 - amountOverclockers * chanceDecreasePerOverclocker
			val reducedChance = (baseChance * overclockerChanceFactor).coerceIn(0f, 1f)

			val chorusRequiredForMaxChance = Mth.ceil((1f - reducedChance) / chanceIncreasePerChorus)
			val chorusAvailable = menu.machineContainer
				.getItem(AdvancedIncubatorBlockEntity.CHORUS_SLOT_INDEX)
				.count
			val chorusUsed = min(chorusRequiredForMaxChance, chorusAvailable)

			val chorusBoost = chorusUsed * chanceIncreasePerChorus
			val finalChance = reducedChance + chorusBoost

			var index = event.toolTip.size

			event.toolTip.add(
				index++,
				CommonComponents.EMPTY
			)

			event.toolTip.add(
				index++,
				ModTooltipLang.GMO_BASE_CHANCE
					.toComponent(
						Gene.getNameComponent(recipe.idealGeneRk),
						(baseChance * 100).toInt()
					)
					.withStyle(ChatFormatting.GRAY)
			)

			if (amountOverclockers != 0) {
				event.toolTip.add(
					index++,
					ModTooltipLang.GMO_OVERCLOCKER_CHANCE
						.toComponent(
							amountOverclockers,
							(reducedChance * 100).toInt()
						)
						.withStyle(ChatFormatting.GRAY)
				)
			}

			if (chorusUsed != 0) {
				event.toolTip.add(
					index,
					ModTooltipLang.GMO_CHORUS_CHANCE
						.toComponent(
							chorusUsed,
							(finalChance * 100).toInt()
						)
						.withStyle(ChatFormatting.GRAY)
				)
			}
		}

	}

}