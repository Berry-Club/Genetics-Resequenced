package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.screens.CellAnalyzerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.Containers
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.common.util.LazyOptional
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler


class CellAnalyzerBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(
    ModBlockEntities.CELL_ANALYZER.get(),
    pPos,
    pBlockState
), MenuProvider {

    private val itemHandler: ItemStackHandler = object : ItemStackHandler(ITEMSTACK_HANDLER_SIZE) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }
    }

    private var lazyItemHandler = LazyOptional.empty<IItemHandler>()

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu? {
        return CellAnalyzerMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return Component.literal("Cell Analyzer")
    }

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast()
        }

        return super.getCapability(cap, side)
    }

    override fun onLoad() {
        super.onLoad()
        lazyItemHandler = LazyOptional.of { itemHandler }
    }

    override fun invalidateCaps() {
        super.invalidateCaps()
        lazyItemHandler.invalidate()
    }

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.put(INVENTORY, itemHandler.serializeNBT())
        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        itemHandler.deserializeNBT(pTag.getCompound(INVENTORY))
    }

    fun drops() {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        Containers.dropContents(this.level!!, this.blockPos, inventory)
    }

    private var progress = 0
    private var maxProgress = 20 * 4
    private fun resetProgress() {
        progress = 0
    }

    val data = object : ContainerData {
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

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: CellAnalyzerBlockEntity
        ) {
            if (level.isClientSide) return

            if (hasRecipe(blockEntity)) {
                blockEntity.progress++
                blockEntity.setChanged()

                if (blockEntity.progress >= blockEntity.maxProgress) {
                    craftItem(blockEntity)
                }
            } else {
                blockEntity.resetProgress()
                blockEntity.setChanged()
            }

        }

        private fun craftItem(blockEntity: CellAnalyzerBlockEntity) {
            if (!hasRecipe(blockEntity)) return

            val inputItem = blockEntity.itemHandler.getStackInSlot(INPUT_SLOT)
            val inputEntity = EntityDnaItem.getEntityType(inputItem) ?: return

            val outputItem = ItemStack(ModItems.CELL)
            val mobSetSuccessful = EntityDnaItem.setMob(outputItem, inputEntity)
            if (!mobSetSuccessful) return

            val amountAlreadyInOutput = blockEntity.itemHandler.getStackInSlot(OUTPUT_SLOT).count
            outputItem.count = amountAlreadyInOutput + 1

            blockEntity.itemHandler.extractItem(INPUT_SLOT, 1, false)
            blockEntity.itemHandler.setStackInSlot(OUTPUT_SLOT, outputItem)

            blockEntity.resetProgress()
        }

        private fun hasRecipe(blockEntity: CellAnalyzerBlockEntity): Boolean {
            val inventory = SimpleContainer(blockEntity.itemHandler.slots)
            for (i in 0 until blockEntity.itemHandler.slots) {
                inventory.setItem(i, blockEntity.itemHandler.getStackInSlot(i))
            }

            val inputItemStack = inventory.getItem(INPUT_SLOT)

            if (!inputItemStack.`is`(ModItems.ORGANIC_MATTER)) return false

            val mobType = EntityDnaItem.getEntityType(inputItemStack) ?: return false

            val outputItem = ItemStack(ModItems.CELL)
            val mobSetSuccessful = EntityDnaItem.setMob(outputItem, mobType)

            if (!mobSetSuccessful) return false

            return outputSlotHasRoom(inventory, outputItem)
        }

        private fun outputSlotHasRoom(inventory: SimpleContainer, potentialOutput: ItemStack): Boolean {
            val outputSlot = inventory.getItem(OUTPUT_SLOT)

            if (outputSlot.isEmpty) return true

            if (outputSlot.count + potentialOutput.count > outputSlot.maxStackSize) return false

            val mobAlreadyInSlot = EntityDnaItem.getEntityType(outputSlot) ?: return false
            val mobToInsert = EntityDnaItem.getEntityType(potentialOutput) ?: return false

            return mobAlreadyInSlot == mobToInsert
        }

        private const val INVENTORY = "inventory"
        private const val PROGRESS_INDEX = 0
        private const val MAX_PROGRESS_INDEX = 1

        const val SIMPLE_CONTAINER_SIZE = 2
        const val ITEMSTACK_HANDLER_SIZE = 3

        private const val INPUT_SLOT = 0
        private const val OUTPUT_SLOT = 1
        private const val OVERCLOCK_SLOT = 2

    }

}