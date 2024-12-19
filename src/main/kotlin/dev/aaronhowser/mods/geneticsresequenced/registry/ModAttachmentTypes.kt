package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

    val ATTACHMENT_TYPES_REGISTRY: DeferredRegister<AttachmentType<*>> =
        DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, GeneticsResequenced.ID)

    val GENE_CONTAINER: DeferredHolder<AttachmentType<*>, AttachmentType<GenesData>> =
        ATTACHMENT_TYPES_REGISTRY.register("genes", Supplier {
            AttachmentType
                .builder(::GenesData)
                .serialize(GenesData.CODEC)
                .copyOnDeath()
                .build()
        })

    val KEPT_INVENTORY: DeferredHolder<AttachmentType<*>, AttachmentType<KeptInventory>> =
        ATTACHMENT_TYPES_REGISTRY.register("kept_inventory", Supplier {
            AttachmentType
                .builder(::KeptInventory)
                .serialize(KeptInventory.CODEC)
                .copyOnDeath()
                .build()
        })

}