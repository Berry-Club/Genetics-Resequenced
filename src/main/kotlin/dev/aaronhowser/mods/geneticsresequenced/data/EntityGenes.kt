package dev.aaronhowser.mods.geneticsresequenced.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType

object EntityGenes {

	val REGISTRY_KEY: ResourceKey<Registry<EntityGenesData>> =
		ResourceKey.createRegistryKey(
			ResourceLocation.fromNamespaceAndPath(
				GeneticsResequenced.ID,
				"entity_genes"
			)
		)

	data class EntityGenesData(
		val geneWeights: Map<ResourceKey<Gene>, Int>
	) {
		companion object {

			val CODEC: Codec<EntityGenesData> =
				RecordCodecBuilder.create { instance ->
					instance.group(
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

	fun getGeneResourceKeyWeights(
		entityType: EntityType<*>,
		registries: HolderLookup.Provider
	): Map<ResourceKey<Gene>, Int> {
		val entityRk = BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType)
			.orElse(null)
			?: return mapOf(ModGenes.BASIC to 1)

		val registry = registries.lookupOrThrow(REGISTRY_KEY)
		val entityGenesRk = ResourceKey.create(REGISTRY_KEY, entityRk.location())

		val data = registry
			.get(entityGenesRk)
			.orElse(null)
			?: return mapOf(ModGenes.BASIC to 1)

		return data.value().geneWeights
	}

	fun getGeneHolderWeights(
		entityType: EntityType<*>,
		registries: HolderLookup.Provider
	): Map<Holder<Gene>, Int> {
		return getGeneResourceKeyWeights(entityType, registries)
			.map { (geneKey, weight) ->
				geneKey.getHolderOrThrow(registries) to weight
			}
			.toMap()
	}

}