package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, GeneticsResequenced.ID, exFileHelper) {

    override fun registerStatesAndModels() {
        antiFieldBlock()
    }

    private fun antiFieldBlock() {
        val block = ModBlocks.ANTI_FIELD_BLOCK.get()

        getVariantBuilder(block)
            .forAllStates { state ->
                val disabled = state.getValue(AntiFieldBlock.DISABLED)
                val variantName = if (disabled) "disabled" else "enabled"

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models().cubeAll(
                            "anti_field_block_${variantName}",
                            OtherUtil.modResource("block/machine_${variantName}")
                        )
                    ).build()
            }



    }

}