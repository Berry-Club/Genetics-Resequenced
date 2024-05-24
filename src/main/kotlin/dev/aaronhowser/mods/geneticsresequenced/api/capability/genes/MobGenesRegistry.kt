package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import net.minecraft.world.entity.EntityType

object MobGenesRegistry {

    private val mobGenes: MutableMap<EntityType<*>, MutableMap<Gene, Int>> = mutableMapOf()

    fun addGenesToEntity(entityType: EntityType<*>, weightToGeneMap: Map<Gene, Int>) {
        val geneList: MutableMap<Gene, Int> = mobGenes[entityType] ?: mutableMapOf()
        geneList.putAll(weightToGeneMap)
        mobGenes[entityType] = geneList
    }

    fun removeGenesFromEntity(entityType: EntityType<*>, vararg genes: Gene) {
        val geneList = mobGenes[entityType] ?: run {
            GeneticsResequenced.LOGGER.warn("Tried to remove genes from $entityType, but it has no genes")
            return
        }

        genes.forEach { geneList.remove(key = it) }
    }

    fun getGenesForEntity(entityType: EntityType<*>): Map<Gene, Int> {
        return mobGenes[entityType] ?: mapOf(DefaultGenes.BASIC to 1)
    }

    fun getRegistry(): Map<EntityType<*>, Map<Gene, Int>> {
        return mobGenes.toMap()
    }


}