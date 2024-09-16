package dev.aaronhowser.mods.geneticsresequenced.api.genes

import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier

data class GeneProperties(
    val id: ResourceLocation,
    val isNegative: Boolean = false,
    val isHidden: Boolean = false,
    val canMobsHave: Boolean = false,
    val dnaPointsRequired: Int = -1,
    val mutatesInto: Gene? = null,
    val potionDetails: PotionDetails? = null,
    val attributeModifiers: Map<Holder<Attribute>, Collection<AttributeModifier>> = emptyMap()
) {

    data class PotionDetails(
        val effect: Holder<MobEffect>,
        val level: Int = 1,
        val duration: Int = -1,
        val showIcon: Boolean = false
    )

}