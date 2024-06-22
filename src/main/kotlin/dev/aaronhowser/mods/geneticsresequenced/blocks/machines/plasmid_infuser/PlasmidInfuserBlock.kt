package dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlockEntities
import dev.aaronhowser.mods.geneticsresequenced.blocks.base.CraftingMachineBlock
import dev.aaronhowser.mods.geneticsresequenced.util.BlockEntityHelper
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material

class PlasmidInfuserBlock : CraftingMachineBlock(
    Properties.of(Material.METAL),
    PlasmidInfuserBlockEntity::class.java
) {

    override fun <T : BlockEntity> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BlockEntityHelper.createTickerHelper(
            pBlockEntityType,
            ModBlockEntities.PLASMID_INFUSER.get(),
            PlasmidInfuserBlockEntity::tick
        )
    }

}