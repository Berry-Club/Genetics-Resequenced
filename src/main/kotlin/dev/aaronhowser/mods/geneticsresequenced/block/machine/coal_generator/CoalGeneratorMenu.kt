package dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModTooltipLang
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.ChatFormatting
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.items.SlotItemHandler
import java.text.NumberFormat

class CoalGeneratorMenu(
	id: Int,
	inventory: Inventory,
	private val coalGeneratorContainer: Container,
	private val containerData: ContainerData
) : MachineMenu(
	ModMenuTypes.COAL_GENERATOR.get(),
	id,
	inventory
) {

	constructor(containerId: Int, playerInventory: Inventory) : this(
		containerId,
		playerInventory,
		SimpleContainer(1),
		SimpleContainerData(CoalGeneratorBlockEntity.CONTAINER_DATA_SIZE)
	)

	override val amountSlots: Int = 1

	constructor(id: Int, inventory: Inventory, extraData: RegistryFriendlyByteBuf) :
			this(
				id,
				inventory,
				inventory.player.level().getBlockEntity(extraData.readBlockPos()) as CoalGeneratorBlockEntity,
				SimpleContainerData(CoalGeneratorBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(inventory, CoalGeneratorBlockEntity.CONTAINER_DATA_SIZE)

		addPlayerInventorySlots(inventoryY)

		val itemHandler = this.blockEntity.getItemHandler(null)
		this.addSlot(SlotItemHandler(itemHandler, CoalGeneratorBlockEntity.INPUT_SLOT, 52, 40))
		addDataSlots(containerData)
	}

	private var maxBurnTime: Int
		get() = containerData.get(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX)
		set(value) {
			containerData.set(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX, value)
		}

	private var burnTimeRemaining: Int
		get() = containerData.get(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX)
		set(value) {
			containerData.set(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX, value)
		}

	val isBurning
		get() = burnTimeRemaining > 0

	override fun getPercentDone(): Float {
		if (maxBurnTime == 0) return 0f

		return 1f - burnTimeRemaining.toFloat() / maxBurnTime.toFloat()
	}

	companion object {
		fun showFuelTooltip(event: ItemTooltipEvent) {
			val itemStack = event.itemStack
			val fuelPer = itemStack.getBurnTime(RecipeType.SMELTING)
			if (fuelPer <= 0) return

			val feProducedPer = CoalGeneratorBlockEntity.energyPerTick * fuelPer
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

	override fun stillValid(pPlayer: Player): Boolean {
		return stillValid(
			ContainerLevelAccess.create(level, blockEntity.blockPos),
			pPlayer,
			ModBlocks.COAL_GENERATOR.get()
		)
	}

}