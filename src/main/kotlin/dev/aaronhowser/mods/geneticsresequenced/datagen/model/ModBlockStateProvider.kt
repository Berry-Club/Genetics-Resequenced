package dev.aaronhowser.mods.geneticsresequenced.datagen.model

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.block.BioluminescenceBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.registry.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredBlock

class ModBlockStateProvider(
    output: PackOutput,
    private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, GeneticsResequenced.ID, existingFileHelper) {

    override fun registerStatesAndModels() {
        antiFieldBlock()
        bioluminescence()

        coalGenerator()
        frontFacingBlock(ModBlocks.CELL_ANALYZER, "cell_analyzer", "block/cell_analyzer_front")
        frontFacingBlock(ModBlocks.DNA_EXTRACTOR, "dna_extractor", "block/dna_extractor_front")
        frontFacingBlock(ModBlocks.DNA_DECRYPTOR, "dna_decryptor", "block/dna_decryptor_front")
        frontFacingBlock(ModBlocks.BLOOD_PURIFIER, "blood_purifier", "block/blood_purifier_front")
        frontFacingBlock(ModBlocks.PLASMID_INFUSER, "plasmid_infuser", "block/plasmid_infuser_front")
        frontFacingBlock(ModBlocks.PLASMID_INJECTOR, "plasmid_injector", "block/plasmid_injector_front")
        frontFacingBlock(ModBlocks.INCUBATOR, "incubator", "block/incubator_front")
        frontFacingBlock(ModBlocks.ADVANCED_INCUBATOR, "advanced_incubator", "block/incubator_front")

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

        val lightLevel = BioluminescenceBlock.LIGHT_LEVEL

        simpleBlockItem(
            deferredBioluminescence.get(),
            ItemModelBuilder(
                mcLoc("item/light_${lightLevel}"),
                existingFileHelper
            )
        )
    }

    private fun frontFacingBlock(
        deferredBlock: DeferredBlock<out Block>,
        name: String,
        frontTexture: String
    ) {

        val top = "block/machine_top"
        val bottom = "block/machine_bottom"
        val side = "block/machine_side"

        getVariantBuilder(deferredBlock.get())
            .forAllStates { state ->
                val facing = state.getValue(HorizontalDirectionalBlock.FACING)

                val north = if (facing == Direction.NORTH) frontTexture else side
                val south = if (facing == Direction.SOUTH) frontTexture else side
                val east = if (facing == Direction.EAST) frontTexture else side
                val west = if (facing == Direction.WEST) frontTexture else side

                val modelName = name + when (facing) {
                    Direction.NORTH -> "_north"
                    Direction.EAST -> "_east"
                    Direction.SOUTH -> "_south"
                    Direction.WEST -> "_west"
                    else -> throw IllegalStateException("Invalid facing direction")
                }

                ConfiguredModel.builder().modelFile(
                    models().cube(
                        modelName,
                        modLoc(bottom),
                        modLoc(top),
                        modLoc(north),
                        modLoc(south),
                        modLoc(east),
                        modLoc(west),
                    ).texture("particle", modLoc(top))
                ).build()
            }

        simpleBlockItem(
            deferredBlock.get(),
            ItemModelBuilder(
                modLoc("block/${name}_east"),
                existingFileHelper
            )
        )
    }

    private fun coalGenerator() {
        val deferredBlock = ModBlocks.COAL_GENERATOR

        val top = "block/machine_top"
        val bottom = "block/machine_bottom"
        val side = "block/machine_side"

        getVariantBuilder(deferredBlock.get())
            .forAllStates { state ->

                val burning = state.getValue(CoalGeneratorBlock.BURNING)
                val facing = state.getValue(HorizontalDirectionalBlock.FACING)

                val burningString = if (burning) "on" else "off"

                val rotationY = when (facing) {
                    Direction.NORTH -> 0
                    Direction.EAST -> 90
                    Direction.SOUTH -> 180
                    Direction.WEST -> 270
                    else -> throw IllegalStateException("Invalid facing direction")
                }

                val modelName = "coal_generator_$burningString"

                val frontTexture = if (burning) "block/coal_generator_front_on" else "block/coal_generator_front_off"

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models().cube(
                            modelName,
                            modLoc(bottom),
                            modLoc(top),
                            modLoc(frontTexture),
                            modLoc(side),
                            modLoc(side),
                            modLoc(side),
                        ).texture("particle", modLoc(top))
                    )
                    .rotationY(rotationY)
                    .build()
            }

        simpleBlockItem(
            deferredBlock.get(),
            ItemModelBuilder(
                modLoc("block/coal_generator_off"),
                existingFileHelper
            )
        )
    }

}