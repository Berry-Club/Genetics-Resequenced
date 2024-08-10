package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.SlotItemHandler


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
                inventory.player.level().getBlockEntity(extraData.readBlockPos()) as IncubatorBlockEntity,
                SimpleContainerData(IncubatorBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, IncubatorBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        val itemHandler = this.blockEntity.itemHandler

        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.TOP_SLOT_INDEX, 83, 21))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 55))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 62))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 55))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 141, 38))

        addDataSlots(containerData)
    }

    override fun getPercentDone(): Float {
        if (ticksRemaining <= 0) return 0f

        val maxProgress = IncubatorBlockEntity.ticksPerBrew
        val progress = maxProgress - ticksRemaining

        return if (maxProgress != 0) {
            progress.toFloat() / maxProgress.toFloat()
        } else {
            0f
        }
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.INCUBATOR.get()
        )
    }

    private var ticksRemaining: Int
        get() = containerData.get(IncubatorBlockEntity.REMAINING_TICKS_INDEX)
        set(value) {
            containerData.set(IncubatorBlockEntity.REMAINING_TICKS_INDEX, value)
        }

    val isCrafting
        get() = ticksRemaining > 0

}