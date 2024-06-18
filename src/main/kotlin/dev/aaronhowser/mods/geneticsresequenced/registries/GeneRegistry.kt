package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryManager

@Suppress("MemberVisibilityCanBePrivate")
object GeneRegistry {

    val GENES_KEY: ResourceKey<Registry<Gene>> = ResourceKey.createRegistryKey(OtherUtil.modResource("genes"))
    val GENES: IForgeRegistry<Gene> = RegistryManager.ACTIVE.getRegistry(GENES_KEY)

    val GENES_REGISTRY: DeferredRegister<Gene> = DeferredRegister.create(GENES, GeneticsResequenced.ID)

}