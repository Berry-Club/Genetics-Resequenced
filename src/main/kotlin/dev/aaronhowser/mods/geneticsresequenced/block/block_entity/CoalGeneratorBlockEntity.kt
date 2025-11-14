package dev.aaronhowser.mods.geneticsresequenced.block.block_entity

import dev.aaronhowser.mods.geneticsresequenced.block.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class CoalGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : InventoryEnergyBlockEntity(ModBlockEntityTypes.COAL_GENERATOR.get(), pos, blockState) {
	override val maxEnergy: Int
		get() = TODO("Not yet implemented")
	override val energyTransferRate: Int
		get() = TODO("Not yet implemented")
	override val containerSize: Int
		get() = TODO("Not yet implemented")
}