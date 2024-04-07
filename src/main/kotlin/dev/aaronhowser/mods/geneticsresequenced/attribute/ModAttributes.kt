package dev.aaronhowser.mods.geneticsresequenced.attribute

import net.minecraft.world.entity.ai.attributes.RangedAttribute

object ModAttributes {

    private const val EFFICIENCY_ATTRIBUTE_NAME = "geneticsresequenced.efficiency"
    val EFFICIENCY_ATTRIBUTE = RangedAttribute(EFFICIENCY_ATTRIBUTE_NAME, 0.0, 0.0, 100000.0)

    private const val CLIMBING_ATTRIBUTE_NAME = "geneticsresequenced.climbing"
    val CLIMBING_ATTRIBUTE = RangedAttribute(CLIMBING_ATTRIBUTE_NAME, 0.0, 0.0, 1.0)

}