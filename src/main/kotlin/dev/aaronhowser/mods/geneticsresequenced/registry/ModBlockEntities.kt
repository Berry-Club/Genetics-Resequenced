package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.machine.blood_purifier.BloodPurifierBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machine.cell_analyzer.CellAnalyzerBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machine.coal_generator.CoalGeneratorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_decryptor.DnaDecryptorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machine.dna_extractor.DnaExtractorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator.IncubatorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlockEntities {

    val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, GeneticsResequenced.ID)

    val COAL_GENERATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<CoalGeneratorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("coal_generator", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> CoalGeneratorBlockEntity(pos, state) },
                ModBlocks.COAL_GENERATOR.get()
            ).build(null)
        })

    val CELL_ANALYZER: DeferredHolder<BlockEntityType<*>, BlockEntityType<CellAnalyzerBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("cell_analyzer", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> CellAnalyzerBlockEntity(pos, state) },
                ModBlocks.CELL_ANALYZER.get()
            ).build(null)
        })

    val DNA_EXTRACTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DnaExtractorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("dna_extractor", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> DnaExtractorBlockEntity(pos, state) },
                ModBlocks.DNA_EXTRACTOR.get()
            ).build(null)
        })

    val DNA_DECRYPTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<DnaDecryptorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("dna_decryptor", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> DnaDecryptorBlockEntity(pos, state) },
                ModBlocks.DNA_DECRYPTOR.get()
            ).build(null)
        })

    val BLOOD_PURIFIER: DeferredHolder<BlockEntityType<*>, BlockEntityType<BloodPurifierBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("blood_purifier", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> BloodPurifierBlockEntity(pos, state) },
                ModBlocks.BLOOD_PURIFIER.get()
            ).build(null)
        })

    val INCUBATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<IncubatorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("incubator", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> IncubatorBlockEntity(pos, state) },
                ModBlocks.INCUBATOR.get()
            ).build(null)
        })

    val ADVANCED_INCUBATOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<AdvancedIncubatorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("advanced_incubator", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> AdvancedIncubatorBlockEntity(pos, state) },
                ModBlocks.ADVANCED_INCUBATOR.get()
            ).build(null)
        })

}