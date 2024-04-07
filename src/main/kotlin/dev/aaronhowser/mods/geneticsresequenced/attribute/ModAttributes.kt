package dev.aaronhowser.mods.geneticsresequenced.attribute

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ModAttributes {

    val REGISTRY: DeferredRegister<Attribute> =
        DeferredRegister.create(ForgeRegistries.ATTRIBUTES, GeneticsResequenced.ID)

    private const val EFFICIENCY_ATTRIBUTE_NAME = "geneticsresequenced.efficiency"
    val EFFICIENCY by REGISTRY.registerObject("efficiency") {
        RangedAttribute(EFFICIENCY_ATTRIBUTE_NAME, 0.0, 0.0, 1.0)
    }

    private const val CLIMBING_ATTRIBUTE_NAME = "geneticsresequenced.climbing"
    val CLIMBING by REGISTRY.registerObject("climbing") {
        RangedAttribute(CLIMBING_ATTRIBUTE_NAME, 0.0, 0.0, 1.0)
    }

}