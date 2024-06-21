package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {

    val BLOCK_REGISTRY: DeferredRegister.Blocks = DeferredRegister.createBlocks(GeneticsResequenced.ID)

    val BIOLUMINESCENCE_BLOCK: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("bioluminescence_block", BlockBehaviour.Properties.of())
    val COAL_GENERATOR: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("coal_generator", BlockBehaviour.Properties.of())
    val CELL_ANALYZER: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("cell_analyzer", BlockBehaviour.Properties.of())
    val DNA_EXTRACTOR: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("dna_extractor", BlockBehaviour.Properties.of())
    val DNA_DECRYPTOR: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("dna_decryptor", BlockBehaviour.Properties.of())
    val BLOOD_PURIFIER: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("blood_purifier", BlockBehaviour.Properties.of())
    val PLASMID_INFUSER: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("plasmid_infuser", BlockBehaviour.Properties.of())
    val PLASMID_INJECTOR: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("plasmid_injector", BlockBehaviour.Properties.of())
    val INCUBATOR: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("incubator", BlockBehaviour.Properties.of())
    val ADVANCED_INCUBATOR: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("advanced_incubator", BlockBehaviour.Properties.of())
    val ANTI_FIELD_BLOCK: DeferredBlock<Block> =
        BLOCK_REGISTRY.registerSimpleBlock("anti_field_block", BlockBehaviour.Properties.of())

}