package dev.aaronhowser.mods.geneticsresequenced.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.event.custom.ModifyEntityGenesEvent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import kotlin.jvm.optionals.getOrNull

data class EntityGenes(
	val entityType: ResourceKey<EntityType<*>>,
	val geneWeights: Map<ResourceKey<Gene>, Int>
) {

	companion object {
		val REGISTRY_KEY: ResourceKey<Registry<EntityGenes>> =
			ResourceKey.createRegistryKey(GeneticsResequenced.modResource("entity_genes"))

		val CODEC: Codec<EntityGenes> = RecordCodecBuilder.create { instance ->
			instance.group(
				ResourceKey.codec(Registries.ENTITY_TYPE)
					.fieldOf("entity_type")
					.forGetter(EntityGenes::entityType),
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
			val entityRk = entityType.builtInRegistryHolder().key()

			val registry = registries.lookupOrThrow(REGISTRY_KEY)

			val resultRks = mutableMapOf<ResourceKey<Gene>, Int>()

			for (egHolder in registry.listElements()) {
				val eg = egHolder.value()
				if (entityRk != eg.entityType) continue

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