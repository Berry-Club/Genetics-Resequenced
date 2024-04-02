package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.LivingEntity

class Genes {

    private val enumGenesList: MutableSet<EnumGenes> = mutableSetOf()

    fun addGene(gene: EnumGenes): Boolean {
        return enumGenesList.add(gene)
    }

    fun removeGene(gene: EnumGenes): Boolean {
        return enumGenesList.remove(gene)
    }

    fun hasGene(gene: EnumGenes): Boolean {
        return enumGenesList.contains(gene)
    }

    fun removeAllGenes() {
        enumGenesList.clear()
    }

    fun addAllGenes() {
        enumGenesList.addAll(EnumGenes.values())
    }

    fun getAmountOfGenes(): Int {
        return enumGenesList.size
    }

    fun getGeneList(): List<EnumGenes> {
        return enumGenesList.toList()
    }

    fun setGeneList(genes: List<EnumGenes>) {
        enumGenesList.clear()
        enumGenesList.addAll(genes)
    }

    fun saveNbt(nbt: CompoundTag) {
        val listTag = nbt.getList(NBT_KEY, Tag.TAG_STRING.toInt())

        listTag.clear()
        listTag.addAll(enumGenesList.map { StringTag.valueOf(it.name) })

        nbt.put(NBT_KEY, listTag)
    }

    fun loadNbt(nbt: CompoundTag) {
        val list: ListTag = nbt.getList(NBT_KEY, Tag.TAG_STRING.toInt())

        require(list.all { it is StringTag }) { "All genes must be strings" }

        val strings = list.map { it.asString }

        val listGenes: MutableList<EnumGenes> = mutableListOf()

        for (string in strings) {
            try {
                listGenes.add(EnumGenes.valueOf(string))
            } catch (e: IllegalArgumentException) {
                GeneticsResequenced.LOGGER.error("An entity loaded with an invalid gene \"$string\". Removing.")
            }
        }

        setGeneList(listGenes)
    }

    companion object {

        private const val NBT_KEY = "genes"

        fun LivingEntity.getGenes(): Genes? {

            if (!this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).isPresent) return null

            return this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).orElseThrow {
                IllegalStateException("Genes capability not present")
            }

        }
    }
}