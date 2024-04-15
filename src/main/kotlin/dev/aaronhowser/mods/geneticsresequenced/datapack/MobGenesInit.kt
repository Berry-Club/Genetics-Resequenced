package dev.aaronhowser.mods.geneticsresequenced.datapack

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenes
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.registries.DeferredRegister

object MobGenesInit {

    val MOB_GENES_REGISTRY: DeferredRegister<MobGenes> = DeferredRegister.create(
        ResourceLocation(GeneticsResequenced.ID, "mob_genes"),
        GeneticsResequenced.ID
    )

}