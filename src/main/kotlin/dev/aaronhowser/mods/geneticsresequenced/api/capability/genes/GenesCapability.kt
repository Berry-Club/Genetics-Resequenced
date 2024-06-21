package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class GenesCapability {

    private val geneList: MutableSet<Gene> = mutableSetOf()

    fun addGene(livingEntity: LivingEntity, gene: Gene): Boolean {
        if (gene.isHidden) {
            GeneticsResequenced.LOGGER.debug("Cannot add hidden gene $gene to entity.")
            return false
        }

        if (livingEntity is Player && gene.isNegative
//            && ServerConfig.disableGivingPlayersNegativeGenes.get() && gene != ModGenes.cringe
        ) {
            GeneticsResequenced.LOGGER.debug("Tried to give negative gene $gene to player $livingEntity, but \"disableGivingPlayersNegativeGenes\" is true in the server config.")
            return false
        }

//        val event = CustomEvents.GeneChangeEvent(livingEntity, gene, true)
//        val wasCanceled = FORGE_BUS.post(event)
//        if (wasCanceled) {
//            GeneticsResequenced.LOGGER.debug("Event was canceled: $event")
//            return false
//        }

        return geneList.add(gene)
    }

    fun removeGene(livingEntity: LivingEntity, gene: Gene): Boolean {

//        val event = CustomEvents.GeneChangeEvent(livingEntity, gene, false)
//        val wasCanceled = FORGE_BUS.post(event)
//        if (wasCanceled) {
//            GeneticsResequenced.LOGGER.debug("Event was canceled: $event")
//            return false
//        }

        return geneList.remove(gene)
    }

    fun removeGenes(livingEntity: LivingEntity, genes: Collection<Gene>) {
        for (gene in genes) {
            removeGene(livingEntity, gene)
        }
    }

    fun hasGene(gene: Gene): Boolean {
        return geneList.contains(gene)
    }

    fun removeAllGenes(livingEntity: LivingEntity) {
        val iterator = geneList.iterator()

        while (iterator.hasNext()) {
            val gene = iterator.next()
            removeGene(livingEntity, gene)
        }
    }

    fun addAllGenes(livingEntity: LivingEntity) {
        for (gene in Gene.getRegistry()) {
            addGene(livingEntity, gene)
        }
    }

    fun getAmountOfGenes(): Int {
        return geneList.size
    }

    fun getGeneList(): List<Gene> {
        return geneList.toList()
    }

    /**
     * Skips cancel checks
     */
    fun setGeneList(genes: List<Gene>) {
        geneList.clear()
        geneList.addAll(genes)
    }

    companion object {

        fun LivingEntity.getGenes(): GenesCapability? {

            if (!this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).isPresent) return null

            return this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).orElseThrow {
                IllegalStateException("Genes capability not present")
            }

        }

        fun LivingEntity.hasGene(gene: Gene): Boolean {
            return this.getGenes()?.hasGene(gene) ?: false
        }
    }

}