package dev.aaronhowser.mods.geneticsresequenced.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.EntityType

class EntityGenes : SimpleJsonResourceReloadListener(
    GsonBuilder().setPrettyPrinting().create(),
    GeneticsResequenced.ID + "/entity_genes"
) {

    companion object {
        private val entityGeneMap: MutableMap<EntityType<*>, Map<ResourceKey<Gene>, Int>> = mutableMapOf()
        fun getEntityGeneRkMap(): Map<EntityType<*>, Map<ResourceKey<Gene>, Int>> = entityGeneMap.toMap()

        fun getEntityGeneHolderMap(registries: HolderLookup.Provider): Map<EntityType<*>, Map<Holder<Gene>, Int>> {
            return entityGeneMap.map { (entityType, geneRkMap) ->
                val holders = getGeneHolderWeights(entityType, registries)

                entityType to holders
            }.toMap()
        }

        fun getGeneRkWeights(entityType: EntityType<*>): Map<ResourceKey<Gene>, Int> {
            return entityGeneMap[entityType] ?: emptyMap()
        }

        fun getGeneHolderWeights(entityType: EntityType<*>, registries: HolderLookup.Provider): Map<Holder<Gene>, Int> {
            return getGeneRkWeights(entityType).map { (rk, weight) ->
                rk.getHolder(registries)!! to weight
            }.toMap()
        }

    }

    private fun addGeneWeights(entityRl: ResourceLocation, newGeneWeights: Map<ResourceKey<Gene>, Int>) {
        val entityType = OtherUtil.getEntityType(entityRl)

        val currentGenes = entityGeneMap[entityType]?.toMutableMap() ?: mutableMapOf()

        for ((gene, weight) in newGeneWeights) {
            val currentWeightForGene = currentGenes[gene] ?: 0
            currentGenes[gene] = currentWeightForGene + weight
        }

        entityGeneMap[entityType] = currentGenes
    }

    data class EntityGenes(
        val entity: ResourceLocation,
        val geneWeights: Map<ResourceKey<Gene>, Int>
    ) {
        companion object {
            val CODEC: Codec<EntityGenes> = RecordCodecBuilder.create { instance ->
                instance.group(
                    ResourceLocation.CODEC
                        .fieldOf("entity")
                        .forGetter(EntityGenes::entity),
                    Codec.unboundedMap(
                        ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY),
                        Codec.INT
                    )
                        .fieldOf("gene_weights")
                        .forGetter(EntityGenes::geneWeights)
                ).apply(instance, ::EntityGenes)
            }
        }
    }

    override fun apply(
        pObject: MutableMap<ResourceLocation, JsonElement>,
        pResourceManager: ResourceManager,
        pProfiler: ProfilerFiller
    ) {

        entityGeneMap.clear()

        for ((key: ResourceLocation, value: JsonElement) in pObject) {
            try {
                val entityGenes: EntityGenes = EntityGenes.CODEC.decode(
                    JsonOps.INSTANCE,
                    value
                ).getOrThrow {
                    IllegalArgumentException("Failed to decode entity genes for $key")
                }.first

                val entityName = entityGenes.entity.toString().split(":")[1]
                val fileName = key.toString().split(":")[1]
                if (entityName != fileName) {
                    GeneticsResequenced.LOGGER.warn("Mob-Gene data for $key has the entity $entityName instead of $fileName. This may be a mistake.")
                }

                addGeneWeights(entityGenes.entity, entityGenes.geneWeights)

                GeneticsResequenced.LOGGER.debug("Loaded gene-mob data: $entityGenes")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}