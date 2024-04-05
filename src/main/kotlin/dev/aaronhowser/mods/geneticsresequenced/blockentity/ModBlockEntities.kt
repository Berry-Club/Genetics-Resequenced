package dev.aaronhowser.mods.geneticsresequenced.blockentity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.block.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import thedarkcolour.kotlinforforge.forge.registerObject


object ModBlockEntities {

    val REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GeneticsResequenced.ID)

    val BIOLUMINESCENCE: RegistryObject<BlockEntityType<BioluminescenceBlockEntity>> = REGISTRY.register(
        "biolumen"
    ) {
        BlockEntityType.Builder.of(
            { pos: BlockPos, state: BlockState -> BioluminescenceBlockEntity(pos, state) },
            ModBlocks.BIOLUMINESCENCE
        ).build(null)
    }

}