package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registries.ModMenuTypes
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
import net.minecraftforge.common.capabilities.ForgeCapabilities
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.items.SlotItemHandler

class PlasmidInfuserMenu(
    id: Int,
    inventory: Inventory,
    blockEntity: PlasmidInfuserBlockEntity,
    private val containerData: ContainerData
) : MachineMenu(
    ModMenuTypes.PLASMID_INFUSER.get(),
    blockEntity,
    id,
    inventory
) {

    constructor(id: Int, inventory: Inventory, extraData: FriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level.getBlockEntity(extraData.readBlockPos()) as PlasmidInfuserBlockEntity,
                SimpleContainerData(CraftingMachineBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, CraftingMachineBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent { itemHandler ->
            this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.INPUT_SLOT_INDEX, 63, 36))
            this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.OUTPUT_SLOT_INDEX, 110, 36))
            this.addSlot(SlotItemHandler(itemHandler, CraftingMachineBlockEntity.OVERCLOCK_SLOT_INDEX, 26, 47))
        }

        addDataSlots(containerData)
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.PLASMID_INFUSER.get()
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
        val progressArrowSize = PlasmidInfuserScreen.ARROW_WIDTH

        return if (maxProgress == 0 || progress == 0) {
            0
        } else {
            progress * progressArrowSize / maxProgress
        }
    }

    companion object {

        fun showTooltip(event: ItemTooltipEvent) {
            val hoverStack = event.itemStack
            if (!hoverStack.`is`(ModItems.DNA_HELIX.get())) return

            val hoverGene = hoverStack.getGene()

            val slots = event.entity?.containerMenu?.slots ?: return
            val plasmidSlotId = 37

            val outputItem = slots.getOrNull(plasmidSlotId)?.item ?: return
            val outputGene = outputItem.getGene() ?: return

            val component =
                when (hoverGene) {
                    null -> {
                        Component.translatable("tooltip.geneticsresequenced.plasmid_infuser.basic_gene")
                    }

                    outputGene -> {
                        Component.translatable("tooltip.geneticsresequenced.plasmid_infuser.matching_gene")
                    }

                    else -> {
                        Component.translatable("tooltip.geneticsresequenced.plasmid_infuser.different_gene")
                    }
                }.withColor(ChatFormatting.GRAY)

            event.toolTip.add(2, component)
        }

        private const val DATA_PROGRESS_INDEX = 0
        private const val DATA_MAX_PROGRESS_INDEX = 1
    }


}