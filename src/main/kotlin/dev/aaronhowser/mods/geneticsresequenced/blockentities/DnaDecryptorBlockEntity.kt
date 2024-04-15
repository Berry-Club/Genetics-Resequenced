package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.EnergySyncPacket
import dev.aaronhowser.mods.geneticsresequenced.screens.DnaDecryptorMenu
import dev.aaronhowser.mods.geneticsresequenced.util.ModEnergyStorage
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
import net.minecraftforge.energy.IEnergyStorage
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemStackHandler

class DnaDecryptorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(
    ModBlockEntities.DNA_DECRYPTOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    private val itemHandler: ItemStackHandler = object : ItemStackHandler(ITEMSTACK_HANDLER_SIZE) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                INPUT_SLOT -> stack.`is`(ModItems.DNA_HELIX)
                OUTPUT_SLOT -> false
                OVERCLOCK_SLOT -> false
                else -> false
            }
        }
    }

    private var lazyItemHandler = LazyOptional.empty<IItemHandler>()

    private val energyStorage: ModEnergyStorage = object : ModEnergyStorage(CAPACITY, MAX_TRANSFER) {
        override fun onEnergyChanged() {
            setChanged()

            ModPacketHandler.messageAllPlayers(EnergySyncPacket(energy, blockPos))
        }
    }

    private var lazyEnergyStorage = LazyOptional.empty<IEnergyStorage>()

    override fun <T : Any?> getCapability(cap: Capability<T>, side: Direction?): LazyOptional<T> {
        return when (cap) {
            ForgeCapabilities.ITEM_HANDLER -> getItemCapability(side).cast()

            ForgeCapabilities.ENERGY -> getEnergyCapability(side).cast()

            else -> super.getCapability(cap, side)
        }
    }

    private fun getItemCapability(side: Direction?): LazyOptional<IItemHandler> {
        return lazyItemHandler.cast()
    }

    private fun getEnergyCapability(side: Direction?): LazyOptional<IEnergyStorage> {
        return lazyEnergyStorage.cast()
    }

    fun getEnergyStorage(): IEnergyStorage = energyStorage

    override fun onLoad() {
        super.onLoad()
        lazyItemHandler = LazyOptional.of { itemHandler }
        lazyEnergyStorage = LazyOptional.of { energyStorage }
    }

    override fun invalidateCaps() {
        super.invalidateCaps()
        lazyItemHandler.invalidate()
        lazyEnergyStorage.invalidate()
    }

    override fun saveAdditional(pTag: CompoundTag) {
        pTag.put(INVENTORY_NBT_KEY, itemHandler.serializeNBT())
        pTag.putInt(ENERGY_NBT_KEY, energyStorage.energyStored)
        super.saveAdditional(pTag)
    }

    override fun load(pTag: CompoundTag) {
        super.load(pTag)
        itemHandler.deserializeNBT(pTag.getCompound(INVENTORY_NBT_KEY))
        energyStorage.setEnergy(pTag.getInt(ENERGY_NBT_KEY))
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

    // Client only
    fun setEnergy(energy: Int) {
        this.energyStorage.setEnergy(energy)
    }

    private val data = object : ContainerData {
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

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return DnaDecryptorMenu(pContainerId, pPlayerInventory, this, this.data)
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

        private const val INVENTORY_NBT_KEY = "dna_decryptor.inventory"
        private const val PROGRESS_INDEX = 0
        private const val MAX_PROGRESS_INDEX = 1

        const val SIMPLE_CONTAINER_SIZE = 2
        const val ITEMSTACK_HANDLER_SIZE = 3

        const val INPUT_SLOT = 0
        const val OUTPUT_SLOT = 1
        const val OVERCLOCK_SLOT = 2

        private const val ENERGY_NBT_KEY = "dna_decryptor.energy"
        private const val CAPACITY = 60_000
        private const val MAX_TRANSFER = 256
        private const val ENERGY_PER_TICK = 32

    }
}