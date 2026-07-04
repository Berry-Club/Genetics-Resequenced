package dev.aaronhowser.mods.geneticsresequenced.block_entity.base

import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.geneticsresequenced.block_entity.base.container_data.EnergyContainerData
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.Transaction

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
		object : SimpleEnergyHandler(maxEnergy, energyTransferRate) {
			override fun onEnergyChanged(previousAmount: Int) {
				setChanged()
			}
		}
	}

	protected open val containerData: ContainerData by lazy { EnergyContainerData(energyStorage) }
	open val container: ImprovedSimpleContainer = ImprovedSimpleContainer(this, 0)

	protected val itemHandler: MachineItemHandler by lazy {
		MachineItemHandler(container)
	}

	private val automationItemHandler: ResourceHandler<ItemResource> by lazy {
		insertAndExtractHandler(*IntArray(container.containerSize) { it })
	}

	protected fun insertOnlyHandler(vararg slots: Int): ResourceHandler<ItemResource> {
		return SidedMachineItemHandler(
			itemHandler,
			slots,
			canInsert = ::canAutomateInsert,
			canExtract = { _, _ -> false }
		)
	}

	protected fun extractOnlyHandler(vararg slots: Int): ResourceHandler<ItemResource> {
		return SidedMachineItemHandler(
			itemHandler,
			slots,
			canInsert = { _, _ -> false },
			canExtract = { _, _ -> true }
		)
	}

	protected fun insertAndExtractHandler(vararg slots: Int): ResourceHandler<ItemResource> {
		return SidedMachineItemHandler(
			itemHandler,
			slots,
			canInsert = ::canAutomateInsert,
			canExtract = { _, _ -> true }
		)
	}

	protected open fun canAutomateInsert(slot: Int, stack: ItemStack): Boolean {
		return container.canPlaceItem(slot, stack)
	}

	open fun getEnergyCapability(direction: Direction?): EnergyHandler {
		return energyStorage
	}

	override fun getContainers(): List<Container> {
		return listOf(container)
	}

	open fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource>? {
		return automationItemHandler
	}

	protected fun insertEnergy(amount: Int): Int {
		Transaction.openRoot().use { transaction ->
			val inserted = energyStorage.insert(amount, transaction)
			transaction.commit()
			return inserted
		}
	}

	protected fun extractEnergy(amount: Int): Int {
		Transaction.openRoot().use { transaction ->
			val extracted = energyStorage.extract(amount, transaction)
			transaction.commit()
			return extracted
		}
	}

	protected open fun serverTick() {}
	protected open fun clientTick() {}

	override fun saveAdditional(output: ValueOutput) {
		super.saveAdditional(output)

		output.saveItems(container)
		output.saveEnergy(ENERGY_NBT, energyStorage)
	}

	override fun loadAdditional(input: ValueInput) {
		super.loadAdditional(input)

		input.loadItems(container)
		input.loadEnergy(ENERGY_NBT, energyStorage)
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
