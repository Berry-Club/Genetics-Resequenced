package dev.aaronhowser.mods.geneticsresequenced.block_entity.base

import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.geneticsresequenced.block_entity.base.container_data.EnergyContainerData
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import net.neoforged.neoforge.items.wrapper.InvWrapper

abstract class MachineBlockEntity(
	blockEntityType: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : SyncingBlockEntity(
	blockEntityType,
	pos,
	blockState
), MenuProvider, ContainerContainer {

	override val syncImmediately: Boolean = true

	abstract val maxEnergy: Int
	abstract val energyTransferRate: Int

	protected val energyStorage by lazy {
		EnergyStorage(maxEnergy, energyTransferRate)
	}

	protected open val containerData: ContainerData by lazy { EnergyContainerData(energyStorage) }
	open val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, 0)

	protected val itemHandler: IItemHandlerModifiable by lazy {
		object : InvWrapper(container) {
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
				return if (slot == CraftingMachineBlockEntity.Companion.OUTPUT_SLOT_INDEX) {
					false
				} else {
					super.isItemValid(slot, stack)
				}
			}
		}
	}

	open fun getEnergyCapability(direction: Direction?): EnergyStorage {
		return energyStorage
	}

	override fun getContainers(): List<Container> {
		return listOf(container)
	}

	open fun getItemHandler(direction: Direction?): IItemHandler? {
		return itemHandler
	}

	protected open fun serverTick() {}
	protected open fun clientTick() {}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		ContainerHelper.saveAllItems(tag, this.container.items, registries)
		tag.put(ENERGY_NBT, energyStorage.serializeNBT(registries))
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ContainerHelper.loadAllItems(tag, this.container.items, registries)
		val energy = tag.get(ENERGY_NBT)
		if (energy is IntTag) {
			energyStorage.deserializeNBT(registries, energy)
		}
	}

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