package dev.aaronhowser.mods.geneticsresequenced.events.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.genebehaviors.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehaviors.ClickGenes
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object ClickItemEvents {

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        if (event.side.isClient) return

        ClickGenes.milkyItem(event)
        ClickGenes.shootFireball(event)
    }

    @SubscribeEvent
    fun onDigSpeed(event: PlayerEvent.BreakSpeed) {
        AttributeGenes.handleEfficiency(event)
    }

}