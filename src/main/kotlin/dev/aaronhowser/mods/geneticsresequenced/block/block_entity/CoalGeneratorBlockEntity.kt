package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.block.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block_old.machine.coal_generator.CoalGeneratorMenu
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.state.BlockState

class CoalGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : InventoryEnergyBlockEntity(ModBlockEntityTypes.COAL_GENERATOR.get(), pos, blockState) {

	override val maxEnergy: Int = ServerConfig.CONFIG.coalGeneratorEnergyCapacity.get()
	override val energyTransferRate: Int = ServerConfig.CONFIG.coalGeneratorEnergyTransferRate.get()
	override val containerSize: Int = CONTAINER_SIZE

	private var burnTimeRemaining: Int = 0
		set(value) {
			field = value.coerceAtLeast(0)
			setChanged()
		}

	private var maxBurnTime: Int = 0
		set(value) {
			field = value.coerceAtLeast(0)
			setChanged()
		}

	private val containerData = object : ContainerData {

		override fun set(index: Int, value: Int) {
			when (index) {
				REMAINING_TICKS_INDEX -> burnTimeRemaining = value
				MAX_BURN_TIME_INDEX -> maxBurnTime = value
			}
		}

		override fun get(index: Int): Int {
			return when (index) {
				REMAINING_TICKS_INDEX -> burnTimeRemaining
				MAX_BURN_TIME_INDEX -> maxBurnTime
				else -> -1
			}
		}

		override fun getCount(): Int = CONTAINER_DATA_SIZE
	}

	override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
		return CoalGeneratorMenu(pContainerId, pPlayerInventory, container, containerData)
	}

	companion object {
		const val CONTAINER_SIZE = 1
		const val INPUT_SLOT = 0

		const val CONTAINER_DATA_SIZE = 2
		const val REMAINING_TICKS_INDEX = 0
		const val MAX_BURN_TIME_INDEX = 1

	}
}