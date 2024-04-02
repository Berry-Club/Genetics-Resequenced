package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.ClickGenes
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

        val player = event.entity
        val genes = player.getGenes() ?: return

        if (genes.hasGene(EnumGenes.MILKY)) ClickGenes.milkyItem(event)
        if (genes.hasGene(EnumGenes.SHOOT_FIREBALLS)) ClickGenes.shootFireball(event)
    }

}