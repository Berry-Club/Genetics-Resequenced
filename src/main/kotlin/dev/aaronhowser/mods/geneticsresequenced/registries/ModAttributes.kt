package dev.aaronhowser.mods.geneticsresequenced.registries

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ModAttributes {

    val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
        DeferredRegister.create(ForgeRegistries.ATTRIBUTES, GeneticsResequenced.ID)

    private const val EFFICIENCY_ATTRIBUTE_NAME = "geneticsresequenced.efficiency"
    val EFFICIENCY by ATTRIBUTE_REGISTRY.registerObject("efficiency") {
        RangedAttribute(EFFICIENCY_ATTRIBUTE_NAME, 0.0, 0.0, 10000.0)
    }

    private const val CLIMBING_ATTRIBUTE_NAME = "geneticsresequenced.climbing"
    val WALL_CLIMBING by ATTRIBUTE_REGISTRY.registerObject("climbing") {
        RangedAttribute(CLIMBING_ATTRIBUTE_NAME, 0.0, 0.0, 1.0)
    }

}