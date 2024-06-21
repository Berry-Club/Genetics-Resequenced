package dev.aaronhowser.mods.geneticsresequenced.api.capability.genes

import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.capabilities.EntityCapability

object GenesCapabilityProvider {


    val geneCapability: EntityCapability<LivingEntity, Void> =
        EntityCapability.createVoid(
            OtherUtil.modResource("genes"),
            LivingEntity::class.java
        )

}