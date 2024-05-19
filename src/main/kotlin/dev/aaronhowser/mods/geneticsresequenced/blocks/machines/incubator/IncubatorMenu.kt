package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.screens.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.items.SlotItemHandler

class IncubatorMenu(
    id: Int,
    inventory: Inventory,
    blockEntity: IncubatorBlockEntity,
    private val containerData: ContainerData
) : MachineMenu(
    ModMenuTypes.INCUBATOR.get(),
    blockEntity,
    id,
    inventory
) {

    constructor(id: Int, inventory: Inventory, extraData: FriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level.getBlockEntity(extraData.readBlockPos()) as IncubatorBlockEntity,
                SimpleContainerData(CraftingMachineBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, CraftingMachineBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent { itemHandler ->
            this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 36))
            this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 36))
            this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 149, 66))
        }

        addDataSlots(containerData)
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.INCUBATOR
        )
    }

    private var progress: Int
        get() = containerData.get(DATA_PROGRESS_INDEX)
        set(value) {
            containerData.set(DATA_PROGRESS_INDEX, value)
        }

    private var maxProgress: Int
        get() = containerData.get(DATA_MAX_PROGRESS_INDEX)
        set(value) {
            containerData.set(DATA_MAX_PROGRESS_INDEX, value)
        }

    val isCrafting
        get() = progress > 0

    fun getScaledProgress(): Int {
        val progressArrowSize = IncubatorScreen.ARROW_WIDTH

        return if (maxProgress == 0 || progress == 0) {
            0
        } else {
            progress * progressArrowSize / maxProgress
        }
    }

    companion object {

        private const val DATA_PROGRESS_INDEX = 0
        private const val DATA_MAX_PROGRESS_INDEX = 1

        // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
        // must assign a slot number to each of the slots used by the GUI.
        // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
        // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
        //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
        //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
        //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
        private const val HOTBAR_SLOT_COUNT = 9
        private const val PLAYER_INVENTORY_ROW_COUNT = 3
        private const val PLAYER_INVENTORY_COLUMN_COUNT = 9
        private const val PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT
        private const val VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT
        private const val VANILLA_FIRST_SLOT_INDEX = 0
        private const val TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT

        // THIS YOU HAVE TO DEFINE!
        private const val TE_INVENTORY_SLOT_COUNT =
            CraftingMachineBlockEntity.ITEMSTACK_HANDLER_SIZE // must be the number of slots you have!
    }

    override fun quickMoveStack(playerIn: Player, index: Int): ItemStack {
        val sourceSlot = slots.getOrNull(index)
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY //EMPTY_ITEM
        val sourceStack = sourceSlot.item
        val copyOfSourceStack = sourceStack.copy()

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(
                    sourceStack,
                    TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT,
                    false
                )
            ) {
                return ItemStack.EMPTY // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(
                    sourceStack,
                    VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
                    false
                )
            ) {
                return ItemStack.EMPTY
            }
        } else {
            GeneticsResequenced.LOGGER.error("Invalid slotIndex: $index")
            return ItemStack.EMPTY
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.count == 0) {
            sourceSlot.set(ItemStack.EMPTY)
        } else {
            sourceSlot.setChanged()
        }
        sourceSlot.onTake(playerIn, sourceStack)
        return copyOfSourceStack
    }

}