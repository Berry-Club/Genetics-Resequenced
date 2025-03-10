package dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
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


class PlasmidInfuserBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.PLASMID_INFUSER.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "plasmid_infuser"

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
                INPUT_SLOT_INDEX -> stack.`is`(ModItems.DNA_HELIX.get())
                OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER.get())
                OUTPUT_SLOT_INDEX -> stack.`is`(ModItems.PLASMID.get())
                else -> false
            }
        }
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return PlasmidInfuserMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return ModBlocks.PLASMID_INFUSER.get().name
    }

    override fun tick() {
        if (hasRecipe() && hasEnoughEnergy()) {
            extractEnergy()

            progress += 1 + amountOfOverclockers
            setChanged()

            if (progress >= maxProgress) {
                craftItem()
                resetProgress()
            }
        } else {
            resetProgress()
            setChanged()
        }
    }

    override fun craftItem() {
        if (!hasRecipe()) return
        val level = level ?: return

        val inputHelix = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val outputPlasmid = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

        val plasmidGeneHolder = PlasmidItem.getGene(outputPlasmid)
        val inputGeneHolder = DnaHelixItem.getGeneHolder(inputHelix)

        // If Plasmid is unset, set it to the Helix's gene and initialize the amount
        if (plasmidGeneHolder == null) {
            if (inputGeneHolder == null) return
            PlasmidItem.setGene(outputPlasmid, inputGeneHolder, 0)

            itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
            return
        }

        if (inputGeneHolder == null) return

        val inputAmount = when {
            inputGeneHolder.isGene(ModGenes.BASIC) -> 1
            inputGeneHolder.isGene(plasmidGeneHolder) -> 2
            else -> return
        }

        PlasmidItem.increaseDnaPoints(outputPlasmid, inputAmount)

        itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
    }

    override fun hasRecipe(): Boolean {
        val inventory = SimpleContainer(itemHandler.slots)
        for (i in 0 until itemHandler.slots) {
            inventory.setItem(i, itemHandler.getStackInSlot(i))
        }

        val inputHelix = inventory.getItem(INPUT_SLOT_INDEX)
        val outputPlasmid = inventory.getItem(OUTPUT_SLOT_INDEX)

        if (!inputHelix.`is`(ModItems.DNA_HELIX.get()) || !outputPlasmid.`is`(ModItems.PLASMID.get())) return false

        if (PlasmidItem.isComplete(outputPlasmid)) return false

        val plasmidGeneHolder = PlasmidItem.getGene(outputPlasmid)
        val inputGeneHolder = DnaHelixItem.getGeneHolder(inputHelix)

        val helixIsBasic = inputGeneHolder.isGene(ModGenes.BASIC)

        // If the Plasmid is unset, it can only accept a Helix that's neither basic nor null
        if (plasmidGeneHolder == null) {
            return !helixIsBasic && inputGeneHolder != null
        }

        if (!helixIsBasic) {
            if (inputGeneHolder != plasmidGeneHolder) return false
        }

        return true
    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: PlasmidInfuserBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }

    }

}