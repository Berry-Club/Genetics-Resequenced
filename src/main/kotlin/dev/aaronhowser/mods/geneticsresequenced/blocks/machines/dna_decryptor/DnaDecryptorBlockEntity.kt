package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.GmoItem.Companion.getGeneChance
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
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
import kotlin.random.Random

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
                INPUT_SLOT_INDEX -> stack.item == ModItems.DNA_HELIX.get() || stack.item == ModItems.GMO_DNA_HELIX.get()
                OVERCLOCK_SLOT_INDEX -> stack.item == ModItems.OVERCLOCKER.get()
                OUTPUT_SLOT_INDEX -> false
                else -> false
            }
        }
    }

    override val menuType: Class<out MachineMenu> = DnaDecryptorMenu::class.java
    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return DnaDecryptorMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.dna_decryptor")
    }

    var isNextGeneSet = false
    var nextGene: Gene? = null

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
        if (inputItem != ModItems.DNA_HELIX.get() && inputItem != ModItems.GMO_DNA_HELIX.get()) return false

        val outputItem = getOutputFromInput(inventory.getItem(INPUT_SLOT_INDEX)) ?: return false

        return outputSlotHasRoom(inventory, outputItem)
    }

    private fun getOutputFromInput(input: ItemStack): ItemStack? {
        val possibleGenes = getPossibleGenes(input)

        val gene: Gene
        if (!isNextGeneSet) {
            gene = when (input.item) {
                ModItems.DNA_HELIX.get() -> getGeneFromRegular(input)
                ModItems.GMO_DNA_HELIX.get() -> getGeneFromGmo(input)
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

    private fun getPossibleGmoGenes(input: ItemStack): List<Gene> {
        val idealGene = input.getGene() ?: return listOf(DefaultGenes.BASIC)

        if (!idealGene.isMutation) {
            return listOf(idealGene, DefaultGenes.BASIC)
        }

        return idealGene.mutatesFrom.toList() + idealGene
    }

    private fun getPossibleGenes(input: ItemStack): List<Gene> {
        if (input.item == ModItems.GMO_DNA_HELIX.get()) {
            return getPossibleGmoGenes(input)
        }

        val mobType = EntityDnaItem.getEntityType(input) ?: return listOf(DefaultGenes.BASIC)
        val genesFromMob = MobGenesRegistry.getGenesForEntity(mobType)
        if (genesFromMob.isEmpty()) return listOf(DefaultGenes.BASIC)

        return genesFromMob
            .map { it.key }
            .filter { it.isActive }
    }

    private fun getGeneFromRegular(input: ItemStack): Gene {
        val possibleGenes = getPossibleGenes(input)
        return possibleGenes.random()
    }

    private fun getGeneFromGmo(input: ItemStack): Gene {
        val idealGene = input.getGene() ?: return DefaultGenes.BASIC
        val chance = input.getGeneChance()

        if (Random.nextFloat() < chance) return idealGene

        val possibleGenes = getPossibleGmoGenes(input) - idealGene
        return possibleGenes.random()
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