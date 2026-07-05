package dev.aaronhowser.mods.genetics_resequenced.event.custom

import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.Event

data class ModifyGeneRequirementsEvent(
	val gene: ResourceKey<Gene>,
	val requirements: MutableSet<ResourceKey<Gene>>
) : Event()