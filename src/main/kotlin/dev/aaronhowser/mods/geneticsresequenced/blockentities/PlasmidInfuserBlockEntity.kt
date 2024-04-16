package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.blockentities.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.isGeneric
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.screens.PlasmidInfuserMenu
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
    override val inventoryNbtKey: String = "plasmid_infuser.inventory"
    override val energyNbtKey: String = "plasmid_infuser.energy"
    override val energyMaximum: Int = 60_000
    override val energyTransferMaximum: Int = 256
    override val energyCostPerTick: Int = 32

    override val itemHandler: ItemStackHandler = object : ItemStackHandler(ITEMSTACK_HANDLER_SIZE) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                INPUT_SLOT_INDEX -> stack.`is`(ModItems.DNA_HELIX)
                OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER)
                OUTPUT_SLOT_INDEX -> stack.`is`(ModItems.PLASMID)
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

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: PlasmidInfuserBlockEntity
        ) {
            if (level.isClientSide) return

            if (hasRecipe(blockEntity) && hasEnoughEnergy(blockEntity)) {
                extractEnergy(blockEntity)

                blockEntity.progress += 1 + blockEntity.amountOfOverclockers
                blockEntity.setChanged()

                if (blockEntity.progress >= blockEntity.maxProgress) {
                    craftItem(blockEntity)
                    blockEntity.resetProgress()
                }
            } else {
                blockEntity.resetProgress()
                blockEntity.setChanged()
            }

        }

        private fun extractEnergy(blockEntity: PlasmidInfuserBlockEntity) {
            if (blockEntity.energyStorage.energyStored < ENERGY_PER_TICK) return
            blockEntity.energyStorage.extractEnergy(ENERGY_PER_TICK, false)
        }

        private fun hasEnoughEnergy(blockEntity: PlasmidInfuserBlockEntity): Boolean {
            return blockEntity.energyStorage.energyStored >= ENERGY_PER_TICK
        }


        private fun craftItem(blockEntity: PlasmidInfuserBlockEntity) {
            if (!hasRecipe(blockEntity)) return

            val inputItem = blockEntity.itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
            val outputItem = blockEntity.itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

            val plasmidGene = PlasmidItem.getGene(outputItem)
            val inputGene = inputItem.getGene()

            // If Plasmid is unset, set it to the Helix's gene and initialize the amount
            if (plasmidGene == null) {
                if (inputGene == null) return
                PlasmidItem.setGene(outputItem, inputGene)
                PlasmidItem.increaseAmount(outputItem, 1)

                blockEntity.itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
                return
            }

            if (inputItem.isGeneric()) {
                PlasmidItem.increaseAmount(outputItem)
                blockEntity.itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
                return
            } else {
                if (inputGene != plasmidGene) return
                PlasmidItem.increaseAmount(outputItem, 2)
                blockEntity.itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
            }

        }

        private fun hasRecipe(blockEntity: PlasmidInfuserBlockEntity): Boolean {
            val inventory = SimpleContainer(blockEntity.itemHandler.slots)
            for (i in 0 until blockEntity.itemHandler.slots) {
                inventory.setItem(i, blockEntity.itemHandler.getStackInSlot(i))
            }

            val inputItem = inventory.getItem(INPUT_SLOT_INDEX)
            val outputItem = inventory.getItem(OUTPUT_SLOT_INDEX)

            if (!inputItem.`is`(ModItems.DNA_HELIX) || !outputItem.`is`(ModItems.PLASMID)) return false

            if (PlasmidItem.isComplete(outputItem)) return false

            val plasmidGene = PlasmidItem.getGene(outputItem)
            val inputGene = inputItem.getGene()
            val helixIsGeneric = inputItem.isGeneric()

            // If the Plasmid is unset, it can only accept a Helix that's neither generic nor null
            if (plasmidGene == null) {
                return !(helixIsGeneric || inputGene == null)
            }

            if (!helixIsGeneric) {
                if (inputGene != plasmidGene) return false
            }

            return true
        }

        private const val ENERGY_PER_TICK = 32

    }


}