package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.ContainerHelper
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.InvWrapper

abstract class MachineBlockEntity(
	blockEntityType: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(
	blockEntityType,
	pos,
	blockState
), MenuProvider {

	abstract val maxEnergy: Int
	abstract val energyTransferRate: Int

	protected val energyStorage = BetterEnergyStorage(this, maxEnergy, energyTransferRate)

	open fun getEnergyCapability(direction: Direction?): EnergyStorage {
		return energyStorage
	}

	protected val energyContainerData = object : ContainerData {
		override fun get(index: Int): Int {
			return when (index) {
				CURRENT_ENERGY_INDEX -> energyStorage.energyStored
				MAX_ENERGY_INDEX -> energyStorage.maxEnergyStored
				else -> -1
			}
		}

		override fun set(index: Int, value: Int) {
			when (index) {
				CURRENT_ENERGY_INDEX -> energyStorage.setEnergy(value)
			}
		}

		override fun getCount(): Int = ENERGY_CONTAINER_DATA_SIZE
	}

	open val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, 0)
	protected open val itemHandler = InvWrapper(container)

	open fun getItemHandler(direction: Direction?): IItemHandler? {
		return itemHandler
	}

	protected open fun serverTick() {}
	protected open fun clientTick() {}

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

	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(pRegistries)
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	override fun getDisplayName(): Component {
		return blockState.block.name
	}

	companion object {
		private const val ENERGY_NBT = "Energy"

		const val ENERGY_CONTAINER_DATA_SIZE = 2
		const val CURRENT_ENERGY_INDEX = 0
		const val MAX_ENERGY_INDEX = 1

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: MachineBlockEntity
		) {
			if (level.isServerSide) {
				blockEntity.serverTick()
			} else {
				blockEntity.clientTick()
			}
		}
	}

}