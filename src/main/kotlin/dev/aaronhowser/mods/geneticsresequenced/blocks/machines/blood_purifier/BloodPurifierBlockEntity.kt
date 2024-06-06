package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.blood_purifier

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipes.machine.BloodPurifierRecipe
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

class BloodPurifierBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.BLOOD_PURIFIER.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "blood_purifier"

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
                INPUT_SLOT_INDEX -> SyringeItem.hasBlood(stack)
                OVERCLOCK_SLOT_INDEX -> stack.`is`(ModItems.OVERCLOCKER.get())
                OUTPUT_SLOT_INDEX -> false
                else -> false
            }
        }
    }

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return BloodPurifierMenu(pContainerId, pPlayerInventory, this, this.containerData)
    }

    override fun getDisplayName(): Component {
        return Component.translatable("block.geneticsresequenced.blood_purifier")
    }

    override fun craftItem() {
        if (!hasRecipe()) return

        val realLevel = level ?: return

        val inventory = SimpleContainer(itemHandler.slots)

        val recipe = realLevel.recipeManager.getRecipeFor(
            BloodPurifierRecipe.recipeType,
            inventory,
            realLevel
        )

        itemHandler.extractItem(INPUT_SLOT_INDEX, 1, false)
        itemHandler.insertItem(OUTPUT_SLOT_INDEX, recipe.get().resultItem, false)

    }

    override fun hasRecipe(): Boolean {
        val realLevel = level ?: return false

        val inventory = SimpleContainer(itemHandler.slots)

        val recipe = realLevel.recipeManager.getRecipeFor(
            BloodPurifierRecipe.recipeType,
            inventory,
            realLevel
        )

        return recipe.isPresent
    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: BloodPurifierBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }

    }
}