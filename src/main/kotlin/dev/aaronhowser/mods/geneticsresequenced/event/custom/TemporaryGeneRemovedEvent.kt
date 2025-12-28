package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.Event

data class TemporaryGeneRemovedEvent(
	val entity: LivingEntity,
	val geneHolder: Holder<Gene>
) : Event()