package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GeneContainer
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

    val ATTACHMENT_TYPES: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, GeneticsResequenced.ID)

    val GENE_CONTAINER: DeferredHolder<AttachmentType<*>, AttachmentType<GeneContainer>> =
        ATTACHMENT_TYPES.register("genes", Supplier {
            AttachmentType
                .builder(Supplier { GeneContainer() })
                .serialize(GeneContainer.CODEC)
                .build()
        })

}