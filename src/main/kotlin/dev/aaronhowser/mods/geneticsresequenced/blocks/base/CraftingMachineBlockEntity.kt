package dev.aaronhowser.mods.geneticsresequenced.blocks.base

import dev.aaronhowser.mods.geneticsresequenced.blocks.base.handlers.WrappedHandler
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.MenuProvider
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.util.LazyOptional

/**
 * Base class for all crafting machines.
 * Note that this is only for basic crafting machines, ie, those that turn one thing into one other thing.
 */
@Suppress("MemberVisibilityCanBePrivate")
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

    abstract val energyCostPerTick: Int

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

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.putInt(progressNbtKey, progress)
        pTag.putInt(maxProgressNbtKey, maxProgress)

        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        maxProgress = pTag.getInt(maxProgressNbtKey)
        progress = pTag.getInt(progressNbtKey)

        super.load(pTag)
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
        return energyStorage.energyStored >= energyCostPerTick
    }

    protected open fun extractEnergy() {
        if (energyStorage.energyStored < energyCostPerTick) return
        energyStorage.extractEnergy(energyCostPerTick, false)
    }

    protected abstract fun hasRecipe(): Boolean
    protected abstract fun craftItem()

    open fun tick() {
        if (hasRecipe() && hasEnoughEnergy()) {
            extractEnergy()

            progress += 1 + amountOfOverclockers
            setChanged()

            if (progress >= maxProgress) {
                craftItem()
            }
        } else {
            resetProgress()
            setChanged()
        }
    }

    private val allFaceHandler = LazyOptional.of {
        WrappedHandler(
            itemHandler,
            canExtract = { slotId -> slotId == OUTPUT_SLOT_INDEX },
            canInsert = { slotId, stack -> slotId == INPUT_SLOT_INDEX }
        )
    }

    override val upItemHandler: LazyOptional<WrappedHandler> = allFaceHandler
    override val downItemHandler: LazyOptional<WrappedHandler> = allFaceHandler
    override val backItemHandler: LazyOptional<WrappedHandler> = allFaceHandler
    override val frontItemHandler: LazyOptional<WrappedHandler> = allFaceHandler
    override val rightItemHandler: LazyOptional<WrappedHandler> = allFaceHandler
    override val leftItemHandler: LazyOptional<WrappedHandler> = allFaceHandler

}