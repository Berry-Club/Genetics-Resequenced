package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.entity.SupportSlime
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.ClickGenes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingGetProjectileEvent
import net.neoforged.neoforge.event.entity.player.ArrowLooseEvent
import net.neoforged.neoforge.event.entity.player.ArrowNockEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object ClickEvents {

    @SubscribeEvent
    fun onInteractEntity(event: PlayerInteractEvent.EntityInteract) {
        ClickGenes.handleWooly(event)
        ClickGenes.handleMilky(event)
        ClickGenes.handleMeaty(event)
    }

    @SubscribeEvent
    fun onUseItem(event: PlayerInteractEvent.RightClickItem) {
        ClickGenes.woolyItem(event)
        ClickGenes.milkyItem(event)
        ClickGenes.meatyItem(event)
        ClickGenes.shootFireball(event)
    }

    @SubscribeEvent
    fun onDigSpeed(event: PlayerEvent.BreakSpeed) {
        AttributeGenes.handleEfficiency(event)
    }

    @SubscribeEvent
    fun onClickBlock(event: PlayerInteractEvent.RightClickBlock) {
        SupportSlime.spawnEggMessage(event)
    }

    @SubscribeEvent
    fun onInteractWithBlock(event: PlayerInteractEvent.RightClickBlock) {
        ClickGenes.eatGrass(event)
        ClickGenes.cureCringe(event)
    }

    @SubscribeEvent
    fun onGetProjectile(event: LivingGetProjectileEvent) {
        ClickGenes.handleInfinityGetProjectile(event)
    }

}