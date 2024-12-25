package dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
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


class DnaExtractorMenu(
    id: Int,
    inventory: Inventory,
    blockEntity: DnaExtractorBlockEntity,
    private val containerData: ContainerData
) : MachineMenu(
    ModMenuTypes.DNA_EXTRACTOR.get(),
    blockEntity,
    id,
    inventory
) {
    constructor(id: Int, inventory: Inventory, extraData: FriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level().getBlockEntity(extraData.readBlockPos()) as DnaExtractorBlockEntity,
                SimpleContainerData(CraftingMachineBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, CraftingMachineBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        val itemHandler = this.blockEntity.itemHandler

        this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 42))
        this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 42))
        this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 54))

        addDataSlots(containerData)
    }

    override fun getPercentDone(): Float {
        return if (maxProgress == 0) {
            0.0f
        } else {
            progress.toFloat() / maxProgress.toFloat()
        }
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.DNA_EXTRACTOR.get()
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

    companion object {

        private const val DATA_PROGRESS_INDEX = 0
        private const val DATA_MAX_PROGRESS_INDEX = 1
    }

}