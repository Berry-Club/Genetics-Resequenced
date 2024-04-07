package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.LivingEntity

class GenesCapability {

    private val geneList: MutableSet<Gene> = mutableSetOf()

    fun addGene(gene: Gene): Boolean {
        return geneList.add(gene)
    }

    fun removeGene(gene: Gene): Boolean {
        return geneList.remove(gene)
    }

    fun hasGene(gene: Gene): Boolean {
        return geneList.contains(gene)
    }

    fun removeAllGenes() {
        geneList.clear()
    }

    fun addAllGenes() {
        geneList.addAll(Gene.REGISTRY)
    }

    fun getAmountOfGenes(): Int {
        return geneList.size
    }

    fun getGeneList(): List<Gene> {
        return geneList.toList()
    }

    fun setGeneList(genes: List<Gene>) {
        geneList.clear()
        geneList.addAll(genes)
    }

    fun saveNbt(nbt: CompoundTag) {
        val listTag = nbt.getList(NBT_KEY, Tag.TAG_STRING.toInt())

        listTag.clear()
        listTag.addAll(geneList.map { StringTag.valueOf(it.id) })

        nbt.put(NBT_KEY, listTag)
    }

    fun loadNbt(nbt: CompoundTag) {
        val list: ListTag = nbt.getList(NBT_KEY, Tag.TAG_STRING.toInt())

        require(list.all { it is StringTag }) { "All genes must be strings" }

        val strings = list.map { it.asString }

        val listGenes: MutableList<Gene> = mutableListOf()

        for (string in strings) {
            val gene = Gene.REGISTRY.find { it.id == string }

            if (gene != null) {
                listGenes.add(gene)
            } else {
                GeneticsResequenced.LOGGER.error("An entity loaded with an invalid gene \"$string\". Removing.")
            }
        }

        setGeneList(listGenes)
    }

    companion object {

        private const val NBT_KEY = "genes"

        fun LivingEntity.getGenes(): GenesCapability? {

            if (!this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).isPresent) return null

            return this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).orElseThrow {
                IllegalStateException("Genes capability not present")
            }

        }
    }
}