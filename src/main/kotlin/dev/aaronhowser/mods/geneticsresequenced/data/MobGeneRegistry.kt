package dev.aaronhowser.mods.geneticsresequenced.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.EntityType

class MobGeneRegistry : SimpleJsonResourceReloadListener(
    GsonBuilder().setPrettyPrinting().create(),
    GeneticsResequenced.ID + "/entity_genes"
) {

    companion object {
        private val entityGeneMap: MutableMap<EntityType<*>, Map<Gene, Int>> = mutableMapOf()
        fun getRegistry(): Map<EntityType<*>, Map<Gene, Int>> = entityGeneMap.toMap()


        fun getGeneWeights(entityType: EntityType<*>): Map<Gene, Int> {
            return entityGeneMap[entityType] ?: emptyMap()
        }

        fun getGeneWeights(entityRl: ResourceLocation): Map<Gene, Int> {
            val entityType = OtherUtil.getEntityType(entityRl)

            return getGeneWeights(entityType)
        }

    }

    private fun assignGenes(entityRl: ResourceLocation, genes: Map<Gene, Int>) {
        val entityType = OtherUtil.getEntityType(entityRl)

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
                ).apply(instance, MobGeneRegistry::EntityGenes)
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

                assignGenes(entityGenes.entity, entityGenes.genes)

                GeneticsResequenced.LOGGER.debug("Loaded gene-mob data: $entityGenes")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }


}