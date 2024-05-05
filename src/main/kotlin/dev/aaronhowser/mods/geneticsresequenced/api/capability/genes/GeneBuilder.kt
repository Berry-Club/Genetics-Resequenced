package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

class GeneBuilder(
    val id: ResourceLocation
) {

    var isNegative: Boolean = false
    var mutatesInto: Gene? = null
    var dnaPointsRequired: Int = -1
    var potionDetails: PotionDetails? = null

    fun build(): Gene {
        return Gene.register(this)
    }

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