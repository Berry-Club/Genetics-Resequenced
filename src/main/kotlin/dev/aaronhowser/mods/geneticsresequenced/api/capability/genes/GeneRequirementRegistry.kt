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

object GeneRequirementRegistry : SimpleJsonResourceReloadListener(
    GsonBuilder().setPrettyPrinting().create(),
    GeneticsResequenced.ID + "/gene_requirements"    // For example, `/resources/data/geneticsresequenced/geneticsresequenced/gene_requirements/example.json`
) {

    data class GeneRequirement(
        val gene: Gene,
        val requiresGenes: List<Gene>
    ) {
        companion object {
            val CODEC: Codec<GeneRequirement> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Gene.CODEC.fieldOf("gene").forGetter(GeneRequirement::gene),
                    Codec.list(Gene.CODEC).fieldOf("requiresGenes").forGetter(GeneRequirement::requiresGenes)
                ).apply(instance, ::GeneRequirement)
            }
        }
    }

    override fun apply(
        pObject: MutableMap<ResourceLocation, JsonElement>,
        pResourceManager: ResourceManager,
        pProfiler: ProfilerFiller
    ) {
        Gene.getRegistry().forEach { it.removeRequiredGenes(it.getRequiredGenes()) }

        val requirements = mutableMapOf<Gene, List<Gene>>()

        for ((key: ResourceLocation, value: JsonElement) in pObject) {
            try {
                val geneRequirement: GeneRequirement = GeneRequirement.CODEC.decode(
                    JsonOps.INSTANCE,
                    value
                ).getOrThrow(false) {
                    GeneticsResequenced.LOGGER.error("Failed to decode entity genes for $key")
                }.first

                val (gene: Gene, requiresGenes: List<Gene>) = geneRequirement
                requirements[gene] = requirements.getOrDefault(gene, emptyList()).plus(requiresGenes)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        requirements.forEach { (gene: Gene, requiresGenes: List<Gene>) ->
            GeneticsResequenced.LOGGER.info("Adding required genes for $gene: ${requiresGenes.joinToString(", ")}")
            gene.addRequiredGenes(requiresGenes)
        }
    }
}