package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

interface IGene {

    fun addGene(gene: EnumGenes)
    fun removeGene(gene: EnumGenes)
    fun hasGene(gene: EnumGenes): Boolean
    fun removeAllGenes()
    fun addAllGenes()
    fun getAmountOfGenes(): Int
    fun getGeneList(): List<EnumGenes>
    fun setGeneList(genes: List<EnumGenes>)

}