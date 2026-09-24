package dev.aaronhowser.mods.genetics_resequenced.event.custom

import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.eventbus.api.Event

data class GeneChangeEventPost(
	val entity: LivingEntity,
	val geneHolder: Holder<Gene>,
	val isAddition: Boolean
) : Event()