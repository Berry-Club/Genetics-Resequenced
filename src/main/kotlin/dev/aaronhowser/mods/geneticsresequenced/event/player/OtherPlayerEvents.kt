package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
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


    fun genesChanged(entity: LivingEntity, changedGene: EnumGenes, wasAdded: Boolean) {

        if (entity is Player) {
            when (changedGene) {
                EnumGenes.EFFICIENCY -> ClickGenes.setEfficiency(entity, if (wasAdded) 1 else 0)
                EnumGenes.EFFICIENCY_4 -> ClickGenes.setEfficiency(entity, if (wasAdded) 4 else 0)

                else -> return
            }
        }

    }

}