package dev.aaronhowser.mods.geneticsresequenced.block_old.base

import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block_old.base.handler.ModEnergyStorage
import dev.aaronhowser.mods.geneticsresequenced.block_old.base.handler.WrappedHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.ContainerHelper
import net.minecraft.world.Containers
import net.minecraft.world.SimpleContainer
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
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

	abstract val machineName: String

	protected val inventoryNbtKey: String
		get() = "${machineName}.inventory"
	protected val energyNbtKey: String
		get() = "${machineName}.energy"

	abstract val energyMaximum: Int
	abstract val energyTransferMaximum: Int

	abstract val containerSize: Int
	protected open val container: SimpleContainer = ImprovedSimpleContainer(this, containerSize)

	open val itemHandler: IItemHandler = InvWrapper(container)

	protected abstract val upItemHandler: WrappedHandler
	protected abstract val downItemHandler: WrappedHandler
	protected abstract val backItemHandler: WrappedHandler
	protected abstract val frontItemHandler: WrappedHandler
	protected abstract val rightItemHandler: WrappedHandler
	protected abstract val leftItemHandler: WrappedHandler

	private val directionWrappedHandlerMap: Map<Direction, WrappedHandler>
		get() = mapOf(
			Direction.DOWN to downItemHandler,
			Direction.UP to upItemHandler,
			Direction.NORTH to backItemHandler,
			Direction.SOUTH to frontItemHandler,
			Direction.EAST to rightItemHandler,
			Direction.WEST to leftItemHandler
		)

	open fun getItemHandler(side: Direction?): IItemHandler {
		if (side == null) return itemHandler

		if (side == Direction.UP) return upItemHandler
		if (side == Direction.DOWN) return downItemHandler

		val directionFacing = this.blockState.getValue(HorizontalDirectionalBlock.FACING)

		return when (directionFacing) {
			Direction.NORTH -> directionWrappedHandlerMap[side.opposite]!!
			Direction.SOUTH -> directionWrappedHandlerMap[side]!!
			Direction.EAST -> directionWrappedHandlerMap[side.clockWise]!!
			Direction.WEST -> directionWrappedHandlerMap[side.counterClockWise]!!

			else -> directionWrappedHandlerMap[side]!!
		}
	}

	//TODO: Allow different blocks to prevent taking or inserting energy (cant take from machines, cant insert into generator)
	open val energyStorage: ModEnergyStorage by lazy {
		object : ModEnergyStorage(energyMaximum, energyTransferMaximum) {
			override fun onEnergyChanged() {
				setChanged()
			}
		}
	}

	protected open val upEnergyStorage by lazy { energyStorage }
	protected open val downEnergyStorage by lazy { energyStorage }
	protected open val backEnergyStorage by lazy { energyStorage }
	protected open val frontEnergyStorage by lazy { energyStorage }
	protected open val rightEnergyStorage by lazy { energyStorage }
	protected open val leftEnergyStorage by lazy { energyStorage }

	private val directionModEnergyStorageMap: Map<Direction, ModEnergyStorage>
		get() = mapOf(
			Direction.DOWN to downEnergyStorage,
			Direction.UP to upEnergyStorage,
			Direction.NORTH to backEnergyStorage,
			Direction.SOUTH to frontEnergyStorage,
			Direction.EAST to rightEnergyStorage,
			Direction.WEST to leftEnergyStorage
		)

	open fun getEnergyCapability(side: Direction?): ModEnergyStorage {
		if (side == null) return energyStorage
		if (side == Direction.UP || side == Direction.DOWN) return directionModEnergyStorageMap[side]!!

		val directionFacing = this.blockState.getValue(HorizontalDirectionalBlock.FACING)
		return when (directionFacing) {
			Direction.NORTH -> directionModEnergyStorageMap[side.opposite]!!
			Direction.SOUTH -> directionModEnergyStorageMap[side]!!
			Direction.EAST -> directionModEnergyStorageMap[side.clockWise]!!
			Direction.WEST -> directionModEnergyStorageMap[side.counterClockWise]!!

			else -> directionModEnergyStorageMap[side]!!
		}
	}

	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag {
		return saveWithoutMetadata(pRegistries)
	}

	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	override fun setChanged() {
		super.setChanged()
		level?.sendBlockUpdated(blockPos, blockState, blockState, 3)
	}

	fun dropDrops() {
		val inventory = SimpleContainer(itemHandler.slots)
		for (i in 0 until itemHandler.slots) {
			inventory.setItem(i, itemHandler.getStackInSlot(i))
		}

		Containers.dropContents(this.level!!, this.blockPos, inventory)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		ContainerHelper.saveAllItems(tag, container.items, registries)
		tag.putInt(energyNbtKey, energyStorage.energyStored)
		super.saveAdditional(tag, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		ContainerHelper.loadAllItems(tag, container.items, registries)
		energyStorage.setEnergy(tag.getInt(energyNbtKey))
		super.loadAdditional(tag, registries)

	}

}