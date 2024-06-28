package dev.aaronhowser.mods.geneticsresequenced.block.machines.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.block.machines.incubator.IncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.items.SlotItemHandler


class AdvancedIncubatorMenu(
    id: Int,
    inventory: Inventory,
    blockEntity: AdvancedIncubatorBlockEntity,
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
                inventory.player.level().getBlockEntity(extraData.readBlockPos()) as AdvancedIncubatorBlockEntity,
                SimpleContainerData(AdvancedIncubatorBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, AdvancedIncubatorBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        val itemHandler = this.blockEntity.itemHandler

        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.TOP_SLOT_INDEX, 83, 15))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 49))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 56))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 49))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.CHORUS_SLOT_INDEX, 141, 32))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 141, 54))

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

    var isHighTemperature: Boolean
        get() = containerData.get(AdvancedIncubatorBlockEntity.IS_HIGH_TEMPERATURE_INDEX) == 1
        private set(value) {
            containerData.set(AdvancedIncubatorBlockEntity.IS_HIGH_TEMPERATURE_INDEX, if (value) 1 else 0)
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

    override fun clickMenuButton(pPlayer: Player, pId: Int): Boolean {
        if (pPlayer is ServerPlayer) {
            if (pId == 1) {
                toggleTemperature()
                return true
            }
        }

        return false
    }

    private fun toggleTemperature() {
        isHighTemperature = !isHighTemperature
        (blockEntity as AdvancedIncubatorBlockEntity).resetBrewTime()
    }

    companion object {
        private const val DATA_PROGRESS_INDEX = 0
    }

}