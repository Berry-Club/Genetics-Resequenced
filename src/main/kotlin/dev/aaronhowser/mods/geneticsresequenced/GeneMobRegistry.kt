package dev.aaronhowser.mods.geneticsresequenced

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller

class GeneMobRegistry : SimpleJsonResourceReloadListener(
    GsonBuilder().setPrettyPrinting().create(),
    GeneticsResequenced.ID + "/entity_genes"
) {

    data class EntityGenes(
        val entity: ResourceLocation,
        val genes: List<Gene>
    ) {
        companion object {
            val allEntityGenes: HashMap<ResourceLocation, EntityGenes> = hashMapOf()

            val CODEC: Codec<EntityGenes> = RecordCodecBuilder.create { instance ->
                instance.group(
                    ResourceLocation.CODEC.fieldOf("entity").forGetter(EntityGenes::entity),
                    Gene.CODEC.listOf().fieldOf("genes").forGetter(EntityGenes::genes)
                ).apply(instance, ::EntityGenes)
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

                EntityGenes.allEntityGenes[key] = entityGenes

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}