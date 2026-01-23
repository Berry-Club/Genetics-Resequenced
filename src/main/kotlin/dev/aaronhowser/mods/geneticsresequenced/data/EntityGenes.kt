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
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.EntityType

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

	private fun addGeneWeights(
		entityRk: ResourceKey<EntityType<*>>,
		newGeneWeights: Map<ResourceKey<Gene>, Int>
	) {
		val entityType = BuiltInRegistries.ENTITY_TYPE.get(entityRk)!!
		val currentGenes = ENTITY_GENE_MAP[entityType]?.toMutableMap() ?: mutableMapOf()

		for ((gene, weight) in newGeneWeights) {
			currentGenes[gene] = currentGenes[gene]?.plus(weight) ?: weight
		}

		ENTITY_GENE_MAP[entityType] = currentGenes
	}

	override fun apply(
		pObject: MutableMap<ResourceLocation, JsonElement>,
		pResourceManager: ResourceManager,
		pProfiler: ProfilerFiller
	) {
		ENTITY_GENE_MAP.clear()

		for ((key: ResourceLocation, value: JsonElement) in pObject) {
			try {
				val entityGenesData: EntityGenesData = EntityGenesData.CODEC.decode(
					JsonOps.INSTANCE,
					value
				).getOrThrow {
					IllegalArgumentException("Failed to decode entity genes for $key")
				}.first

				val entityName = entityGenesData.entityPredicate.location().path
				val fileName = key.toString().split(":")[1]
				if (entityName != fileName) {
					GeneticsResequenced.LOGGER.warn("Gene-mob data for $key has the entity $entityName instead of $fileName. This may be a mistake.")
				}

				addGeneWeights(
					entityGenesData.entityPredicate,
					entityGenesData.geneWeights
				)

				GeneticsResequenced.LOGGER.debug("Loaded gene-mob data for ${entityGenesData.entityPredicate.location()}, with ${entityGenesData.geneWeights.size} genes")
			} catch (e: Exception) {
				e.printStackTrace()
			}

		}
	}

	companion object {
		const val DIRECTORY = GeneticsResequenced.ID + "/entity_genes"


		private val ENTITY_GENE_MAP: MutableMap<EntityPredicate, Map<ResourceKey<Gene>, Int>> = mutableMapOf()

		fun getEntityGeneHolderMap(registries: HolderLookup.Provider): Map<EntityPredicate, Map<Holder.Reference<Gene>, Int>> {
			return ENTITY_GENE_MAP.map { (predicate, rkMap) ->
				val holderMap = rkMap.map { (rk, weight) -> rk.getHolderOrThrow(registries) to weight }.toMap()
				predicate to holderMap
			}.toMap()
		}

		fun getGeneResourceKeyWeights(entitySnapshot: EntitySnapshot): Map<ResourceKey<Gene>, Int> {
			return ENTITY_GENE_MAP
				.asSequence()
				.filter { (predicate, _) -> predicate.test(entitySnapshot) }
				.flatMap { it.value.asSequence() }
				.groupingBy { it.key }
				.fold(0) { acc, (_, weight) -> acc + weight }
		}

		fun getGeneHolderWeights(entitySnapshot: EntitySnapshot, registries: HolderLookup.Provider): Map<Holder<Gene>, Int> {
			val geneWeights = getGeneResourceKeyWeights(entitySnapshot)

			return geneWeights.map { (resourceKey, weight) ->
				resourceKey.getHolderOrThrow(registries) to weight
			}.toMap()
		}

	}

}