package dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.base.InventoryEnergyBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.handler.WrappedHandler
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.ItemStackHandler
import java.util.function.Predicate

class CoalGeneratorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : InventoryEnergyBlockEntity(
    ModBlockEntities.COAL_GENERATOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "coal_generator"

    override val energyMaximum: Int = ServerConfig.coalGeneratorEnergyCapacity.get()
    override val energyTransferMaximum: Int = ServerConfig.coalGeneratorEnergyTransferRate.get()

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return CoalGeneratorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.coal_generator")
    }


    override val amountOfItemSlots: Int = 1
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        private val replacementCache: Object2BooleanOpenHashMap<Item> = Object2BooleanOpenHashMap()
        private fun isReplacementItem(item: Item): Boolean {
            return replacementCache.computeIfAbsent(item, Predicate {
                val isReplacement = BuiltInRegistries.ITEM.any {
                    it.getCraftingRemainingItem(ItemStack(item)).item == item
                }
                return@Predicate isReplacement
            })
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            val isFuel = stack.getBurnTime(RecipeType.SMELTING) > 0
            if (isFuel) return true

            return isReplacementItem(stack.item)
        }
    }

    private val allSideHandler =
        WrappedHandler(
            itemHandler,
            canExtract = { false },
            canInsert = { slotId, stack -> slotId == INPUT_SLOT }
        )

    override val upItemHandler: WrappedHandler = allSideHandler
    override val downItemHandler: WrappedHandler = allSideHandler
    override val backItemHandler: WrappedHandler = allSideHandler
    override val frontItemHandler: WrappedHandler = allSideHandler
    override val rightItemHandler: WrappedHandler = allSideHandler
    override val leftItemHandler: WrappedHandler = allSideHandler

    private val data = object : ContainerData {
        private var _totalTicksToBurn = 0
            set(value) {
                field = value
                _ticksToBurnRemaining = _ticksToBurnRemaining.coerceAtMost(_totalTicksToBurn)
            }
        private var _ticksToBurnRemaining = 0
            set(value) {
                field = value.coerceIn(0, _totalTicksToBurn)
            }

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                REMAINING_TICKS_INDEX -> _ticksToBurnRemaining
                MAX_BURN_TIME_INDEX -> _totalTicksToBurn
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                REMAINING_TICKS_INDEX -> _ticksToBurnRemaining = pValue
                MAX_BURN_TIME_INDEX -> _totalTicksToBurn = pValue
            }
        }

        override fun getCount(): Int {
            return SIMPLE_CONTAINER_SIZE
        }
    }

    private val burnTicksLeftNbtKey = "$machineName.burn_ticks_left"
    private val maxBurnTicksNbtKey = "$machineName.max_burn_ticks"

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        pTag.putInt(burnTicksLeftNbtKey, burnTimeRemaining)
        pTag.putInt(maxBurnTicksNbtKey, maxBurnTime)

        super.saveAdditional(pTag, pRegistries)
    }

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        maxBurnTime = pTag.getInt(maxBurnTicksNbtKey)
        burnTimeRemaining = pTag.getInt(burnTicksLeftNbtKey)

        super.loadAdditional(pTag, pRegistries)
    }

    private var maxBurnTime: Int
        get() = data.get(MAX_BURN_TIME_INDEX)
        set(value) {
            data.set(MAX_BURN_TIME_INDEX, value)
            setChanged()
        }

    private var burnTimeRemaining: Int
        get() {
            val t = data.get(REMAINING_TICKS_INDEX)
            return t
        }
        set(value) {
            data.set(REMAINING_TICKS_INDEX, value)
            setChanged()
        }

    private fun tryToStartBurning() {

        val inputItem = itemHandler.getStackInSlot(INPUT_SLOT)
        val fuel = inputItem.getBurnTime(RecipeType.SMELTING)

        if (fuel <= 0) {
            stopBurning()
        } else {
            startBurning(inputItem, fuel)
        }

        setChanged()
    }

    private fun stopBurning() {
        maxBurnTime = 0
        burnTimeRemaining = 0
        val state = blockState.setValue(CoalGeneratorBlock.BURNING, false)
        level?.setBlock(blockPos, state, 3)
    }

    private fun startBurning(inputItem: ItemStack, fuel: Int) {
        val state = blockState.setValue(CoalGeneratorBlock.BURNING, true)
        level?.setBlock(blockPos, state, 3)

        val fuelReplacedItem = inputItem.craftingRemainingItem

        maxBurnTime = fuel
        burnTimeRemaining = fuel
        itemHandler.extractItem(INPUT_SLOT, 1, false)

        if (!fuelReplacedItem.isEmpty && itemHandler.getStackInSlot(INPUT_SLOT).isEmpty) {
            itemHandler.insertItem(INPUT_SLOT, fuelReplacedItem, false)
        }
    }

    private fun generateEnergy() {
        energyStorage.receiveEnergy(energyPerTick, false)

        burnTimeRemaining -= 1
        setChanged()
    }

    val isBurning: Boolean
        get() = burnTimeRemaining > 0

    private fun hasRoomForEnergy(): Boolean {
        val maxEnergy = energyStorage.maxEnergyStored
        val currentEnergy = energyStorage.energyStored
        return currentEnergy + energyPerTick <= maxEnergy
    }

    private fun pushEnergyToAdjacent() {

        if (energyStorage.energyStored <= 0) return
        for (direction in Direction.entries) {
            val neighborPos = blockPos.offset(direction.normal)

            val neighborEnergy =
                level?.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.opposite) ?: continue

            if (!neighborEnergy.canReceive()) continue

            val energyToTransfer = minOf(
                neighborEnergy.receiveEnergy(ServerConfig.coalGeneratorEnergyTransferRate.get(), true),
                neighborEnergy.maxEnergyStored - neighborEnergy.energyStored
            )

            if (energyToTransfer <= 0) continue
            neighborEnergy.receiveEnergy(
                energyStorage.extractEnergy(energyToTransfer, false),
                false
            )
        }
    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: CoalGeneratorBlockEntity
        ) {
            if (level.isClientSide) return

            blockEntity.apply {
                pushEnergyToAdjacent()

                if (!hasRoomForEnergy()) return

                if (isBurning) {
                    generateEnergy()
                } else {
                    tryToStartBurning()
                }
            }
        }


        // How many values are stored in the container data
        // Here it's two: remaining ticks and max burn time
        const val SIMPLE_CONTAINER_SIZE = 2

        const val INPUT_SLOT = 0

        val energyPerTick: Int
            get() = ServerConfig.coalGeneratorEnergyPerTick.get()

        const val REMAINING_TICKS_INDEX = 0
        const val MAX_BURN_TIME_INDEX = 1
    }

}