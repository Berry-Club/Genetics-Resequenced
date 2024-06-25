package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
    output: PackOutput,
    private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, GeneticsResequenced.ID, existingFileHelper) {

    override fun registerStatesAndModels() {
        antiFieldBlock()
        bioluminescence()
    }

    private fun antiFieldBlock() {
        val deferredAntiFieldBlock = ModBlocks.ANTI_FIELD_BLOCK

        getVariantBuilder(deferredAntiFieldBlock.get())
            .forAllStates { state ->
                val disabled = state.getValue(AntiFieldBlock.DISABLED)
                val modelVariantName = if (disabled) "anti_field_block_disabled" else "anti_field_block_enabled"
                val textureVariantName = if (disabled) "block/machine_bottom" else "block/machine_top"

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models().cubeAll(
                            modelVariantName,
                            modLoc(textureVariantName)
                        )
                    ).build()
            }

        simpleBlockItem(
            deferredAntiFieldBlock.get(),
            ItemModelBuilder(
                modLoc("block/anti_field_block_enabled"),
                existingFileHelper
            )
        )
    }

    private fun bioluminescence() {
        val deferredBioluminescence = ModBlocks.BIOLUMINESCENCE_BLOCK

        getVariantBuilder(deferredBioluminescence.get())
            .forAllStates {
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models().withExistingParent(
                            deferredBioluminescence.id.path,
                            mcLoc("block/air")
                        )
                    )
                    .build()
            }

        simpleBlockItem(
            deferredBioluminescence.get(),
            ItemModelBuilder(
                mcLoc("item/light_10"),
                existingFileHelper
            )
        )

    }

}