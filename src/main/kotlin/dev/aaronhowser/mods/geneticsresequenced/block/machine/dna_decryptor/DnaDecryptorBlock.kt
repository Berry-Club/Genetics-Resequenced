package dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_decryptor

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.block.base.CraftingMachineBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlockEntities
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class DnaDecryptorBlock(
    properties: Properties = defaultProperties
) : CraftingMachineBlock(
    properties,
    DnaDecryptorBlockEntity::class.java
) {

    override fun codec(): MapCodec<DnaDecryptorBlock> {
        return simpleCodec(::DnaDecryptorBlock)
    }

    override fun <T : BlockEntity> getTicker(
        pLevel: Level,
        pState: BlockState,
        pBlockEntityType: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return BaseEntityBlock.createTickerHelper(
            pBlockEntityType,
            ModBlockEntities.DNA_DECRYPTOR.get(),
            DnaDecryptorBlockEntity::tick
        )
    }

}