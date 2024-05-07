package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.items.ItemStackHandler

class PlasmidInjectorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.PLASMID_INJECTOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "plasmid_injector"

    override val energyMaximum: Int = 60_000
    override val energyTransferMaximum: Int = 256
    override val energyCostPerTick: Int = 32

    override val itemHandler: ItemStackHandler = object : ItemStackHandler(ITEMSTACK_HANDLER_SIZE) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                INPUT_SLOT_INDEX ->
                    stack.`is`(ModItems.PLASMID) && PlasmidItem.isComplete(stack)

                OUTPUT_SLOT_INDEX ->
                    stack.`is`(ModItems.SYRINGE) && SyringeItem.hasBlood(stack) && !SyringeItem.isContaminated(stack)

                OVERCLOCK_SLOT_INDEX ->
                    stack.`is`(ModItems.OVERCLOCKER)

                else -> false
            }
        }
    }

    override val menuType: Class<out MachineMenu> = PlasmidInjectorMenu::class.java
    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return PlasmidInjectorMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.plasmid_injector")
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

    override fun hasRecipe(): Boolean {
        val plasmidStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val syringeStack = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

        if (!plasmidStack.`is`(PlasmidItem) || !syringeStack.`is`(SyringeItem)) return false

        val plasmidGene = plasmidStack.getGene() ?: return false
        if (!PlasmidItem.isComplete(plasmidStack)) return false

        return SyringeItem.canAddGene(syringeStack, plasmidGene)
    }

    override fun craftItem() {
        val plasmidStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val syringeStack = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

        val plasmidGene = plasmidStack.getGene() ?: return

        itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)

        SyringeItem.addGene(syringeStack, plasmidGene)
    }

    companion object {
        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: PlasmidInjectorBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }
    }

}