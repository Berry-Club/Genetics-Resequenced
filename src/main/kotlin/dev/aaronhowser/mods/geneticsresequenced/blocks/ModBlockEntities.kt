package dev.aaronhowser.mods.geneticsresequenced.blocks

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_extractor.DnaExtractorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.cell_analyzer.CellAnalyzerBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.coal_generator.CoalGeneratorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor.DnaDecryptorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_infuser.PlasmidInfuserBlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModBlockEntities {

    val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GeneticsResequenced.ID)

    val CELL_ANALYZER: RegistryObject<BlockEntityType<CellAnalyzerBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("cell_analyzer") {
            BlockEntityType.Builder.of(
                { pos, state -> CellAnalyzerBlockEntity(pos, state) },
                ModBlocks.CELL_ANALYZER
            ).build(null)
        }

    val COAL_GENERATOR: RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("coal_generator") {
            BlockEntityType.Builder.of(
                { pos, state -> CoalGeneratorBlockEntity(pos, state) },
                ModBlocks.COAL_GENERATOR
            ).build(null)
        }

    val DNA_EXTRACTOR: RegistryObject<BlockEntityType<DnaExtractorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("dna_extractor") {
            BlockEntityType.Builder.of(
                { pos, state -> DnaExtractorBlockEntity(pos, state) },
                ModBlocks.DNA_EXTRACTOR
            ).build(null)
        }

    val DNA_DECRYPTOR: RegistryObject<BlockEntityType<DnaDecryptorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("dna_decryptor") {
            BlockEntityType.Builder.of(
                { pos, state -> DnaDecryptorBlockEntity(pos, state) },
                ModBlocks.DNA_DECRYPTOR
            ).build(null)
        }

    val PLASMID_INFUSER: RegistryObject<BlockEntityType<PlasmidInfuserBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("plasmid_infuser") {
            BlockEntityType.Builder.of(
                { pos, state -> PlasmidInfuserBlockEntity(pos, state) },
                ModBlocks.PLASMID_INFUSER
            ).build(null)
        }

}