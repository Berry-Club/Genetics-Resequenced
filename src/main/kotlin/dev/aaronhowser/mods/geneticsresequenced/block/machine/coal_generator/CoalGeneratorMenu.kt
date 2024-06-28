package dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator

import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.ScreenTextures
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.items.SlotItemHandler
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

    constructor(id: Int, inventory: Inventory, extraData: RegistryFriendlyByteBuf) :
            this(
                id,
                inventory,
                inventory.player.level().getBlockEntity(extraData.readBlockPos()) as CoalGeneratorBlockEntity,
                SimpleContainerData(CoalGeneratorBlockEntity.SIMPLE_CONTAINER_SIZE)
            )

    init {
        checkContainerSize(inventory, CoalGeneratorBlockEntity.SIMPLE_CONTAINER_SIZE)

        addPlayerInventory(inventory)
        addPlayerHotbar(inventory)

        val itemHandler = this.blockEntity.getItemCapability(null)

        this.addSlot(SlotItemHandler(itemHandler, CoalGeneratorBlockEntity.INPUT_SLOT, 52, 34))

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
        val fuelSize = ScreenTextures.Elements.Burn.TEXTURE_SIZE

        return if (maxBurnTime == 0) {
            0
        } else {
            fuelSize - (fuelSize * getPercentDone()).toInt()
        }
    }

    fun getScaledProgressArrow(): Int {
        val progressArrowWidth = ScreenTextures.Elements.ArrowRight.Dimensions.WIDTH

        return if (maxBurnTime == 0) {
            0
        } else {
            (progressArrowWidth.toDouble() * getPercentDone()).toInt()
        }
    }

    companion object {
        fun showFuelTooltip(event: ItemTooltipEvent) {
            val itemStack = event.itemStack
            val fuelPer = itemStack.getBurnTime(RecipeType.SMELTING)
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
                    2,
                    ModLanguageProvider.Tooltips.COAL_GEN_TOTAL_FE
                        .toComponent(feStringTotal)
                        .withColor(ChatFormatting.GRAY)
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