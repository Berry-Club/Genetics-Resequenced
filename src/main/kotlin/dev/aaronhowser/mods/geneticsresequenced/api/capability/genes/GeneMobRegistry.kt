package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.EntityType
import net.minecraftforge.registries.ForgeRegistries

object GeneMobRegistry : SimpleJsonResourceReloadListener(
    GsonBuilder().setPrettyPrinting().create(),
    GeneticsResequenced.ID + "/entity_genes"    // For example, `/resources/data/geneticsresequenced/geneticsresequenced/entity_genes/example.json`
) {

    private val entityGeneMap: MutableMap<EntityType<*>, Map<Gene, Int>> = mutableMapOf()

    fun getRegistry(): Map<EntityType<*>, Map<Gene, Int>> = entityGeneMap.toMap()

    fun getGeneWeights(entity: EntityType<*>): Map<Gene, Int> {
        return entityGeneMap[entity] ?: emptyMap()
    }

    fun getGeneWeights(entity: ResourceLocation): Map<Gene, Int> {
        val entityType = ForgeRegistries.ENTITY_TYPES.getValue(entity)
            ?: throw IllegalArgumentException("Unknown entity type: $entity")

        return getGeneWeights(entityType)
    }

    private fun assignGenes(entity: ResourceLocation, genes: Map<Gene, Int>) {
        val entityType = ForgeRegistries.ENTITY_TYPES.getValue(entity)
            ?: throw IllegalArgumentException("Unknown entity type: $entity")

        val currentGenes = entityGeneMap[entityType]?.toMutableMap() ?: mutableMapOf()
        currentGenes.putAll(genes)
        entityGeneMap[entityType] = currentGenes
    }

    data class EntityGenes(
        val entity: ResourceLocation,
        val genes: Map<Gene, Int>
    ) {
        companion object {
            val CODEC: Codec<EntityGenes> = RecordCodecBuilder.create { instance ->
                instance.group(
                    ResourceLocation.CODEC.fieldOf("entity").forGetter(EntityGenes::entity),
                    Codec.unboundedMap(Gene.CODEC, Codec.INT).fieldOf("genes").forGetter(EntityGenes::genes)
                ).apply(instance, GeneMobRegistry::EntityGenes)
            }
        }
    }

    override fun apply(
        pObject: MutableMap<ResourceLocation, JsonElement>,
        pResourceManager: ResourceManager,
        pProfiler: ProfilerFiller
    ) {
        for ((key: ResourceLocation, value: JsonElement) in pObject) {
            try {
                val entityGenes: EntityGenes = EntityGenes.CODEC.decode(
                    JsonOps.INSTANCE,
                    value
                ).getOrThrow(false) {
                    GeneticsResequenced.LOGGER.error("Failed to decode entity genes for $key")
                }.first

                assignGenes(entityGenes.entity, entityGenes.genes)

                GeneticsResequenced.LOGGER.info("Loaded gene-mob data: $entityGenes")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}