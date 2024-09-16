package dev.aaronhowser.mods.geneticsresequenced.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener
import net.minecraft.util.profiling.ProfilerFiller

class GeneRequirementRegistry : SimpleJsonResourceReloadListener(
    GsonBuilder().setPrettyPrinting().create(),
    GeneticsResequenced.ID + "/gene_requirements"    // For example, `/resources/data/geneticsresequenced/geneticsresequenced/gene_requirements/example.json`
) {

    data class GeneRequirement(
        val gene: Holder<Gene>,
        val requiresGenes: List<Holder<Gene>>
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
        GeneRegistry.getAllGeneHolders(registryLookup).forEach {
            it.value().removeRequiredGenes(it.value().getRequiredGeneHolders())
        }

        val requirements = mutableMapOf<Holder<Gene>, List<Holder<Gene>>>()

        for ((key: ResourceLocation, value: JsonElement) in pObject) {
            try {
                val geneRequirement: GeneRequirement = GeneRequirement.CODEC.decode(
                    JsonOps.INSTANCE,
                    value
                ).getOrThrow {
                    IllegalArgumentException("Failed to decode entity genes for $key")
                }.first

                val (gene: Holder<Gene>, requiresGenes: List<Holder<Gene>>) = geneRequirement
                requirements[gene] = requirements.getOrDefault(gene, emptyList()).plus(requiresGenes)

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        requirements.forEach { (gene: Holder<Gene>, requiresGenes: List<Holder<Gene>>) ->
            GeneticsResequenced.LOGGER.debug("Adding required genes for $gene: ${requiresGenes.joinToString(", ")}")
            gene.value().addRequiredGenes(requiresGenes)
        }
    }
}