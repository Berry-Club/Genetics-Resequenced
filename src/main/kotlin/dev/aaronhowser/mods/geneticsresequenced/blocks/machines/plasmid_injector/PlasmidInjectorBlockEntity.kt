package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserMenu
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
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
                INPUT_SLOT_INDEX -> stack.`is`(ModItems.PLASMID)
                OUTPUT_SLOT_INDEX -> stack.`is`(ModItems.SYRINGE)
                OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER)
                else -> false
            }
        }
    }

    override val menuType: Class<out MachineMenu> = PlasmidInjectorMenu::class.java
    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return PlasmidInjectorMenu(pContainerId, pPlayerInventory, this, this.containerData)
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

    override fun hasRecipe(): Boolean {
        return false
    }

    override fun craftItem() {

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