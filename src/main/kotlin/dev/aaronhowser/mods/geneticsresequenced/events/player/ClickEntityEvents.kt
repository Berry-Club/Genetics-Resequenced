package dev.aaronhowser.mods.geneticsresequenced.events.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.ClickGenes
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object ClickEntityEvents {

    @SubscribeEvent
    fun onInteractEntity(event: PlayerInteractEvent.EntityInteract) {
        ClickGenes.handleWooly(event)
        ClickGenes.handleMilky(event)
        ClickGenes.handleMeaty(event)
    }

}