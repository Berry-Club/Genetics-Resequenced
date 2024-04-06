package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.ClickGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.TickGenes
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherPlayerEvents {

    @SubscribeEvent
    fun onInteractWithBlock(event: PlayerInteractEvent.RightClickBlock) {
        if (event.side.isClient) return

        val player = event.entity

        val genes = player.getGenes() ?: return

        if (genes.hasGene(EnumGenes.EAT_GRASS)) ClickGenes.eatGrass(event)
    }

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {
        if (event.side.isClient) return

        TickGenes.handleNoHunger(event.player)
        TickGenes.handleFlight(event.player)
    }

}