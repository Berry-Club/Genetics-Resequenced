package dev.aaronhowser.mods.geneticsresequenced.block.machines.cell_analyzer

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem.Companion.setEntityType
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
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
import net.neoforged.neoforge.items.ItemStackHandler


class CellAnalyzerBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.CELL_ANALYZER.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "cell_analyzer"

    override val energyMaximum: Int = 60_000
    override val energyTransferMaximum: Int = 256
    override val baseEnergyCostPerTick: Int = 32

    override val amountOfItemSlots: Int = 3
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                INPUT_SLOT_INDEX -> stack.`is`(ModItems.ORGANIC_MATTER.get())
                OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER.get())
                OUTPUT_SLOT_INDEX -> false
                else -> false
            }
        }
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return CellAnalyzerMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return ModLanguageProvider.Blocks.CELL_ANALYZER.toComponent()
    }

    override fun craftItem() {
        if (!hasRecipe()) return

        val inputItem = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val inputEntity = EntityDnaItem.getEntityType(inputItem) ?: return

        val outputItem = ItemStack(ModItems.CELL.get()).setEntityType(inputEntity)

        val amountAlreadyInOutput = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX).count
        outputItem.count = amountAlreadyInOutput + 1

        itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
        itemHandler.setStackInSlot(OUTPUT_SLOT_INDEX, outputItem)
    }

    override fun hasRecipe(): Boolean {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        val inputItemStack = inventory.getItem(INPUT_SLOT_INDEX)

        if (!inputItemStack.`is`(ModItems.ORGANIC_MATTER.get())) return false

        val mobType = EntityDnaItem.getEntityType(inputItemStack) ?: return false

        val outputItem = ItemStack(ModItems.CELL.get()).setEntityType(mobType) ?: return false

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

    companion object {
        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: CellAnalyzerBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }
    }
}