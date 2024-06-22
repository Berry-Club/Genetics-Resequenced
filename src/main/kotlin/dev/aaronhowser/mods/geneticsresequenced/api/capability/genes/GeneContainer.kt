package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttachmentTypes
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.attachment.AttachmentType


data class GeneContainer(
    val genes: Set<Gene>
) {
    constructor() : this(HashSet())

    companion object {

        val CODEC: Codec<GeneContainer> = Gene.CODEC.listOf().xmap(
            { list: List<Gene> ->
                GeneContainer(
                    HashSet<Gene>(list)
                )
            },
            { genes: GeneContainer ->
                ArrayList<Gene>(
                    genes.genes
                )
            })

        val attachment: AttachmentType<GeneContainer> by lazy { ModAttachmentTypes.GENE_CONTAINER.get() }

        var LivingEntity.genes: Set<Gene>
            get() = this.getData(attachment).genes
            set(value) {
                this.setData(attachment, GeneContainer(value))
            }

        fun LivingEntity.addGenes(vararg newGenes: Gene) {
            this.genes += newGenes
        }

        fun LivingEntity.removeGenes(vararg oldGenes: Gene) {
            this.genes -= oldGenes
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