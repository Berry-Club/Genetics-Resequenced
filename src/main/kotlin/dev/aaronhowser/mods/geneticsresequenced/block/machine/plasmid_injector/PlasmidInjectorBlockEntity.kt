package dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler


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
    override val baseEnergyCostPerTick: Int = 32

    override val amountOfItemSlots: Int = 3
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                INPUT_SLOT_INDEX ->
                    (stack.item == ModItems.PLASMID.get() && PlasmidItem.isComplete(stack)) || (stack.item == ModItems.ANTI_PLASMID.get())

                OUTPUT_SLOT_INDEX ->
                    SyringeItem.hasBlood(stack) && !SyringeItem.isContaminated(stack)

                OVERCLOCK_SLOT_INDEX ->
                    stack.item == ModItems.OVERCLOCKER.get()

                else -> false
            }
        }
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return PlasmidInjectorMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return ModBlocks.PLASMID_INJECTOR.get().name
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

        if (!syringeStack.isSyringe()) return false

        when (plasmidStack.item) {
            ModItems.PLASMID.get() -> {
                val plasmidGene = PlasmidItem.getGene(plasmidStack) ?: return false
                if (!PlasmidItem.isComplete(plasmidStack)) return false
                return SyringeItem.canAddGene(syringeStack, plasmidGene)
            }

            ModItems.ANTI_PLASMID.get() -> {
                val antiPlasmidAntigene = PlasmidItem.getGene(plasmidStack) ?: return false
                return SyringeItem.canAddAntigene(syringeStack, antiPlasmidAntigene)
            }

            else -> return false
        }

    }

    override fun craftItem() {
        val plasmidStack = itemHandler.getStackInSlot(INPUT_SLOT_INDEX)
        val syringeStack = itemHandler.getStackInSlot(OUTPUT_SLOT_INDEX)

        val plasmidGene = PlasmidItem.getGene(plasmidStack) ?: return

        when (plasmidStack.item) {
            ModItems.PLASMID.get() -> {
                SyringeItem.addGene(syringeStack, plasmidGene)
            }

            ModItems.ANTI_PLASMID.get() -> {
                SyringeItem.addAntigene(syringeStack, plasmidGene)
            }

            else -> throw IllegalStateException("Invalid plasmid item")
        }

        itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
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