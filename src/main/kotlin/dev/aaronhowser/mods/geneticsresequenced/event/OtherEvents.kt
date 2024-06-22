package dev.aaronhowser.mods.geneticsresequenced.event

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.util.ModScheduler
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.ServerTickEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherEvents {

    @SubscribeEvent
    fun onServerTick(event: ServerTickEvent.Post) {

    }

}