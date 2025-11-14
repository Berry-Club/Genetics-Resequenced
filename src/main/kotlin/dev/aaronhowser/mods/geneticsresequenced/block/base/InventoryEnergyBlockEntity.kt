package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.items.wrapper.InvWrapper

abstract class InventoryEnergyBlockEntity(
	blockEntityType: BlockEntityType<*>,
	pPos: BlockPos,
	pBlockState: BlockState
) : BlockEntity(
	blockEntityType,
	pPos,
	pBlockState
) {

	abstract val maxEnergy: Int
	abstract val energyTransferRate: Int

	val energyStorage = BetterEnergyStorage(this, maxEnergy, energyTransferRate)

	abstract val containerSize: Int
	open val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, containerSize)
	private val invWrapper = InvWrapper(container)

	open fun getItemHandler(direction: Direction?): InvWrapper? {
		return invWrapper
	}

	override fun setChanged() {
		super.setChanged()

		level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL_IMMEDIATE)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		ContainerHelper.saveAllItems(tag, this.container.items, registries)
		tag.putInt(ENERGY_NBT, energyStorage.energyStored)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ContainerHelper.loadAllItems(tag, this.container.items, registries)
		energyStorage.setEnergy(tag.getInt(ENERGY_NBT))
	}

	companion object {
		private const val ENERGY_NBT = "Energy"
	}

}