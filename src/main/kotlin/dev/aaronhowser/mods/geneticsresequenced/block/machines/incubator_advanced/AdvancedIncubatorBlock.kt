package dev.aaronhowser.mods.geneticsresequenced.block.machines.incubator_advanced

import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class AdvancedIncubatorBlock(
    val properties: Properties = Properties.of().sound(SoundType.METAL)
) : CraftingMachineBlock(
    properties,
    AdvancedIncubatorBlockEntity::class.java
) {

    override fun <T : BlockEntity> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BaseEntityBlock.createTickerHelper(
            pBlockEntityType,
            ModBlockEntities.ADVANCED_INCUBATOR.get(),
            AdvancedIncubatorBlockEntity::tick
        )
    }

}