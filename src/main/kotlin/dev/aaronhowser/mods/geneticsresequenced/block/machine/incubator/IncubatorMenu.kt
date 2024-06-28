package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
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

        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.TOP_SLOT_INDEX, 83, 15))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 49))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 56))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 49))
        this.addSlot(SlotItemHandler(itemHandler, IncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 141, 32))

        addDataSlots(containerData)
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.INCUBATOR.get()
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
        val progressArrowSize = ScreenTextures.Elements.ArrowDown.Dimensions.HEIGHT

        return if (progress == 0) {
            0
        } else {
            progress * progressArrowSize / IncubatorBlockEntity.ticksPerBrew
        }
    }

    companion object {
        private const val DATA_PROGRESS_INDEX = 0
    }

}