package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.ClickGenes
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object ClickEntityEvents {

    @SubscribeEvent
    fun onInteractEntity(event: PlayerInteractEvent.EntityInteract) {
        if (event.side.isClient) return

        ClickGenes.wooly(event)
        ClickGenes.milky(event)
        ClickGenes.meaty(event)
    }

}