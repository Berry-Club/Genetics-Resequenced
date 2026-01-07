package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.ImprovedSimpleContainer
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity.Companion.OUTPUT_SLOT_INDEX
import dev.aaronhowser.mods.geneticsresequenced.block.base.container_data.EnergyContainerData
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.ContainerHelper
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.energy.EnergyStorage
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.IItemHandlerModifiable
import net.minecraftforge.items.wrapper.InvWrapper

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

	protected val energyStorage by lazy {
		EnergyStorage(maxEnergy, energyTransferRate)
	}

	protected var lazyEnergyStorage: LazyOptional<EnergyStorage> = LazyOptional.empty()

	protected open val containerData: ContainerData by lazy { EnergyContainerData(energyStorage) }

	open val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, 0)
	protected open val itemHandler: IItemHandlerModifiable by lazy {
		object : InvWrapper(container) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
				return if (slot == OUTPUT_SLOT_INDEX) {
					false
				} else {
					super.isItemValid(slot, stack)
				}
			}
		}
	}

	protected var lazyItemHandler: LazyOptional<IItemHandler> = LazyOptional.empty()

	open fun getItemHandler(direction: Direction?): IItemHandler? {
		return itemHandler
	}

	override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
		return when (cap) {
			ForgeCapabilities.ENERGY -> lazyEnergyStorage.cast()
			ForgeCapabilities.ITEM_HANDLER -> lazyItemHandler.cast()

			else -> super.getCapability(cap, side)
		}
	}

	override fun onLoad() {
		super.onLoad()

		lazyEnergyStorage = LazyOptional.of { energyStorage }
		lazyItemHandler = LazyOptional.of { itemHandler }
	}

	override fun invalidateCaps() {
		super.invalidateCaps()

		lazyEnergyStorage.invalidate()
		lazyItemHandler.invalidate()
	}

	protected open fun serverTick() {}
	protected open fun clientTick() {}

	override fun setChanged() {
		super.setChanged()

		level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL_IMMEDIATE)
	}

	override fun saveAdditional(pTag: CompoundTag) {
		super.saveAdditional(pTag)

		ContainerHelper.saveAllItems(pTag, this.container.items)
		pTag.put(ENERGY_NBT, energyStorage.serializeNBT())
	}

	override fun load(pTag: CompoundTag) {
		super.load(pTag)

		ContainerHelper.loadAllItems(pTag, this.container.items)
		val energy = pTag.get(ENERGY_NBT)
		if (energy is IntTag) {
			energyStorage.deserializeNBT(energy)
		}
	}

	override fun getUpdateTag(): CompoundTag = saveWithoutMetadata()
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	override fun getDisplayName(): Component {
		return blockState.block.name
	}

	companion object {
		private const val ENERGY_NBT = "Energy"

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