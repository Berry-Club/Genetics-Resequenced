package dev.aaronhowser.mods.geneticsresequenced.menu.plasmid_infuser

import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.MachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.CraftingContainerData
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.menu.CraftingMachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModDataComponents
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.ChatFormatting
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

class PlasmidInfuserMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	craftingContainerData: ContainerData
) : CraftingMachineMenu(ModMenuTypes.PLASMID_INFUSER.get(), containerId, playerInventory, machineContainer, craftingContainerData) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(CraftingMachineBlockEntity.DEFAULT_INVENTORY_SIZE),
		SimpleContainerData(CraftingContainerData.CRAFTING_CONTAINER_DATA_SIZE)
	)

	init {
		addSlots()
	}

	override fun inputFilter(inputStack: ItemStack): Boolean {
		return inputStack.`is`(ModItems.DNA_HELIX) && inputStack.has(ModDataComponents.GENE)
	}

	override fun addSlots() {
		val helixSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42, ::inputFilter)
		val plasmidSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42) { it.`is`(ModItems.PLASMID) }
		val overclockSlot = FilteredSlot(machineContainer, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54) { it.`is`(ModItems.OVERCLOCKER) }

		this.addSlot(helixSlot)
		this.addSlot(plasmidSlot)
		this.addSlot(overclockSlot)
	}

	companion object {
		fun showTooltip(event: ItemTooltipEvent) {
			val hoverStack = event.itemStack
			if (hoverStack.item != ModItems.DNA_HELIX.get()) return

			val hoveredGeneHolder = DnaHelixItem.getGeneHolder(hoverStack) ?: return

			val slots = event.entity?.containerMenu?.slots ?: return
			val plasmidSlotId = 37  //Evil magic number that i got by printing whatever slot I was hovering

			val outputItem = slots.getOrNull(plasmidSlotId)?.item ?: return
			val outputGene = PlasmidItem.getGene(outputItem) ?: return

			val component = when {
				hoveredGeneHolder.isGene(ModGenes.BASIC) -> ModTooltipLang.INFUSER_BASIC.toComponent()

				hoveredGeneHolder.isGene(outputGene) -> ModTooltipLang.INFUSER_MATCHING.toComponent()

				else -> ModTooltipLang.INFUSER_MISMATCH.toComponent()
			}.withStyle(ChatFormatting.GRAY)

			event.toolTip.add(2, component)
		}

	}

}