package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.blockentities.base.CraftingMachine
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.screens.DnaDecryptorMenu
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
) : CraftingMachine(
    ModBlockEntities.DNA_DECRYPTOR.get(),
    pPos,
    pBlockState
), MenuProvider {
    override val inventoryNbtKey: String = "dna_decryptor.inventory"
    override val energyNbtKey: String = "dna_decryptor.energy"
    override val energyMaximum: Int = 60_000
    override val energyTransferMaximum: Int = 256
    override val energyCostPerTick: Int = 32

    override val itemHandler: ItemStackHandler = object : ItemStackHandler(itemstackHandlerSize) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                inputSlot -> stack.`is`(ModItems.DNA_HELIX)
                outputSlot -> false
                OVERCLOCK_SLOT -> false
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

    var nextGene: Gene? = null

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: DnaDecryptorBlockEntity
        ) {
            if (level.isClientSide) return

            if (hasRecipe(blockEntity) && hasEnoughEnergy(blockEntity)) {
                extractEnergy(blockEntity)

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

        private fun extractEnergy(blockEntity: DnaDecryptorBlockEntity) {
            if (blockEntity.energyStorage.energyStored < ENERGY_PER_TICK) return
            blockEntity.energyStorage.extractEnergy(ENERGY_PER_TICK, false)
        }

        private fun hasEnoughEnergy(blockEntity: DnaDecryptorBlockEntity): Boolean {
            return blockEntity.energyStorage.energyStored >= ENERGY_PER_TICK
        }


        private fun craftItem(blockEntity: DnaDecryptorBlockEntity) {
            if (!hasRecipe(blockEntity)) return

            val inputItem = blockEntity.itemHandler.getStackInSlot(INPUT_SLOT)
            val outputItem = getOutputFromInput(inputItem, blockEntity) ?: return

            val amountAlreadyInOutput = blockEntity.itemHandler.getStackInSlot(OUTPUT_SLOT).count
            outputItem.count = amountAlreadyInOutput + 1

            blockEntity.itemHandler.extractItem(INPUT_SLOT, 1, false)
            blockEntity.itemHandler.setStackInSlot(OUTPUT_SLOT, outputItem)

            blockEntity.resetProgress()
            blockEntity.nextGene = null
        }

        private fun hasRecipe(blockEntity: DnaDecryptorBlockEntity): Boolean {
            val inventory = SimpleContainer(blockEntity.itemHandler.slots)
            for (i in 0 until blockEntity.itemHandler.slots) {
                inventory.setItem(i, blockEntity.itemHandler.getStackInSlot(i))
            }

            if (!inventory.getItem(INPUT_SLOT).`is`(ModItems.DNA_HELIX)) return false

            val outputItem = getOutputFromInput(inventory.getItem(INPUT_SLOT), blockEntity) ?: return false

            return outputSlotHasRoom(inventory, outputItem)
        }

        private fun getOutputFromInput(input: ItemStack, blockEntity: DnaDecryptorBlockEntity): ItemStack? {
            val mobType = EntityDnaItem.getEntityType(input) ?: return null
            val genesFromMob = MobGenesRegistry.getGenesForEntity(mobType)
            if (genesFromMob.isEmpty()) return null

            val gene: Gene
            if (blockEntity.nextGene == null) {
                gene = genesFromMob.random()
                blockEntity.nextGene = gene
            } else {

                if (!genesFromMob.contains(blockEntity.nextGene)) {
                    blockEntity.nextGene = null
                    return null
                }

                gene = blockEntity.nextGene!!
            }

            return ItemStack(ModItems.DNA_HELIX).setGene(gene)
        }

        private fun outputSlotHasRoom(inventory: SimpleContainer, potentialOutput: ItemStack): Boolean {
            val outputSlot = inventory.getItem(OUTPUT_SLOT)

            if (outputSlot.isEmpty) return true

            if (outputSlot.count + potentialOutput.count > outputSlot.maxStackSize) return false

            return DnaHelixItem.getGene(outputSlot) == DnaHelixItem.getGene(potentialOutput)
        }

        const val SIMPLE_CONTAINER_SIZE = 2
        const val ITEMSTACK_HANDLER_SIZE = 3

        const val INPUT_SLOT = 0
        const val OUTPUT_SLOT = 1
        const val OVERCLOCK_SLOT = 2

        private const val ENERGY_PER_TICK = 32

    }
}