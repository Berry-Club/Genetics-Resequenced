package dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_decryptor

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.data.MobGeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
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
    override val baseEnergyCostPerTick: Int = 32

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
        return ModBlocks.DNA_DECRYPTOR.get().name
    }

    private var isNextGeneSet = false
    private var nextGene: Holder<Gene>? = null

    override fun craftItem() {
        if (!hasRecipe()) return

        val inputItem = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val outputItem = getOutputFromInput(inputItem, level?.registryAccess()!!) ?: return

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

        val inputStack = inventory.getItem(INPUT_SLOT_INDEX)
        if (inputStack.item != ModItems.DNA_HELIX.get()) return false
        if (DnaHelixItem.getGene(inputStack, this.level?.registryAccess()!!) != null) return false

        val outputItem = getOutputFromInput(
            inventory.getItem(INPUT_SLOT_INDEX),
            level?.registryAccess()!!
        ) ?: return false

        return outputSlotHasRoom(inventory, outputItem)
    }

    private fun getOutputFromInput(input: ItemStack, registries: HolderLookup.Provider): ItemStack? {
        val possibleGenes = getPossibleGenes(input, registries)

        val gene: Holder<Gene>
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

        val helixStack = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGene(helixStack, gene)

        return helixStack
    }

    private fun getPossibleGenes(input: ItemStack, registries: HolderLookup.Provider): List<Holder<Gene>> {
        val basic = ModGenes.BASIC.getHolder(registries)!!
        val mobType = EntityDnaItem.getEntityType(input) ?: return listOf(basic)

        val genesFromMob = MobGeneRegistry.getGeneWeights(mobType)
        if (genesFromMob.isEmpty()) return listOf(basic)

        return genesFromMob
            .map { it.key }
            .filter { it.value().isActive }
    }

    private fun outputSlotHasRoom(inventory: SimpleContainer, potentialOutput: ItemStack): Boolean {
        val currentItemInOutput = inventory.getItem(OUTPUT_SLOT_INDEX)
        if (currentItemInOutput.isEmpty) return true
        if (currentItemInOutput.count + potentialOutput.count > currentItemInOutput.maxStackSize) return false

        return DnaHelixItem.getGene(currentItemInOutput, this.level?.registryAccess()!!) ==
                DnaHelixItem.getGene(potentialOutput, this.level?.registryAccess()!!)
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