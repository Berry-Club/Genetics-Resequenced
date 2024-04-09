package dev.aaronhowser.mods.geneticsresequenced.blockentities

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModBlockEntities {

    val REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GeneticsResequenced.ID)

    val CELL_ANALYZER: RegistryObject<BlockEntityType<CellAnalyzerBlockEntity>> = REGISTRY.register("cell_analyzer") {
        BlockEntityType.Builder.of(
            { pos, state -> CellAnalyzerBlockEntity(pos, state) },
            ModBlocks.CELL_ANALYZER
        ).build(null)
    }

}