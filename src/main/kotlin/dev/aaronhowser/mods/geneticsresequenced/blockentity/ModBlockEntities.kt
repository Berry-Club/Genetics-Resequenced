package dev.aaronhowser.mods.geneticsresequenced.blockentity

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries


object ModBlockEntities {

    val REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GeneticsResequenced.ID)

}