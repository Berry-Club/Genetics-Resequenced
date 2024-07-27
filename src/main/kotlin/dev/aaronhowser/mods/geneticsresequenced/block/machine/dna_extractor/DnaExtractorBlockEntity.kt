package dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
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


class DnaExtractorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.DNA_EXTRACTOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "dna_extractor"

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
                INPUT_SLOT_INDEX -> stack.item == ModItems.CELL.get() || stack.item == ModItems.GMO_CELL.get()
                OVERCLOCK_SLOT_INDEX -> stack.item == ModItems.OVERCLOCKER.get()
                OUTPUT_SLOT_INDEX -> false
                else -> false
            }
        }
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return DnaExtractorMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return ModLanguageProvider.Blocks.DNA_EXTRACTOR.toComponent()
    }


    override fun craftItem() {
        if (!hasRecipe()) return

        val inputItem = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val outputItem = getOutputFromInput(inputItem) ?: return

        val amountAlreadyInOutput = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX).count
        outputItem.count = amountAlreadyInOutput + 1

        itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
        itemHandler.setStackInSlot(OUTPUT_SLOT_INDEX, outputItem)

        resetProgress()
    }

    override fun hasRecipe(): Boolean {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        val inputItemStack = inventory.getItem(INPUT_SLOT_INDEX)

        val inputItem = inputItemStack.item
        if (inputItem != ModItems.CELL.get() && inputItem != ModItems.GMO_CELL.get()) return false

        val outputItem = getOutputFromInput(inputItemStack) ?: return false

        return outputSlotHasRoom(inventory, outputItem)
    }

    private fun getOutputFromInput(input: ItemStack): ItemStack? {
        if (input.item == ModItems.CELL.get()) {
            val mobType = EntityDnaItem.getEntityType(input) ?: return null
            val dnaStack = ModItems.DNA_HELIX.toStack()

            val setWorked = setEntityType(dnaStack, mobType)
            if (!setWorked) {
                GeneticsResequenced.LOGGER.error("A DNA Extractor tried to set an invalid entity type at ${blockPos.x}, ${blockPos.y}, ${blockPos.z}: ${mobType.descriptionId}")
                return null
            }

            return dnaStack
        }

        if (input.item == ModItems.GMO_CELL.get()) {
            val gene = DnaHelixItem.getGene(input) ?: return null
            return DnaHelixItem.setGene(ModItems.DNA_HELIX.toStack(), gene)
        }

        return null
    }

    private fun outputSlotHasRoom(inventory: SimpleContainer, potentialOutput: ItemStack): Boolean {
        val stackInOutputSlot = inventory.getItem(OUTPUT_SLOT_INDEX)
        if (stackInOutputSlot.isEmpty) return true

        if (stackInOutputSlot.count + potentialOutput.count > stackInOutputSlot.maxStackSize) return false

        return ItemStack.isSameItemSameComponents(stackInOutputSlot, potentialOutput)
    }

    companion object {
        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: DnaExtractorBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }
    }
}