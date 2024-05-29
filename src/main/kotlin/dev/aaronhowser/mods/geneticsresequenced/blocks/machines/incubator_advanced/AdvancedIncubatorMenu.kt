package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.screens.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.screens.base.MachineMenu
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.items.SlotItemHandler

class AdvancedIncubatorMenu(
    id: Int,
    inventory: Inventory,
    override val blockEntity: AdvancedIncubatorBlockEntity,
    private val containerData: ContainerData
) : MachineMenu(
    ModMenuTypes.ADVANCED_INCUBATOR.get(),
    blockEntity,
    id,
    inventory
) {

    constructor(id: Int, inventory: Inventory, extraData: FriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level.getBlockEntity(extraData.readBlockPos()) as AdvancedIncubatorBlockEntity,
                SimpleContainerData(AdvancedIncubatorBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, AdvancedIncubatorBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent { itemHandler ->
            this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.TOP_SLOT_INDEX, 83, 15))
            this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 49))
            this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 56))
            this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 49))
            this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.CHORUS_SLOT_INDEX, 141, 32))
            this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 146, 67))
        }

        addDataSlots(containerData)
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.ADVANCED_INCUBATOR.get()
        )
    }

    private var progress: Int
        get() = containerData.get(DATA_PROGRESS_INDEX)
        set(value) {
            containerData.set(DATA_PROGRESS_INDEX, value)
        }

    val isCrafting
        get() = progress > 0

    fun getScaledProgress(): Int {
        val progressArrowSize = AdvancedIncubatorScreen.ARROW_HEIGHT

        return if (progress == 0) {
            0
        } else {
            progress * progressArrowSize / AdvancedIncubatorBlockEntity.TICKS_PER
        }
    }

    fun toggleTemperature() {
        blockEntity.toggleTemperature()
    }

    companion object {
        private const val DATA_PROGRESS_INDEX = 0
    }

}