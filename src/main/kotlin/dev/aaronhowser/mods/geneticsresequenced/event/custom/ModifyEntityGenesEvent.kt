package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.neoforged.bus.api.Event

data class ModifyEntityGenesEvent(
	val entityType: ResourceKey<EntityType<*>>,
	val geneWeights: MutableMap<ResourceKey<Gene>, Int>
) : Event()