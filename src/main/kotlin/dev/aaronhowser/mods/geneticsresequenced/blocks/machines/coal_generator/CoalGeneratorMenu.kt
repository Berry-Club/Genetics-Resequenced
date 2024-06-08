package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlocks
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
            val fuelPer = ForgeHooks.getBurnTime(itemStack, RecipeType.SMELTING)
            if (fuelPer <= 0) return

            val feProducedPer = CoalGeneratorBlockEntity.energyPerTick * fuelPer
            val feStringPer = NumberFormat.getNumberInstance().format(feProducedPer)

            event.toolTip.add(
                1, Component.literal("$feStringPer FE").withColor(ChatFormatting.GRAY)
            )

            val amount = itemStack.count
            if (amount > 1) {
                val feProducedTotal = feProducedPer * amount
                val feStringTotal = NumberFormat.getNumberInstance().format(feProducedTotal)

                event.toolTip.add(
                    2, Component.translatable(
                        "tooltip.geneticsresequenced.coal_generator.total_fe",
                        feStringTotal
                    ).withColor(ChatFormatting.GRAY)
                )
            }

        }
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.COAL_GENERATOR.get()
        )
    }
}