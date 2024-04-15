package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.blockentities.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.screens.CellAnalyzerMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.items.ItemStackHandler


class CellAnalyzerBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.CELL_ANALYZER.get(),
    pPos,
    pBlockState
), MenuProvider {
    override val inventoryNbtKey: String = "cell_analyzer.inventory"
    override val energyNbtKey: String = "cell_analyzer.energy"
    override val energyMaximum: Int = 60_000
    override val energyTransferMaximum: Int = 256
    override val energyCostPerTick: Int = 32

    override val itemHandler: ItemStackHandler = object : ItemStackHandler(ITEMSTACK_HANDLER_SIZE) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                INPUT_SLOT_INDEX -> stack.`is`(ModItems.ORGANIC_MATTER)
                OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER)
                OUTPUT_SLOT_INDEX -> false
                else -> false
            }
        }
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return CellAnalyzerMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.cell_analyzer")
    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: CellAnalyzerBlockEntity
        ) {
            if (level.isClientSide) return

            if (hasRecipe(blockEntity) && hasEnoughEnergy(blockEntity)) {
                extractEnergy(blockEntity)

                blockEntity.progress += 1 + blockEntity.amountOfOverclockers
                blockEntity.setChanged()

                if (blockEntity.progress >= blockEntity.maxProgress) {
                    craftItem(blockEntity)
                }
            } else {
                blockEntity.resetProgress()
                blockEntity.setChanged()
            }

        }

        private fun extractEnergy(blockEntity: CellAnalyzerBlockEntity) {
            if (blockEntity.energyStorage.energyStored < blockEntity.energyCostPerTick) return
            blockEntity.energyStorage.extractEnergy(blockEntity.energyCostPerTick, false)
        }

        private fun hasEnoughEnergy(blockEntity: CellAnalyzerBlockEntity): Boolean {
            return blockEntity.energyStorage.energyStored >= blockEntity.energyCostPerTick
        }


        private fun craftItem(blockEntity: CellAnalyzerBlockEntity) {
            if (!hasRecipe(blockEntity)) return

            val inputItem = blockEntity.itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
            val inputEntity = EntityDnaItem.getEntityType(inputItem) ?: return

            val outputItem = ItemStack(ModItems.CELL).setMob(inputEntity) ?: return

            val amountAlreadyInOutput = blockEntity.itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX).count
            outputItem.count = amountAlreadyInOutput + 1

            blockEntity.itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
            blockEntity.itemHandler.setStackInSlot(OUTPUT_SLOT_INDEX, outputItem)

            blockEntity.resetProgress()
        }

        private fun hasRecipe(blockEntity: CellAnalyzerBlockEntity): Boolean {
            val inventory = SimpleContainer(blockEntity.itemHandler.slots)
            for (i in 0 until blockEntity.itemHandler.slots) {
                inventory.setItem(i, blockEntity.itemHandler.getStackInSlot(i))
            }

            val inputItemStack = inventory.getItem(INPUT_SLOT_INDEX)

            if (!inputItemStack.`is`(ModItems.ORGANIC_MATTER)) return false

            val mobType = EntityDnaItem.getEntityType(inputItemStack) ?: return false

            val outputItem = ItemStack(ModItems.CELL).setMob(mobType) ?: return false

            return outputSlotHasRoom(inventory, outputItem)
        }

        private fun outputSlotHasRoom(inventory: SimpleContainer, potentialOutput: ItemStack): Boolean {
            val outputSlot = inventory.getItem(OUTPUT_SLOT_INDEX)

            if (outputSlot.isEmpty) return true

            if (outputSlot.count + potentialOutput.count > outputSlot.maxStackSize) return false

            val mobAlreadyInSlot = EntityDnaItem.getEntityType(outputSlot) ?: return false
            val mobToInsert = EntityDnaItem.getEntityType(potentialOutput) ?: return false

            return mobAlreadyInSlot == mobToInsert
        }
    }

}