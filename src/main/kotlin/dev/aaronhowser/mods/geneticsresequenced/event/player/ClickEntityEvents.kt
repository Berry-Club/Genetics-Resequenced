package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.ClickGenes
import net.minecraft.world.entity.LivingEntity
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

        val target = event.target
        if (target !is LivingEntity) return

        val targetGenes = target.getGenes() ?: return

        if (targetGenes.hasGene(EnumGenes.WOOLY)) ClickGenes.wooly(event)
        if (targetGenes.hasGene(EnumGenes.MILKY)) ClickGenes.milky(event)
        if (targetGenes.hasGene(EnumGenes.MEATY)) ClickGenes.meaty(event)
    }

}