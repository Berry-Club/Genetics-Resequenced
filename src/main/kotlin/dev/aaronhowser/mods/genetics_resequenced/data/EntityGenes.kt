package dev.aaronhowser.mods.genetics_resequenced.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.event.custom.ModifyEntityGenesEvent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import thedarkcolour.kotlinforforge.neoforge.forge.FORGE_BUS
import kotlin.jvm.optionals.getOrNull

data class EntityGenes(
	val entity: ResourceKey<EntityType<*>>,
	val geneWeights: Map<ResourceKey<Gene>, Int>
) {

	companion object {
		val REGISTRY_KEY: ResourceKey<Registry<EntityGenes>> =
			ResourceKey.createRegistryKey(
				Identifier.fromNamespaceAndPath(
					GeneticsResequenced.MOD_ID,
					"entity_genes"
				)
			)

		val CODEC: Codec<EntityGenes> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceKey.codec(Registries.ENTITY_TYPE)
					.fieldOf("entity")
					.forGetter(EntityGenes::entity),
				Codec.unboundedMap(
					ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY),
					Codec.INT
				)
					.fieldOf("gene_weights")
					.forGetter(EntityGenes::geneWeights)
			).apply(instance, ::EntityGenes)
		}

		fun getGeneHolderWeights(
			entityType: EntityType<*>,
			registries: HolderLookup.Provider
		): Map<Holder<Gene>, Int> {
			val entityRk = entityType.builtInRegistryHolder().key ?: return emptyMap()

			val registry = registries.lookupOrThrow(REGISTRY_KEY)

			val resultRks = mutableMapOf<ResourceKey<Gene>, Int>()

			for (egHolder in registry.listElements()) {
				val eg = egHolder.value()
				if (entityRk != eg.entity) continue

				for ((geneRk, weight) in eg.geneWeights) {
					val existingWeight = resultRks.getOrDefault(geneRk, 0)
					resultRks[geneRk] = existingWeight + weight
				}
			}

			val event = ModifyEntityGenesEvent(entityRk, resultRks)
			FORGE_BUS.post(event)

			val geneRegistry = registries.lookupOrThrow(ModGenes.GENE_REGISTRY_KEY)
			val result = mutableMapOf<Holder<Gene>, Int>()

			for ((geneRk, weight) in resultRks) {
				val geneHolder = geneRegistry.get(geneRk).getOrNull() ?: continue
				result[geneHolder] = weight
			}

			if (result.isEmpty()) {
				val basic = ModGenes.BASIC.getHolderOrThrow(registries)
				result[basic] = 1
			}

			return result
		}

		fun getAllWeights(registries: HolderLookup.Provider): Map<EntityType<*>, Map<Holder<Gene>, Int>> {
			return BuiltInRegistries.ENTITY_TYPE.asSequence()
				.associateWith { entityType -> getGeneHolderWeights(entityType, registries) }
		}

	}

}