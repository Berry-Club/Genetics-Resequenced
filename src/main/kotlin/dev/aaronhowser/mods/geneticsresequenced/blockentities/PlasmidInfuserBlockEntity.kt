package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.blockentities.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
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
                OUTPUT_SLOT_INDEX -> false
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
            val outputItem = getOutputFromInput(inputItem, blockEntity) ?: return

        }

        private fun hasRecipe(blockEntity: PlasmidInfuserBlockEntity): Boolean {

            return false

            val inventory = SimpleContainer(blockEntity.itemHandler.slots)
            for (i in 0 until blockEntity.itemHandler.slots) {
                inventory.setItem(i, blockEntity.itemHandler.getStackInSlot(i))
            }

//            return outputSlotHasRoom(inventory, outputItem)
        }

        private fun getOutputFromInput(input: ItemStack, blockEntity: PlasmidInfuserBlockEntity): ItemStack? {

            return null

            val mobType = EntityDnaItem.getEntityType(input) ?: return null
            val genesFromMob = MobGenesRegistry.getGenesForEntity(mobType)
            if (genesFromMob.isEmpty()) return null

//
//            val gene: Gene
//            if (blockEntity.nextGene == null) {
//                gene = genesFromMob.map { it.key }.random()!!
//                blockEntity.nextGene = gene
//            } else {
//
//                if (!genesFromMob.contains(blockEntity.nextGene)) {
//                    blockEntity.nextGene = null
//                    return null
//                }
//
//                gene = blockEntity.nextGene!!
//            }

//            return ItemStack(ModItems.DNA_HELIX).setGene(gene)
        }

        private fun outputSlotHasRoom(inventory: SimpleContainer, potentialOutput: ItemStack): Boolean {
            val outputSlot = inventory.getItem(OUTPUT_SLOT_INDEX)

            if (outputSlot.isEmpty) return true

            if (outputSlot.count + potentialOutput.count > outputSlot.maxStackSize) return false

            return DnaHelixItem.getGene(outputSlot) == DnaHelixItem.getGene(potentialOutput)
        }

        private const val ENERGY_PER_TICK = 32

    }


}