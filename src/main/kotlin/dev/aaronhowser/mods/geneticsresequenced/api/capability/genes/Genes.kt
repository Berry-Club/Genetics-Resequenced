package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.LivingEntity

object Genes : IGenes {

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

    private const val NBT_KEY = "genes"

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
        val listGenes = strings.map { EnumGenes.valueOf(it) }

        setGeneList(listGenes)
    }

    fun LivingEntity.getGenes(): IGenes {
        return this.getCapability(GenesCapabilityProvider.GENE_CAPABILITY).orElseThrow {
            IllegalStateException("Genes capability not present")
        }
    }
}