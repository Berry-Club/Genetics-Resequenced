package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData
import dev.aaronhowser.mods.genetics_resequenced.attachment.KeptInventory
import dev.aaronhowser.mods.genetics_resequenced.attachment.TemporaryGenesData
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

	val ATTACHMENT_TYPES_REGISTRY: DeferredRegister<AttachmentType<*>> =
		DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, GeneticsResequenced.MOD_ID)

	val GENE_CONTAINER: DeferredHolder<AttachmentType<*>, AttachmentType<GenesData>> =
		register(
			"genes",
			AttachmentType
				.builder(::GenesData)
				.serialize(GenesData.CODEC)
				.copyOnDeath()
				.build()
		)

	val TEMPORARY_GENES: DeferredHolder<AttachmentType<*>, AttachmentType<TemporaryGenesData>> =
		register(
			"temporary_genes",
			AttachmentType
				.builder(::TemporaryGenesData)
				.serialize(TemporaryGenesData.CODEC)
				.sync(TemporaryGenesData.STREAM_CODEC)
				.build()
		)

	val KEPT_INVENTORY: DeferredHolder<AttachmentType<*>, AttachmentType<KeptInventory>> =
		register(
			"kept_inventory",
			AttachmentType
				.builder(::KeptInventory)
				.serialize(KeptInventory.CODEC)
				.copyOnDeath()
				.build()
		)

	private fun <T> register(name: String, type: AttachmentType<T>): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return ATTACHMENT_TYPES_REGISTRY.register(name, Supplier { type })
	}

}