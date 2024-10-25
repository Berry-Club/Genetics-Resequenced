package dev.aaronhowser.mods.geneticsresequenced.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolder
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
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

    companion object {
        const val DIRECTORY = GeneticsResequenced.ID + "/entity_genes"

        private val entityGeneMap: MutableMap<EntityType<*>, Map<ResourceKey<Gene>, Int>> = mutableMapOf()
        fun getEntityGeneRkMap(): Map<EntityType<*>, Map<ResourceKey<Gene>, Int>> = entityGeneMap.toMap()

        fun getEntityGeneHolderMap(registries: HolderLookup.Provider): Map<EntityType<*>, Map<Holder<Gene>, Int>> {
            return entityGeneMap.map { (entityType, geneRkMap) ->
                val holders = getGeneHolderWeights(entityType, registries)

                entityType to holders
            }.toMap()
        }

        fun getGeneRkWeights(entityType: EntityType<*>): Map<ResourceKey<Gene>, Int> {
            return entityGeneMap[entityType] ?: mapOf(ModGenes.BASIC to 1)
        }

        fun getGeneHolderWeights(entityType: EntityType<*>, registries: HolderLookup.Provider): Map<Holder<Gene>, Int> {
            return getGeneRkWeights(entityType).map { (rk, weight) ->
                rk.getHolder(registries)!! to weight
            }.toMap()
        }

    }

    private fun addGeneWeights(
        entityRk: ResourceKey<EntityType<*>>,
        newGeneWeights: Map<ResourceKey<Gene>, Int>
    ) {
        val entityType = BuiltInRegistries.ENTITY_TYPE.get(entityRk)!!
        val currentGenes = entityGeneMap[entityType]?.toMutableMap() ?: mutableMapOf()

        for ((gene, weight) in newGeneWeights) {
            currentGenes[gene] = currentGenes[gene]?.plus(weight) ?: weight
        }

        entityGeneMap[entityType] = currentGenes
    }

    data class EntityGenesData(
        val entity: ResourceKey<EntityType<*>>,
        val geneWeights: Map<ResourceKey<Gene>, Int>
    ) {
        companion object {
            val CODEC: Codec<EntityGenesData> = RecordCodecBuilder.create { instance ->
                instance.group(
                    ResourceKey.codec(Registries.ENTITY_TYPE)
                        .fieldOf("entity")
                        .forGetter(EntityGenesData::entity),
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
        entityGeneMap.clear()

        for ((key: ResourceLocation, value: JsonElement) in pObject) {
            try {
                val entityGenesData: EntityGenesData = EntityGenesData.CODEC.decode(
                    JsonOps.INSTANCE,
                    value
                ).getOrThrow {
                    IllegalArgumentException("Failed to decode entity genes for $key")
                }.first

                val entityName = entityGenesData.entity.location().path
                val fileName = key.toString().split(":")[1]
                if (entityName != fileName) {
                    GeneticsResequenced.LOGGER.warn("Gene-mob data for $key has the entity $entityName instead of $fileName. This may be a mistake.")
                }

                addGeneWeights(
                    entityGenesData.entity,
                    entityGenesData.geneWeights
                )

                GeneticsResequenced.LOGGER.debug("Loaded gene-mob data for ${entityGenesData.entity.location()}, with ${entityGenesData.geneWeights.size} genes")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}