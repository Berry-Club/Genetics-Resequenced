package dev.aaronhowser.mods.geneticsresequenced.api.genes

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect

data class GeneProperties(
    val id: ResourceLocation,
    val isNegative: Boolean = false,
    val isHidden: Boolean = false,
    val canMobsHave: Boolean = false,
    val dnaPointsRequired: Int = -1,
    val mutatesInto: Gene? = null,
    val potionDetails: PotionDetails? = null
) {

    data class PotionDetails(
        val effect: Holder<MobEffect>,
        val level: Int,
        val duration: Int = -1,
        val showIcon: Boolean = false
    )

}