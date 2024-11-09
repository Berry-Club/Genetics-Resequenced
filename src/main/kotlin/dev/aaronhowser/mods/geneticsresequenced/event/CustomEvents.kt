package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent

object CustomEvents {

    abstract class GeneChangeEvent : Event() {

        abstract val entity: LivingEntity
        abstract val geneHolder: Holder<Gene>
        abstract val wasAdded: Boolean

        data class Pre(
            override val entity: LivingEntity,
            override val geneHolder: Holder<Gene>,
            override val wasAdded: Boolean
        ) : GeneChangeEvent(), ICancellableEvent

        data class Post(
            override val entity: LivingEntity,
            override val geneHolder: Holder<Gene>,
            override val wasAdded: Boolean
        ) : GeneChangeEvent()

    }

}