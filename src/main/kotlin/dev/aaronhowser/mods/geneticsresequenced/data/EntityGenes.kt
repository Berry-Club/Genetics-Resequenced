package dev.aaronhowser.mods.geneticsresequenced.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.entity.predicate.EntityPredicate
import dev.aaronhowser.mods.aaron.entity.predicate.snapshot.EntitySnapshot
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller

class EntityGenes : SimpleJsonResourceReloadListener(
	GsonBuilder().setPrettyPrinting().create(),
	DIRECTORY
) {

	data class EntityGenesData(
		val entityPredicate: EntityPredicate,
		val geneWeights: Map<ResourceKey<Gene>, Int>
	) {
		companion object {
			val CODEC: Codec<EntityGenesData> = RecordCodecBuilder.create { instance ->
				instance.group(
					EntityPredicate.CODEC
						.fieldOf("entity_predicate")
						.forGetter(EntityGenesData::entityPredicate),
					Codec.unboundedMap(
						ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY),
						Codec.INT
					)
						.fieldOf("gene_weights")
						.forGetter(EntityGenesData::geneWeights)
				).apply(instance, ::EntityGenesData)
			}
		}
	}

	override fun apply(
		pObject: MutableMap<ResourceLocation, JsonElement>,
		pResourceManager: ResourceManager,
		pProfiler: ProfilerFiller
	) {
		ENTITY_GENES.clear()

		for ((key, value) in pObject) {
			println("Loading entity genes data for $key")

			try {
				val entityGenesData: EntityGenesData = EntityGenesData.CODEC
					.decode(JsonOps.INSTANCE, value)
					.getOrThrow { error("Failed to decode entity genes for $key") }
					.first

				ENTITY_GENES.add(entityGenesData)
			} catch (e: Exception) {
				e.printStackTrace()
			}
		}
	}

	companion object {
		const val DIRECTORY = GeneticsResequenced.ID + "/entity_genes"

		private val ENTITY_GENES: MutableList<EntityGenesData> = mutableListOf()
		fun getEntityGenesData(): List<EntityGenesData> = ENTITY_GENES.toList()

		fun getGeneResourceKeyWeights(entitySnapshot: EntitySnapshot): Map<ResourceKey<Gene>, Int> {
			return ENTITY_GENES
				.asSequence()
				.filter { (predicate, _) -> predicate.test(entitySnapshot) } // Get just the data that matches the entity
				.flatMap { (_, geneWeights) -> geneWeights.entries } // Just get their gene weights
				.groupingBy { it.key }
				.fold(0) { acc, entry -> acc + entry.value } // Sum up weights for the same gene
		}

		fun getGeneHolderWeights(entitySnapshot: EntitySnapshot, registries: HolderLookup.Provider): Map<Holder<Gene>, Int> {
			val geneWeights = getGeneResourceKeyWeights(entitySnapshot)

			return geneWeights.map { (resourceKey, weight) ->
				resourceKey.getHolderOrThrow(registries) to weight
			}.toMap()
		}

	}

}