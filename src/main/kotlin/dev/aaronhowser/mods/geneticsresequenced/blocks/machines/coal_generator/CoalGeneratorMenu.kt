package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.screens.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.common.ForgeHooks
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.items.SlotItemHandler
import java.text.NumberFormat

class CoalGeneratorMenu(
    id: Int,
    inventory: Inventory,
    blockEntity: CoalGeneratorBlockEntity,
    private val containerData: ContainerData
) : MachineMenu(
    ModMenuTypes.COAL_GENERATOR.get(),
    blockEntity,
    id,
    inventory
) {

    constructor(id: Int, inventory: Inventory, extraData: FriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level.getBlockEntity(extraData.readBlockPos()) as CoalGeneratorBlockEntity,
                SimpleContainerData(CoalGeneratorBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, CoalGeneratorBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent { itemHandler ->
            this.addSlot(SlotItemHandler(itemHandler, CoalGeneratorBlockEntity.INPUT_SLOT, 52, 34))
        }

        addDataSlots(containerData)
    }

    private var maxBurnTime: Int
        get() = containerData.get(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX)
        set(value) {
            containerData.set(CoalGeneratorBlockEntity.MAX_BURN_TIME_INDEX, value)
        }

    private var burnTimeRemaining: Int
        get() = containerData.get(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX)
        set(value) {
            containerData.set(CoalGeneratorBlockEntity.REMAINING_TICKS_INDEX, value)
        }

    val isBurning
        get() = burnTimeRemaining > 0

    fun getPercentDone(): Double {
        if (maxBurnTime == 0) return 0.0

        return 1.0 - burnTimeRemaining.toDouble() / maxBurnTime.toDouble()
    }

    fun getScaledFuelRemaining(): Int {
        val fuelSize = CoalGeneratorScreen.BURN_HEIGHT

        return if (maxBurnTime == 0) {
            0
        } else {
            fuelSize - (fuelSize * getPercentDone()).toInt()
        }
    }

    fun getScaledProgressArrow(): Int {
        val progressArrowSize = CoalGeneratorScreen.ARROW_WIDTH

        return if (maxBurnTime == 0) {
            0
        } else {
            (progressArrowSize.toDouble() * getPercentDone()).toInt()
        }
    }

    companion object {

        fun showFuelTooltip(event: ItemTooltipEvent) {
            val itemStack = event.itemStack
            val fuel = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING)
            if (fuel <= 0) return

            val feProduced = CoalGeneratorBlockEntity.energyPerTick * fuel
            val feString = NumberFormat.getNumberInstance().format(feProduced)

            event.toolTip.add(
                1, Component.literal("$feString FE").withColor(ChatFormatting.GRAY)
            )
        }

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
            CoalGeneratorBlockEntity.SIMPLE_CONTAINER_SIZE // must be the number of slots you have!
    }

    //FIXME: Crashes when you shift click items in
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

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.COAL_GENERATOR
        )
    }
}