package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.block.base.menu.MachineMenu
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorBlockEntity.Companion.CHORUS_SLOT_INDEX
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModMenuTypes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import net.minecraft.ChatFormatting
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.chat.CommonComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.SimpleContainerData
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.items.SlotItemHandler
import kotlin.math.min


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

        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.TOP_SLOT_INDEX, 83, 21))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX, 60, 55))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX, 83, 62))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX, 106, 55))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.CHORUS_SLOT_INDEX, 141, 38))
        this.addSlot(SlotItemHandler(itemHandler, AdvancedIncubatorBlockEntity.OVERCLOCKER_SLOT_INDEX, 141, 60))

        addDataSlots(containerData)
    }

    override fun stillValid(pPlayer: Player): Boolean {
        return stillValid(
            ContainerLevelAccess.create(level, blockEntity.blockPos),
            pPlayer,
            ModBlocks.ADVANCED_INCUBATOR.get()
        )
    }

    private var ticksRemaining: Int
        get() = containerData.get(AdvancedIncubatorBlockEntity.REMAINING_TICKS_INDEX)
        set(value) {
            containerData.set(AdvancedIncubatorBlockEntity.REMAINING_TICKS_INDEX, value)
        }

    var isHighTemperature: Boolean
        get() = containerData.get(AdvancedIncubatorBlockEntity.IS_HIGH_TEMPERATURE_INDEX) == 1
        private set(value) {
            containerData.set(AdvancedIncubatorBlockEntity.IS_HIGH_TEMPERATURE_INDEX, if (value) 1 else 0)
        }

    val isCrafting
        get() = ticksRemaining > 0

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
        fun showChanceTooltip(event: ItemTooltipEvent) {
            val potionStack = event.itemStack

            val potion = OtherUtil.getPotion(potionStack) ?: return
            if (
                potion != ModPotions.CELL_GROWTH
                && potion != ModPotions.MUTATION
            ) return

            val player = event.entity ?: return
            val menu = player.containerMenu as? AdvancedIncubatorMenu ?: return
            val blockEntity = menu.blockEntity as? AdvancedIncubatorBlockEntity ?: return

            val topStack = blockEntity.itemHandler.getStackInSlot(AdvancedIncubatorBlockEntity.TOP_SLOT_INDEX)

            val recipe = AdvancedIncubatorBlockEntity.getGmoRecipe(topStack, potionStack) ?: return

            val chanceDecreasePerOverclocker = ServerConfig.incubatorOverclockerChanceDecrease.get().toFloat()
            val chanceIncreasePerChorus = ServerConfig.incubatorChorusFruitChanceIncrease.get().toFloat()

            val baseChance = recipe.geneChance

            val amountOverclockers = blockEntity.amountOfOverclockers
            val overclockerChanceFactor =
                1 - amountOverclockers * chanceDecreasePerOverclocker
            val reducedChance = (baseChance * overclockerChanceFactor).coerceIn(0f, 1f)

            val chorusRequiredForMaxChance = Mth.ceil((1f - reducedChance) / chanceIncreasePerChorus)
            val chorusAvailable = blockEntity.itemHandler.getStackInSlot(CHORUS_SLOT_INDEX).count
            val chorusUsed = min(chorusRequiredForMaxChance, chorusAvailable)

            val chorusBoost = chorusUsed * chanceIncreasePerChorus
            val finalChance = reducedChance + chorusBoost

            var index = event.toolTip.size

            event.toolTip.add(
                index++,
                CommonComponents.EMPTY
            )

            event.toolTip.add(
                index++,
                ModLanguageProvider.Tooltips.GMO_BASE_CHANCE
                    .toComponent(
                        Gene.getNameComponent(recipe.idealGeneRk),
                        (baseChance * 100).toInt()
                    )
                    .withColor(ChatFormatting.GRAY)
            )

            if (amountOverclockers != 0) {
                event.toolTip.add(
                    index++,
                    ModLanguageProvider.Tooltips.GMO_OVERCLOCKER_CHANCE
                        .toComponent(
                            amountOverclockers,
                            (reducedChance * 100).toInt()
                        )
                        .withColor(ChatFormatting.GRAY)
                )
            }

            if (chorusUsed != 0) {
                event.toolTip.add(
                    index,
                    ModLanguageProvider.Tooltips.GMO_CHORUS_CHANCE
                        .toComponent(
                            chorusUsed,
                            (finalChance * 100).toInt()
                        )
                        .withColor(ChatFormatting.GRAY)
                )
            }
        }
    }

}