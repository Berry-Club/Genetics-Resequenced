package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.getGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.ClickGenes
import dev.aaronhowser.mods.geneticsresequenced.genebehavior.TickGenes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
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

        ClickGenes.eatGrass(event)
    }

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {
        if (event.side.isClient) return

        TickGenes.handleNoHunger(event.player)
        TickGenes.handleFlight(event.player)
    }

    @SubscribeEvent
    fun onArrowNock(event: ArrowNockEvent) {
        ClickGenes.handleInfinityStart(event)
    }

    @SubscribeEvent
    fun onArrowLoose(event: ArrowLooseEvent) {
        ClickGenes.handleInfinityEnd(event)
    }


    fun genesChanged(entity: LivingEntity, changedGene: Gene, wasAdded: Boolean) {

        if (entity is Player) {
            when (changedGene) {
                Gene.EFFICIENCY -> {
                    val entityGenes = entity.getGenes() ?: return
                    if (entityGenes.hasGene(Gene.EFFICIENCY_4)) return
                    AttributeGenes.setEfficiency(entity, if (wasAdded) 1 else 0)
                }

                Gene.EFFICIENCY_4 -> {
                    if (wasAdded) {
                        AttributeGenes.setEfficiency(entity, 4)
                    } else {
                        val entityGenes = entity.getGenes() ?: return
                        val levelToSetTo = if (entityGenes.hasGene(Gene.EFFICIENCY)) 1 else 0
                        AttributeGenes.setEfficiency(entity, levelToSetTo)
                    }
                }

                Gene.STEP_ASSIST -> AttributeGenes.setStepAssist(entity, wasAdded)

                Gene.WALL_CLIMBING -> AttributeGenes.setWallClimbing(entity, wasAdded)

                else -> return
            }
        }

    }

}