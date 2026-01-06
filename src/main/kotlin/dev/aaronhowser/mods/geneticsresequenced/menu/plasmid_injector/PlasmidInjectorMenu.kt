package dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_injector

import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.ChatFormatting
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraftforge.event.entity.player.ItemTooltipEvent

class PlasmidInjectorMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.PLASMID_INJECTOR.get(), containerId, playerInventory, machineContainer, craftingContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(CraftingContainerData.CRAFTING_CONTAINER_DATA_SIZE)
	)

	init {
		addSlots()
	}

	override fun inputFilter(inputStack: ItemStack): Boolean = PlasmidItem.isComplete(inputStack)

	private fun syringeFilter(syringeStack: ItemStack): Boolean {
		return syringeStack.isItem(ModItemTagsProvider.SYRINGES) && !SyringeItem.isContaminated(syringeStack)
	}

	override fun addSlots() {
		val plasmidSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42, ::inputFilter)
		val syringeSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42, ::syringeFilter)
		val overclockSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54) { it.isItem(ModItems.OVERCLOCKER) }

		this.addSlot(plasmidSlot)
		this.addSlot(syringeSlot)
		this.addSlot(overclockSlot)
	}

	companion object {
		fun showTooltip(event: ItemTooltipEvent) {
			val hoverStack = event.itemStack
			if (hoverStack.item != ModItems.SYRINGE.get()) return

			if (SyringeItem.isContaminated(hoverStack)) {
				event.toolTip.add(
					2,
					ModTooltipLang.INJECTOR_CONTAMINATED
						.toComponent()
						.withStyle(ChatFormatting.RED)
				)
			}
		}
	}

}