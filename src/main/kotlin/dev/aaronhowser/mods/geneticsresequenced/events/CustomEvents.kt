package dev.aaronhowser.mods.geneticsresequenced.events

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraftforge.eventbus.api.Event

object CustomEvents {

    data class PlayerInventoryChangeEvent(
        val player: ServerPlayer,
        val slot: Int,
        val stack: ItemStack
    ) : Event()

}