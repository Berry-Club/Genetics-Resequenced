package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.BioluminescenceBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {

    val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(GeneticsResequenced.ID)

    val BIOLUMINESCENCE_BLOCK: DeferredBlock<Block> = registerBlock("bioluminescence_block") { BioluminescenceBlock() }
    val COAL_GENERATOR: DeferredBlock<Block> = registerSimpleBlock("coal_generator")
    val CELL_ANALYZER: DeferredBlock<Block> = registerSimpleBlock("cell_analyzer")
    val DNA_EXTRACTOR: DeferredBlock<Block> = registerSimpleBlock("dna_extractor")
    val DNA_DECRYPTOR: DeferredBlock<Block> = registerSimpleBlock("dna_decryptor")
    val BLOOD_PURIFIER: DeferredBlock<Block> = registerSimpleBlock("blood_purifier")
    val PLASMID_INFUSER: DeferredBlock<Block> = registerSimpleBlock("plasmid_infuser")
    val PLASMID_INJECTOR: DeferredBlock<Block> = registerSimpleBlock("plasmid_injector")
    val INCUBATOR: DeferredBlock<Block> = registerSimpleBlock("incubator")
    val ADVANCED_INCUBATOR: DeferredBlock<Block> = registerSimpleBlock("advanced_incubator")
    val ANTI_FIELD_BLOCK: DeferredBlock<Block> = registerSimpleBlock("anti_field_block")

    private fun registerBlock(
        name: String,
        supplier: () -> Block
    ): DeferredBlock<Block> {
        val block = BLOCK_REGISTRY.register(name, supplier)

        ModItems.ITEM_REGISTRY.registerSimpleBlockItem(name, block)

        return block
    }

    private fun registerSimpleBlock(
        name: String
    ): DeferredBlock<Block> {
        val block = BLOCK_REGISTRY.registerSimpleBlock(name, BlockBehaviour.Properties.of())

        ModItems.ITEM_REGISTRY.registerSimpleBlockItem(name, block)

        return block
    }

}