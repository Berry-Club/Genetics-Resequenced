package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

class Genes : IGenes {

    private val enumGenesList: MutableSet<EnumGenes> = mutableSetOf()

    override fun addGene(gene: EnumGenes): Boolean {
        return enumGenesList.add(gene)
    }

    override fun removeGene(gene: EnumGenes): Boolean {
        return enumGenesList.remove(gene)
    }

    override fun hasGene(gene: EnumGenes): Boolean {
        return enumGenesList.contains(gene)
    }

    override fun removeAllGenes() {
        enumGenesList.clear()
    }

    override fun addAllGenes() {
        enumGenesList.addAll(EnumGenes.values())
    }

    override fun getAmountOfGenes(): Int {
        return enumGenesList.size
    }

    override fun getGeneList(): List<EnumGenes> {
        return enumGenesList.toList()
    }

    override fun setGeneList(genes: List<EnumGenes>) {
        enumGenesList.clear()
        enumGenesList.addAll(genes)
    }
}