package dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.items.SlotItemHandler


class PlasmidInjectorMenu(
    id: Int,
    inventory: Inventory,
    blockEntity: PlasmidInjectorBlockEntity,
    private val containerData: ContainerData
) : MachineMenu(
    ModMenuTypes.PLASMID_INJECTOR.get(),
    blockEntity,
    id,
    inventory
) {

    constructor(id: Int, inventory: Inventory, extraData: FriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level().getBlockEntity(extraData.readBlockPos()) as PlasmidInjectorBlockEntity,
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


    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.PLASMID_INJECTOR.get()
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
        val progressArrowSize = ScreenTextures.Elements.ArrowRight.Dimensions.WIDTH

        return if (maxProgress == 0 || progress == 0) {
            0
        } else {
            progress * progressArrowSize / maxProgress
        }
    }

    companion object {
        fun showTooltip(event: ItemTooltipEvent) {
            val hoverStack = event.itemStack
            if (hoverStack.item != ModItems.SYRINGE.get()) return

            if (SyringeItem.isContaminated(hoverStack)) {
                event.toolTip.add(
                    2,
                    ModLanguageProvider.Tooltips.INFUSER_CONTAMINATED
                        .toComponent()
                        .withColor(ChatFormatting.RED)
                )
            }
        }

        private const val DATA_PROGRESS_INDEX = 0
        private const val DATA_MAX_PROGRESS_INDEX = 1
    }

}