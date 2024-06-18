package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

class GeneBuilder(
    val id: ResourceLocation
) {

    private var hidden: Boolean = false
    private var isNegative: Boolean = false
    private var canMobsHave: Boolean = false
    private var dnaPointsRequired: Int = -1
    private var mutatesInto: Gene? = null
    private var potionDetails: PotionDetails? = null

    fun build(): Gene {
        return Gene.register(
            id = id,
            isNegative = isNegative,
            canMobsHave = canMobsHave,
            dnaPointsRequired = dnaPointsRequired,
            mutatesInto = mutatesInto,
            potionDetails = potionDetails,
            hidden = hidden
        )
    }

    fun setNegative(): GeneBuilder {
        this.isNegative = true
        return this
    }

    fun allowMobs(): GeneBuilder {
        this.canMobsHave = true
        return this
    }

    fun setDnaPointsRequired(amountNeeded: Int): GeneBuilder {
        this.dnaPointsRequired = amountNeeded
        return this
    }

    fun setMutatesInto(gene: Gene): GeneBuilder {
        mutatesInto = gene
        return this
    }

    fun setPotion(
        effect: MobEffect,
        level: Int,
        duration: Int = 300,
        showIcon: Boolean = false
    ): GeneBuilder {
        potionDetails = PotionDetails(effect, level, duration, showIcon)
        return this
    }

    data class PotionDetails(
        val effect: MobEffect,
        val level: Int,
        val duration: Int,
        val showIcon: Boolean
    )

    fun removePlasmid(): GeneBuilder {
        hidden = true
        return this
    }

}