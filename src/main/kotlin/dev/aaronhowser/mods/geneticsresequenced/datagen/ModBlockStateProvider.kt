package dev.aaronhowser.mods.geneticsresequenced.datagen

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
    dataGenerator: DataGenerator,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(
    dataGenerator,
    GeneticsResequenced.ID,
    exFileHelper
) {
    override fun registerStatesAndModels() {
//        blockWithItem(ModBlocks.ANTI_FIELD_BLOCK.get())
    }

    private fun blockWithItem(block: Block) {
        simpleBlock(block)
        simpleBlockItem(block, cubeAll(block))
    }

}