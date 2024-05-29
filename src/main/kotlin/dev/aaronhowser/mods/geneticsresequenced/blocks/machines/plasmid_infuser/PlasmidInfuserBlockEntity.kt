package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.isBasic
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem.Companion.increaseAmount
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
    override val energyCostPerTick: Int = 32

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
        return Component.translatable("block.geneticsresequenced.plasmid_infuser")
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

        val inputHelix = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val outputPlasmid = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

        val plasmidGene = outputPlasmid.getGene()
        val inputGene = inputHelix.getGene()

        // If Plasmid is unset, set it to the Helix's gene and initialize the amount
        if (plasmidGene == null) {
            if (inputGene == null) return
            outputPlasmid.setGene(inputGene).increaseAmount(1)

            itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
            return
        }

        if (inputHelix.isBasic()) {
            outputPlasmid.increaseAmount(1)
            itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
            return
        } else {
            if (inputGene != plasmidGene) return
            outputPlasmid.increaseAmount(2)
            itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
        }

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

        val plasmidGene = outputPlasmid.getGene()
        val inputGene = inputHelix.getGene()
        val helixIsBasic = inputHelix.isBasic()

        // If the Plasmid is unset, it can only accept a Helix that's neither basic nor null
        if (plasmidGene == null) {
            return !(helixIsBasic || inputGene == null)
        }

        if (!helixIsBasic) {
            if (inputGene != plasmidGene) return false
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