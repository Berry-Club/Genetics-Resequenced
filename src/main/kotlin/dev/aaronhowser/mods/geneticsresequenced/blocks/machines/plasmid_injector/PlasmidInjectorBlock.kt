package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector

import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlock
import dev.aaronhowser.mods.geneticsresequenced.util.BlockEntityHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

class PlasmidInjectorBlock : CraftingMachineBlock(
    Properties.of(Material.METAL),
    PlasmidInjectorBlockEntity::class.java
) {

    override fun <T : BlockEntity> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BlockEntityHelper.createTickerHelper(
            pBlockEntityType,
            ModBlockEntities.PLASMID_INJECTOR.get(),
            PlasmidInjectorBlockEntity::tick
        )
    }

}