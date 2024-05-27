package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

class GeneBuilder(
    val id: ResourceLocation
) {

    private var isNegative: Boolean = false
    private var dnaPointsRequired: Int = -1
    private var requiredGenes: MutableSet<Gene> = mutableSetOf()
    private var potionDetails: PotionDetails? = null

    fun build(): Gene {
        return Gene.register(id, isNegative, dnaPointsRequired, requiredGenes, potionDetails)
    }

    fun setNegative(): GeneBuilder {
        this.isNegative = true
        return this
    }

    fun setDnaPointsRequired(amountNeeded: Int): GeneBuilder {
        this.dnaPointsRequired = amountNeeded
        return this
    }

    fun addRequiredGenes(vararg gene: Gene): GeneBuilder {
        requiredGenes.addAll(gene)
        return this
    }

    fun setPotion(
        effect: MobEffect,
        level: Int,
        duration: Int = 300
    ): GeneBuilder {
        potionDetails = PotionDetails(effect, level, duration)
        return this
    }

    data class PotionDetails(
        val effect: MobEffect,
        val level: Int,
        val duration: Int
    )

}