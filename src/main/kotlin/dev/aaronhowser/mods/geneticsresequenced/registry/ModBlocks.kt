package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.AntiFieldBlock
import dev.aaronhowser.mods.geneticsresequenced.block.BioluminescenceBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.blood_purifier.BloodPurifierBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.cell_analyzer.CellAnalyzerBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator.CoalGeneratorBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_decryptor.DnaDecryptorBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor.DnaExtractorBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorBlock
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {


    val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(GeneticsResequenced.ID)

    val BIOLUMINESCENCE_BLOCK: DeferredBlock<BioluminescenceBlock> =
        registerBlock("bioluminescence") { BioluminescenceBlock() }
    val ANTI_FIELD_BLOCK: DeferredBlock<AntiFieldBlock> = registerBlock("anti_field_block") { AntiFieldBlock() }

    val COAL_GENERATOR: DeferredBlock<CoalGeneratorBlock> = registerBlock("coal_generator") { CoalGeneratorBlock() }
    val CELL_ANALYZER: DeferredBlock<CellAnalyzerBlock> = registerBlock("cell_analyzer") { CellAnalyzerBlock() }
    val DNA_EXTRACTOR: DeferredBlock<Block> = registerBlock("dna_extractor") { DnaExtractorBlock() }
    val DNA_DECRYPTOR: DeferredBlock<DnaDecryptorBlock> = registerBlock("dna_decryptor") { DnaDecryptorBlock() }
    val BLOOD_PURIFIER: DeferredBlock<BloodPurifierBlock> = registerBlock("blood_purifier") { BloodPurifierBlock() }
    val PLASMID_INFUSER: DeferredBlock<Block> = registerBlock("plasmid_infuser")
    val PLASMID_INJECTOR: DeferredBlock<Block> = registerBlock("plasmid_injector")
    val INCUBATOR: DeferredBlock<IncubatorBlock> = registerBlock("incubator") { IncubatorBlock() }
    val ADVANCED_INCUBATOR: DeferredBlock<Block> = registerBlock("advanced_incubator") { AdvancedIncubatorBlock() }

    private fun <T : Block> registerBlock(
        name: String,
        supplier: () -> T = { Block(BlockBehaviour.Properties.of()) as T }
    ): DeferredBlock<T> {
        val block = BLOCK_REGISTRY.register(name, supplier)

        ModItems.ITEM_REGISTRY.registerSimpleBlockItem(name, block)

        return block
    }

}