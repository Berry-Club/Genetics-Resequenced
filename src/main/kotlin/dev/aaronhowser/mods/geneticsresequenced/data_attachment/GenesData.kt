package dev.aaronhowser.mods.geneticsresequenced.data_attachment

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.attachment.AttachmentType


data class GenesData(
    val genes: Set<Gene>
) {
    constructor() : this(HashSet())

    companion object {

        val CODEC: Codec<GenesData> = Gene.CODEC.listOf().xmap(
            { list: List<Gene> ->
                GenesData(
                    HashSet<Gene>(list)
                )
            },
            { genes: GenesData ->
                ArrayList<Gene>(
                    genes.genes
                )
            })

        val attachment: AttachmentType<GenesData> by lazy { ModAttachmentTypes.GENE_CONTAINER.get() }

        var LivingEntity.genes: Set<Gene>
            get() = this.getData(attachment).genes
            set(value) {
                this.setData(attachment, GenesData(value))
            }

        fun LivingEntity.addGenes(vararg newGenes: Gene): Boolean {
            val oldSize = this.genes.size
            this.genes += newGenes
            return oldSize != this.genes.size
        }

        fun LivingEntity.removeGenes(vararg oldGenes: Gene): Boolean {
            val oldSize = this.genes.size
            this.genes -= oldGenes
            return oldSize != this.genes.size
        }

        fun LivingEntity.hasGene(gene: Gene): Boolean {
            return gene in this.genes
        }

        fun LivingEntity.removeAllGenes() {
            this.genes = emptySet()
        }

        fun LivingEntity.addAlLGenes(includeNegative: Boolean = false) {
            this.genes = Gene.getRegistry().filter { includeNegative || !it.isNegative }.toSet()
        }

    }
}