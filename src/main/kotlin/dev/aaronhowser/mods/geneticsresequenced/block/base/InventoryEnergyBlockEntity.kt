package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.InvWrapper
import java.util.function.IntSupplier

abstract class InventoryEnergyBlockEntity(
	blockEntityType: BlockEntityType<*>,
	pPos: BlockPos,
	pBlockState: BlockState
) : BlockEntity(
	blockEntityType,
	pPos,
	pBlockState
) {

	abstract val maxEnergy: IntSupplier
	abstract val energyTransferRate: IntSupplier

	abstract val containerSize: Int
	open val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, containerSize)
	private val invWrapper = InvWrapper(container)

	open fun getItemHandler(direction: Direction?): InvWrapper? {
		return invWrapper
	}

}