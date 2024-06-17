package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
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

class DnaDecryptorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.DNA_DECRYPTOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "dna_decryptor"

    override val energyMaximum: Int = 60_000
    override val energyTransferMaximum: Int = 256
    override val energyCostPerTick: Int = 32

    override val amountOfItemSlots: Int = 3
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                INPUT_SLOT_INDEX -> stack.item == ModItems.DNA_HELIX.get()
                OVERCLOCK_SLOT_INDEX -> stack.item == ModItems.OVERCLOCKER.get()
                OUTPUT_SLOT_INDEX -> false
                else -> false
            }
        }
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return DnaDecryptorMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.dna_decryptor")
    }

    private var isNextGeneSet = false
    private var nextGene: Gene? = null

    override fun craftItem() {
        if (!hasRecipe()) return

        val inputItem = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val outputItem = getOutputFromInput(inputItem) ?: return

        val amountAlreadyInOutput = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX).count
        outputItem.count = amountAlreadyInOutput + 1

        itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
        itemHandler.setStackInSlot(OUTPUT_SLOT_INDEX, outputItem)

        resetProgress()
        isNextGeneSet = false
    }

    override fun hasRecipe(): Boolean {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        val inputItem = inventory.getItem(INPUT_SLOT_INDEX).item
        if (inputItem != ModItems.DNA_HELIX.get()) return false

        val outputItem = getOutputFromInput(inventory.getItem(INPUT_SLOT_INDEX)) ?: return false

        return outputSlotHasRoom(inventory, outputItem)
    }

    private fun getOutputFromInput(input: ItemStack): ItemStack? {
        val possibleGenes = getPossibleGenes(input)

        val gene: Gene
        if (!isNextGeneSet) {
            gene = when (input.item) {
                ModItems.DNA_HELIX.get() -> possibleGenes.random()
                else -> throw IllegalStateException("Invalid item in input slot")
            }

            nextGene = gene
            isNextGeneSet = true
        } else {
            if (nextGene !in possibleGenes) {
                isNextGeneSet = false
                return null
            }

            if (nextGene != null) {
                gene = nextGene!!
            } else {
                return null
            }
        }

        return ItemStack(ModItems.DNA_HELIX.get()).setGene(gene)
    }

    private fun getPossibleGenes(input: ItemStack): List<Gene> {
        val mobType = EntityDnaItem.getEntityType(input) ?: return listOf(ModGenes.basic)

        val genesFromMob = MobGeneRegistry.getGeneWeights(mobType)
        if (genesFromMob.isEmpty()) return listOf(ModGenes.basic)

        return genesFromMob
            .map { it.key }
            .filter { it.isActive }
    }

    private fun outputSlotHasRoom(inventory: SimpleContainer, potentialOutput: ItemStack): Boolean {
        val outputSlot = inventory.getItem(OUTPUT_SLOT_INDEX)
        if (outputSlot.isEmpty) return true
        if (outputSlot.count + potentialOutput.count > outputSlot.maxStackSize) return false
        return outputSlot.getGene() == potentialOutput.getGene()
    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: DnaDecryptorBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }

    }
}