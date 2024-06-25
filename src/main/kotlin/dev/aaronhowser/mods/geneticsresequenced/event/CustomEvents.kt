package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.Event
import net.neoforged.bus.api.ICancellableEvent

object CustomEvents {

    data class PlayerInventoryChangeEvent(
        val player: ServerPlayer,
        val slot: Int,
        val stack: ItemStack
    ) : Event()

    abstract class GeneChangeEvent : Event() {

        abstract val entity: LivingEntity
        abstract val gene: Gene
        abstract val wasAdded: Boolean

        data class Pre(
            override val entity: LivingEntity,
            override val gene: Gene,
            override val wasAdded: Boolean
        ) : GeneChangeEvent(), ICancellableEvent

        data class Post(
            override val entity: LivingEntity,
            override val gene: Gene,
            override val wasAdded: Boolean
        ) : GeneChangeEvent()

    }

}