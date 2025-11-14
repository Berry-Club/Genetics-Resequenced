package dev.aaronhowser.mods.geneticsresequenced.menu.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.block_entity.CoalGeneratorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData

class CoalGeneratorMenu(
	id: Int,
	playerInventory: Inventory,
	private val coalGeneratorContainer: Container,
	private val containerData: ContainerData
) : MachineMenu(ModMenuTypes.COAL_GENERATOR.get(), id, playerInventory) {

	override val amountSlots: Int = CoalGeneratorBlockEntity.CONTAINER_SIZE

	var maxBurnTime: Int
		get() = containerData.get(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX)
		set(value) = containerData.set(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX, value)

	var burnTimeRemaining: Int
		get() = containerData.get(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX)
		set(value) = containerData.set(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX, value)

	override fun getPercentDone(): Float {
		TODO("Not yet implemented")
	}

	override fun stillValid(player: Player): Boolean = coalGeneratorContainer.stillValid(player)
}