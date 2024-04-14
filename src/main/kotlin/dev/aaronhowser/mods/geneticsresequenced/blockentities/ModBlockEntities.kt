package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModBlockEntities {

    val Block_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GeneticsResequenced.ID)

    val CELL_ANALYZER: RegistryObject<BlockEntityType<CellAnalyzerBlockEntity>> =
        Block_ENTITY_REGISTRY.register("cell_analyzer") {
            BlockEntityType.Builder.of(
                { pos, state -> CellAnalyzerBlockEntity(pos, state) },
                ModBlocks.CELL_ANALYZER
            ).build(null)
        }

    val COAL_GENERATOR: RegistryObject<BlockEntityType<CoalGeneratorBlockEntity>> =
        Block_ENTITY_REGISTRY.register("coal_generator") {
            BlockEntityType.Builder.of(
                { pos, state -> CoalGeneratorBlockEntity(pos, state) },
                ModBlocks.COAL_GENERATOR
            ).build(null)
        }

    val DNA_DECRYPTOR: RegistryObject<BlockEntityType<DnaDecryptorBlockEntity>> =
        Block_ENTITY_REGISTRY.register("dna_decryptor") {
            BlockEntityType.Builder.of(
                { pos, state -> DnaDecryptorBlockEntity(pos, state) },
                ModBlocks.DNA_DECRYPTOR
            ).build(null)
        }

}