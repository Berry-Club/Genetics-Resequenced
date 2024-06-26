package dev.aaronhowser.mods.geneticsresequenced.block.base

import dev.aaronhowser.mods.geneticsresequenced.block.base.handler.WrappedHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

/**
 * Base class for all crafting machines.
 * Note that this is only for basic crafting machines, ie, those that turn one thing into one other thing.
 */
abstract class CraftingMachineBlockEntity(
    pType: BlockEntityType<*>,
    pPos: BlockPos,
    pBlockState: BlockState
) : InventoryEnergyBlockEntity(
    pType,
    pPos,
    pBlockState
), MenuProvider {

    companion object {
        const val SIMPLE_CONTAINER_SIZE = 2
        const val ITEMSTACK_HANDLER_SIZE = 3

        const val INPUT_SLOT_INDEX = 0
        const val OUTPUT_SLOT_INDEX = 1
        const val OVERCLOCK_SLOT_INDEX = 2
    }

    abstract val baseEnergyCostPerTick: Int
    fun energyCostPerTick(): Int {
        return baseEnergyCostPerTick + (baseEnergyCostPerTick * amountOfOverclockers * 0.25f).toInt()
    }

    open val amountOfOverclockers: Int
        get() = itemHandler.getStackInSlot(OVERCLOCK_SLOT_INDEX).count

    protected var progress = 0
    protected var maxProgress = 20 * 4
    protected fun resetProgress() {
        progress = 0
    }

    protected val progressNbtKey
        get() = "${machineName}.progress"
    protected val maxProgressNbtKey
        get() = "${machineName}.max_progress"

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        pTag.putInt(progressNbtKey, progress)
        pTag.putInt(maxProgressNbtKey, maxProgress)

        super.saveAdditional(pTag, pRegistries)
    }

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        maxProgress = pTag.getInt(maxProgressNbtKey)
        progress = pTag.getInt(progressNbtKey)

        super.loadAdditional(pTag, pRegistries)
    }

    protected val containerData = object : ContainerData {

        private val PROGRESS_INDEX = 0
        private val MAX_PROGRESS_INDEX = 1

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                PROGRESS_INDEX -> progress
                MAX_PROGRESS_INDEX -> maxProgress
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                PROGRESS_INDEX -> progress = pValue
                MAX_PROGRESS_INDEX -> maxProgress = pValue
            }
        }

        override fun getCount(): Int {
            return SIMPLE_CONTAINER_SIZE
        }
    }

    protected open fun hasEnoughEnergy(): Boolean {
        return energyStorage.energyStored >= energyCostPerTick()
    }

    protected open fun extractEnergy() {
        if (energyStorage.energyStored < energyCostPerTick()) return
        energyStorage.extractEnergy(energyCostPerTick(), false)
    }

    protected abstract fun hasRecipe(): Boolean
    protected abstract fun craftItem()

    open fun tick() {
        if (hasRecipe() && hasEnoughEnergy()) {
            extractEnergy()

            progress += 1 + amountOfOverclockers
            setChanged()

            if (progress >= maxProgress) {
                progress = 1
//                craftItem()
            }
        } else {
            resetProgress()
            setChanged()
        }
    }

    private val allFaceHandler by lazy {
        WrappedHandler(
            itemHandler,
            canExtract = { slotId -> slotId == OUTPUT_SLOT_INDEX },
            canInsert = { slotId, stack -> slotId == INPUT_SLOT_INDEX }
        )
    }

    override val upItemHandler: WrappedHandler by lazy { allFaceHandler }
    override val downItemHandler: WrappedHandler by lazy { allFaceHandler }
    override val backItemHandler: WrappedHandler by lazy { allFaceHandler }
    override val frontItemHandler: WrappedHandler by lazy { allFaceHandler }
    override val rightItemHandler: WrappedHandler by lazy { allFaceHandler }
    override val leftItemHandler: WrappedHandler by lazy { allFaceHandler }

}