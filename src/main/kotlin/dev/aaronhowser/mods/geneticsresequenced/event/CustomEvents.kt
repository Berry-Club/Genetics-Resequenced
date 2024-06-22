package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
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

    data class GeneChangeEvent(
        val entity: LivingEntity,
        val gene: Gene,
        val wasAdded: Boolean
    ) : Event(), ICancellableEvent

}