package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

class GeneBuilder(
    val id: ResourceLocation
) {

    private var isNegative: Boolean = false
    private var mutatesInto: Gene? = null
    private var dnaPointsRequired: Int = -1
    private var potionDetails: PotionDetails? = null

    fun build(): Gene = Gene(
        id = id,
        isNegative = isNegative,
        mutatesInto = mutatesInto,
        dnaPointsRequired = dnaPointsRequired,
        potionDetails = potionDetails
    )

    fun setNegative(): GeneBuilder {
        this.isNegative = true
        return this
    }

    fun setMutatesInto(mutatesInto: Gene): GeneBuilder {
        this.mutatesInto = mutatesInto
        return this
    }

    fun setDnaPointsRequired(amountNeeded: Int): GeneBuilder {
        this.dnaPointsRequired = amountNeeded
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

    class PotionDetails(
        val effect: MobEffect,
        val level: Int,
        val duration: Int
    )

}