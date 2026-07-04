package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GeneCooldowns
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import dev.aaronhowser.mods.geneticsresequenced.attachment.KeptInventory
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData
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
				.serialize(GenesData.MAP_CODEC)
				.copyOnDeath()
				.build()
		)

	val TEMPORARY_GENES: DeferredHolder<AttachmentType<*>, AttachmentType<TemporaryGenesData>> =
		register(
			"temporary_genes",
			AttachmentType
				.builder(::TemporaryGenesData)
				.serialize(TemporaryGenesData.MAP_CODEC)
				.sync(TemporaryGenesData.STREAM_CODEC)
				.build()
		)

	val KEPT_INVENTORY: DeferredHolder<AttachmentType<*>, AttachmentType<KeptInventory>> =
		register(
			"kept_inventory",
			AttachmentType
				.builder(::KeptInventory)
				.serialize(KeptInventory.MAP_CODEC)
				.copyOnDeath()
				.build()
		)

	val GENE_COOLDOWNS: DeferredHolder<AttachmentType<*>, AttachmentType<GeneCooldowns>> =
		register(
			"gene_cooldowns",
			AttachmentType
				.builder(::GeneCooldowns)
				.serialize(GeneCooldowns.MAP_CODEC)
				.sync(GeneCooldowns.STREAM_CODEC)
				.build()
		)

	private fun <T : Any> register(
		name: String,
		type: AttachmentType<T>
	): DeferredHolder<AttachmentType<*>, AttachmentType<T>> {
		return ATTACHMENT_TYPES_REGISTRY.register(name, Supplier { type })
	}

}
