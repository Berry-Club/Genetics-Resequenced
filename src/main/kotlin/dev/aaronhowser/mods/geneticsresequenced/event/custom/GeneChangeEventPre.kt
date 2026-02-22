package dev.aaronhowser.mods.geneticsresequenced.event.custom

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.eventbus.api.Cancelable
import net.minecraftforge.eventbus.api.Event

@Cancelable
data class GeneChangeEventPre(
	val entity: LivingEntity,
	val geneHolder: Holder<Gene>,
	val isAddition: Boolean
) : Event()