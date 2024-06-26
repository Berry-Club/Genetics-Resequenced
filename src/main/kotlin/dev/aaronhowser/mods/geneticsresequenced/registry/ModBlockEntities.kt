package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.machines.blood_purifier.BloodPurifierBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machines.coal_generator.CoalGeneratorBlockEntity
import dev.aaronhowser.mods.geneticsresequenced.block.machines.cell_analyzer.CellAnalyzerBlockEntity
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

    val BLOOD_PURIFIER: DeferredHolder<BlockEntityType<*>, BlockEntityType<BloodPurifierBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("blood_purifier", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> BloodPurifierBlockEntity(pos, state) },
                ModBlocks.BLOOD_PURIFIER.get()
            ).build(null)
        })

}