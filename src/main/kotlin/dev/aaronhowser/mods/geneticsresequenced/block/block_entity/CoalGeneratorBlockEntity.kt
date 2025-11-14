package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.block.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class CoalGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : InventoryEnergyBlockEntity(ModBlockEntityTypes.COAL_GENERATOR.get(), pos, blockState) {

	override val maxEnergy: Int = ServerConfig.CONFIG.coalGeneratorEnergyCapacity.get()
	override val energyTransferRate: Int = ServerConfig.CONFIG.coalGeneratorEnergyTransferRate.get()
	override val containerSize: Int = CONTAINER_SIZE

	companion object {
		const val CONTAINER_SIZE = 1
	}
}