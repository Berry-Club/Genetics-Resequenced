package dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.base.handler.WrappedHandler
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionBrewing
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.ItemStackHandler
import kotlin.math.min
import kotlin.random.Random


class AdvancedIncubatorBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : CraftingMachineBlockEntity(
    ModBlockEntities.ADVANCED_INCUBATOR.get(),
    pPos,
    pBlockState
), MenuProvider {

    override val machineName: String = "advanced_incubator"

    override val energyMaximum: Int = 50_000
    override val energyTransferMaximum: Int = 500

    override fun createMenu(pContainerId: Int, pPlayerInventory: Inventory, pPlayer: Player): AbstractContainerMenu {
        return AdvancedIncubatorMenu(pContainerId, pPlayerInventory, this, this.data)
    }

    override fun getDisplayName(): Component {
        return ModBlocks.ADVANCED_INCUBATOR.get().name
    }

    private val potionBrewing: PotionBrewing?
        get() = level?.potionBrewing()

    override val amountOfItemSlots: Int = 6
    override val itemHandler: ItemStackHandler = object : ItemStackHandler(amountOfItemSlots) {
        override fun onContentsChanged(slot: Int) {
            setChanged()

            if (slot == OVERCLOCKER_SLOT_INDEX) {
                resetBrewTime()
            }
        }

        override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
            return when (slot) {
                TOP_SLOT_INDEX -> potionBrewing?.isIngredient(stack) ?: false

                LEFT_BOTTLE_SLOT_INDEX,
                MIDDLE_BOTTLE_SLOT_INDEX,
                RIGHT_BOTTLE_SLOT_INDEX -> potionBrewing?.isInput(stack) ?: false

                OVERCLOCKER_SLOT_INDEX -> stack.item == ModItems.OVERCLOCKER.get()

                CHORUS_SLOT_INDEX -> stack.item == Items.CHORUS_FRUIT

                else -> false
            }
        }
    }

    private val chorusHandler =
        WrappedHandler(
            itemHandler,
            canExtract = { slot -> slot == OUTPUT_SLOT_INDEX },
            canInsert = { slot, stack -> slot == CHORUS_SLOT_INDEX }
        )

    override val upItemHandler: WrappedHandler = IncubatorBlockEntity.topSlotHandler(itemHandler)
    override val downItemHandler: WrappedHandler = IncubatorBlockEntity.bottomSlotsHandler(itemHandler)
    override val backItemHandler: WrappedHandler = chorusHandler
    override val frontItemHandler: WrappedHandler = chorusHandler
    override val rightItemHandler: WrappedHandler = IncubatorBlockEntity.bottomSlotsHandler(itemHandler)
    override val leftItemHandler: WrappedHandler = IncubatorBlockEntity.bottomSlotsHandler(itemHandler)

    private val data = object : ContainerData {

        private var remainingTicks = 0
        private var isHighTemperature = 0

        override fun get(pIndex: Int): Int {
            return when (pIndex) {
                REMAINING_TICKS_INDEX -> remainingTicks
                IS_HIGH_TEMPERATURE_INDEX -> isHighTemperature
                else -> 0
            }
        }

        override fun set(pIndex: Int, pValue: Int) {
            when (pIndex) {
                REMAINING_TICKS_INDEX -> remainingTicks = pValue
                IS_HIGH_TEMPERATURE_INDEX -> isHighTemperature = pValue
            }
            setChanged()
        }

        override fun getCount(): Int {
            return SIMPLE_CONTAINER_SIZE
        }
    }

    private val ticksRemainingNbtKey = "$machineName.ticksRemaining"
    var ticksRemaining: Int
        get() = data.get(REMAINING_TICKS_INDEX)
        private set(value) {
            data.set(REMAINING_TICKS_INDEX, value)
        }

    private val isHighTemperatureNbtKey = "$machineName.isHighTemperature"
    private var isHighTemperature: Boolean
        get() = data.get(IS_HIGH_TEMPERATURE_INDEX) == 1
        set(value) {
            data.set(IS_HIGH_TEMPERATURE_INDEX, if (value) 1 else 0)
        }

    val isBrewing: Boolean
        get() = ticksRemaining > 0

    override val baseEnergyCostPerTick = 10

    override fun saveAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        pTag.putInt(ticksRemainingNbtKey, ticksRemaining)
        pTag.putBoolean(isHighTemperatureNbtKey, isHighTemperature)

        super.saveAdditional(pTag, pRegistries)
    }

    override fun loadAdditional(pTag: CompoundTag, pRegistries: HolderLookup.Provider) {
        ticksRemaining = pTag.getInt(ticksRemainingNbtKey)
        isHighTemperature = pTag.getBoolean(isHighTemperatureNbtKey)

        super.loadAdditional(pTag, pRegistries)
    }


    override val amountOfOverclockers: Int
        get() = itemHandler.getStackInSlot(OVERCLOCKER_SLOT_INDEX).count

    fun resetBrewTime() {
        ticksRemaining = IncubatorBlockEntity.ticksPerBrew
        subticks = 0
    }

    private var subticks = 0
    override fun tick() {

        if (!isBrewing && hasRecipe()) {
            ticksRemaining = IncubatorBlockEntity.ticksPerBrew
        } else if (!hasRecipe()) {
            ticksRemaining = 0
            return
        }

        if (isHighTemperature) {
            energyStorage.extractEnergy(energyCostPerTick(), false)
            ticksRemaining -= 1 + amountOfOverclockers
        } else {
            val amountOfOverclockers = amountOfOverclockers
            subticks += 1 + amountOfOverclockers

            val subticksOverMax = subticks - lowTempTickFactor
            if (subticksOverMax >= 0) {
                subticks = subticksOverMax
                energyStorage.extractEnergy(energyCostPerTick(), false)
                ticksRemaining -= 1
            }
        }

        if (ticksRemaining <= 0) {
            craftItem()
            return
        }
    }

    override fun craftItem() {
        val topStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)

        val bottleSlots = listOf(
            LEFT_BOTTLE_SLOT_INDEX,
            MIDDLE_BOTTLE_SLOT_INDEX,
            RIGHT_BOTTLE_SLOT_INDEX
        )

        val chanceDecreasePerOverclocker = ServerConfig.incubatorOverclockerChanceDecrease.get().toFloat()
        val chanceIncreasePerChorus = ServerConfig.incubatorChorusFruitChanceIncrease.get().toFloat()

        for (slotIndex in bottleSlots) {
            val bottleStack = itemHandler.getStackInSlot(slotIndex)

            var output = potionBrewing?.mix(topStack, bottleStack) ?: continue

            if (output.item == ModItems.GMO_CELL.get() && !isHighTemperature) {
                val thisRecipe = GmoRecipe.getGmoRecipe(this.level!!, topStack, bottleStack) ?: continue

                // The base chance
                val geneChance = thisRecipe.geneChance

                // Reduce the chance based on the amount of Overclockers (1.0 means no change)
                val overclockerChanceFactor =
                    1 - amountOfOverclockers * chanceDecreasePerOverclocker
                val reducedChance = (geneChance * overclockerChanceFactor).coerceIn(0f, 1f)

                // Increase the chance based on the amount of Chorus Fruit
                val chorusRequiredForMaxChance = Mth.ceil((1f - reducedChance) / chanceIncreasePerChorus)
                val chorusAvailable = itemHandler.getStackInSlot(CHORUS_SLOT_INDEX).count
                val chorusUsed = min(chorusRequiredForMaxChance, chorusAvailable)

                itemHandler.getStackInSlot(CHORUS_SLOT_INDEX).shrink(chorusUsed)

                val chorusBoost = chorusUsed * chanceIncreasePerChorus
                val finalChance = reducedChance + chorusBoost

                val nextFloat = Random.nextFloat()

                if (nextFloat <= finalChance) {
                    output = thisRecipe.getSuccess(this.level!!.registryAccess())
                }
            }

            if (!output.isEmpty) {
                itemHandler.setStackInSlot(slotIndex, output)
            }
        }

        topStack.shrink(1)
        setChanged()
    }

    override fun hasRecipe(): Boolean {
        if (!hasEnoughEnergy()) return false

        val topSlotStack = itemHandler.getStackInSlot(TOP_SLOT_INDEX)

        val brewing = potionBrewing ?: return false

        if (brewing.hasMix(
                itemHandler.getStackInSlot(IncubatorBlockEntity.LEFT_BOTTLE_SLOT_INDEX),
                topSlotStack
            )
        ) return true

        if (brewing.hasMix(
                itemHandler.getStackInSlot(IncubatorBlockEntity.MIDDLE_BOTTLE_SLOT_INDEX),
                topSlotStack
            )
        ) return true

        if (brewing.hasMix(
                itemHandler.getStackInSlot(IncubatorBlockEntity.RIGHT_BOTTLE_SLOT_INDEX),
                topSlotStack
            )
        ) return true

        return false
    }

    companion object {

        fun tick(
            level: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: AdvancedIncubatorBlockEntity
        ) {
            if (level.isClientSide) return
            blockEntity.tick()
        }

        const val SIMPLE_CONTAINER_SIZE = 2
        const val REMAINING_TICKS_INDEX = 0
        const val IS_HIGH_TEMPERATURE_INDEX = 1

        const val TOP_SLOT_INDEX = 0
        const val LEFT_BOTTLE_SLOT_INDEX = 1
        const val MIDDLE_BOTTLE_SLOT_INDEX = 2
        const val RIGHT_BOTTLE_SLOT_INDEX = 3
        const val CHORUS_SLOT_INDEX = 4
        const val OVERCLOCKER_SLOT_INDEX = 5

        val lowTempTickFactor: Int
            get() = ServerConfig.incubatorLowTempTickFactor.get()

    }

}