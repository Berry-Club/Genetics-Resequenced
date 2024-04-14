package dev.aaronhowser.mods.geneticsresequenced.screens

import dev.aaronhowser.mods.geneticsresequenced.blockentities.DnaDecryptorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.*
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.items.SlotItemHandler


class DnaDecryptorMenu : AbstractContainerMenu {

    val blockEntity: DnaDecryptorBlockEntity
    private val level: Level
    private val data: ContainerData

    constructor(id: Int, inventory: Inventory, extraData: FriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level.getBlockEntity(extraData.readBlockPos()) as DnaDecryptorBlockEntity,
                SimpleContainerData(DnaDecryptorBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    constructor(
        id: Int,
        inventory: Inventory,
        blockEntity: DnaDecryptorBlockEntity,
        containerData: ContainerData
    ) : super(ModMenuTypes.CELL_ANALYZER.get(), id) {
        checkContainerSize(inventory, DnaDecryptorBlockEntity.SIMPLE_CONTAINER_SIZE)

        this.blockEntity = blockEntity
        this.level = inventory.player.level
        this.data = containerData

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent { itemHandler ->
            this.addSlot(SlotItemHandler(itemHandler, DnaDecryptorBlockEntity.INPUT_SLOT, 63, 36))
            this.addSlot(SlotItemHandler(itemHandler, DnaDecryptorBlockEntity.OUTPUT_SLOT, 110, 36))
            this.addSlot(SlotItemHandler(itemHandler, DnaDecryptorBlockEntity.OVERCLOCK_SLOT, 149, 66))
        }

        addDataSlots(containerData)
    }

    private var progress: Int
        get() = data.get(DATA_PROGRESS_INDEX)
        set(value) {
            data.set(DATA_PROGRESS_INDEX, value)
        }

    private var maxProgress: Int
        get() = data.get(DATA_MAX_PROGRESS_INDEX)
        set(value) {
            data.set(DATA_MAX_PROGRESS_INDEX, value)
        }

    val isCrafting
        get() = progress > 0

    fun getScaledProgress(): Int {
        val progressArrowSize = DnaDecryptorScreen.ARROW_WIDTH

        return if (maxProgress == 0 || progress == 0) {
            0
        } else {
            progress * progressArrowSize / maxProgress
        }
    }

    companion object {

        private const val DATA_PROGRESS_INDEX = 0
        private const val DATA_MAX_PROGRESS_INDEX = 1

        private const val TOP_LEFT_INVENTORY_X = 8
        private const val TOP_LEFT_INVENTORY_Y = 84

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
            DnaDecryptorBlockEntity.ITEMSTACK_HANDLER_SIZE // must be the number of slots you have!
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
            println("Invalid slotIndex:$index")
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

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.CELL_ANALYZER
        )
    }

    // Adds the 27 slots of the player inventory
    private fun addPlayerInventory(playerInventory: Inventory) {
        for (row in 0 until 3) {
            for (column in 0 until 9) {
                addSlot(
                    Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        TOP_LEFT_INVENTORY_X + column * 18,
                        TOP_LEFT_INVENTORY_Y + row * 18
                    )
                )
            }
        }
    }

    // Adds the 9 slots of the player hotbar
    private fun addPlayerHotbar(playerInventory: Inventory) {
        for (column in 0 until 9) {
            addSlot(
                Slot(
                    playerInventory,
                    column,
                    TOP_LEFT_INVENTORY_X + column * 18,
                    TOP_LEFT_INVENTORY_Y + 4 + 3 * 18
                )
            )
        }
    }
}