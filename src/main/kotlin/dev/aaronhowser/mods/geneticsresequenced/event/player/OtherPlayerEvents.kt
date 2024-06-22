package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.InventoryListener
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherPlayerEvents {

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {
        TickGenes.handleNoHunger(event.entity)
        TickGenes.handleMobSight(event.entity)
//        AttributeGenes.handleWallClimbing(event.entity)     // Requires clientside handling
        TickGenes.handleItemMagnet(event.entity)
        TickGenes.handleXpMagnet(event.entity)
    }

    @SubscribeEvent
    fun onPickUpItem(event: ItemEntityPickupEvent) {
        val stack = event.itemEntity
        val player = event.player

        if (stack.item.item == ModItems.SYRINGE.get()) {
            val thrower = event.itemEntity.owner

//            player.hurt(SyringeItem.damageSourceStepOnSyringe(thrower), 1.0f)
        }
    }

    @SubscribeEvent
    fun onLogIn(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return

        InventoryListener.startListening(player)
    }

}