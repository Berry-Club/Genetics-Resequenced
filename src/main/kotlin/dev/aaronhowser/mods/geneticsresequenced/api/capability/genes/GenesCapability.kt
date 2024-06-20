package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.configs.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.events.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Suppress("LoggingSimilarMessage")
class GenesCapability {

    private val geneList: MutableSet<Gene> = mutableSetOf()

    fun addGene(livingEntity: LivingEntity, gene: Gene): Boolean {
        if (gene.isHidden) {
            GeneticsResequenced.LOGGER.debug("Cannot add hidden gene $gene to entity.")
            return false
        }

        if (livingEntity is Player && gene.isNegative && ServerConfig.disableGivingPlayersNegativeGenes.get() && gene != ModGenes.cringe) {
            GeneticsResequenced.LOGGER.debug("Tried to give negative gene $gene to player $livingEntity, but \"disableGivingPlayersNegativeGenes\" is true in the server config.")
            return false
        }

        val event = CustomEvents.GeneChangeEvent(livingEntity, gene, true)
        val wasCanceled = FORGE_BUS.post(event)
        if (wasCanceled) {
            GeneticsResequenced.LOGGER.debug("Event was canceled: $event")
            return false
        }

        return geneList.add(gene)
    }

    fun addGenes(livingEntity: LivingEntity, genes: Collection<Gene>) {
        genes.forEach { addGene(livingEntity, it) }
    }

    fun removeGene(livingEntity: LivingEntity, gene: Gene): Boolean {

        val event = CustomEvents.GeneChangeEvent(livingEntity, gene, false)
        val wasCanceled = FORGE_BUS.post(event)
        if (wasCanceled) {
            GeneticsResequenced.LOGGER.debug("Event was canceled: $event")
            return false
        }

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

    fun saveNbt(nbt: CompoundTag) {
        val listTag = nbt.getList(GENES_NBT_KEY, Tag.TAG_STRING.toInt())

        listTag.clear()
        listTag.addAll(geneList.map { StringTag.valueOf(it.id.toString()) })

        nbt.put(GENES_NBT_KEY, listTag)
    }

    fun loadNbt(nbt: CompoundTag) {
        val list: ListTag = nbt.getList(GENES_NBT_KEY, Tag.TAG_STRING.toInt())

        require(list.all { it is StringTag }) { "All genes must be strings" }

        val geneCapabilityStrings = list.map { it.asString }

        val listGenes: MutableList<Gene> = mutableListOf()

        for (geneId in geneCapabilityStrings) {
            val gene = Gene.fromId(geneId)
            if (gene != null) {
                listGenes.add(gene)
            } else {
                GeneticsResequenced.LOGGER.error("An entity loaded with an invalid gene \"$geneId\". Removing.")
            }
        }

        setGeneList(listGenes)
    }

    companion object {

        private const val GENES_NBT_KEY = "genes"

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