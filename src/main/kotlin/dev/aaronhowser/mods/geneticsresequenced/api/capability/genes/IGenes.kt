package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

interface IGenes {

    fun addGene(gene: EnumGenes): Boolean
    fun removeGene(gene: EnumGenes): Boolean
    fun hasGene(gene: EnumGenes): Boolean
    fun removeAllGenes()
    fun addAllGenes()
    fun getAmountOfGenes(): Int
    fun getGeneList(): List<EnumGenes>
    fun setGeneList(genes: List<EnumGenes>)

}