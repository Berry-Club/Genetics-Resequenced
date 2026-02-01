package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.Event

data class ModifyGeneRequirementsEvent(
	val gene: ResourceKey<Gene>,
	val requirements: MutableSet<ResourceKey<Gene>>
) : Event()