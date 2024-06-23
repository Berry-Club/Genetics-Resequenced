package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModAttributes {

    val ATTRIBUTE_REGISTRY: DeferredRegister<Attribute> =
        DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, GeneticsResequenced.ID)

    private const val EFFICIENCY_ATTRIBUTE_NAME = "geneticsresequenced.efficiency"
    val EFFICIENCY: DeferredHolder<Attribute, RangedAttribute> =
        ATTRIBUTE_REGISTRY.register("efficiency", Supplier {
            RangedAttribute(EFFICIENCY_ATTRIBUTE_NAME, 0.0, 0.0, 10000.0)
        })

    private const val CLIMBING_ATTRIBUTE_NAME = "geneticsresequenced.climbing"
    val WALL_CLIMBING: DeferredHolder<Attribute, RangedAttribute> =
        ATTRIBUTE_REGISTRY.register("climbing", Supplier {
            RangedAttribute(CLIMBING_ATTRIBUTE_NAME, 0.0, 0.0, 1.0)
        })

}