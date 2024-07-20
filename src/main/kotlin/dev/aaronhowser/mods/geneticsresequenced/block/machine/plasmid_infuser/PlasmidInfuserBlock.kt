package dev.aaronhowser.mods.geneticsresequenced.block.machine.plasmid_infuser

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class PlasmidInfuserBlock(
    properties: Properties = defaultProperties
) : CraftingMachineBlock(
    properties,
    PlasmidInfuserBlockEntity::class.java
) {

    override fun codec(): MapCodec<PlasmidInfuserBlock> {
        return simpleCodec(::PlasmidInfuserBlock)
    }

    override fun <T : BlockEntity> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BaseEntityBlock.createTickerHelper(
            pBlockEntityType,
            ModBlockEntities.PLASMID_INFUSER.get(),
            PlasmidInfuserBlockEntity::tick
        )
    }

}