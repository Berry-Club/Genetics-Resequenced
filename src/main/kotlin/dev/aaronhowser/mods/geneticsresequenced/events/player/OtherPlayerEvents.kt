package dev.aaronhowser.mods.geneticsresequenced.events.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.ClickGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.OtherGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherPlayerEvents {

    @SubscribeEvent
    fun onInteractWithBlock(event: PlayerInteractEvent.RightClickBlock) {
        ClickGenes.eatGrass(event)
        ClickGenes.cureCringe(event)
    }

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent) {
        TickGenes.handleNoHunger(event.player)
        TickGenes.handleMobSight(event.player)
        AttributeGenes.handleWallClimbing(event.player)     // Requires clientside handling
        TickGenes.handleItemMagnet(event.player)
        TickGenes.handleXpMagnet(event.player)
    }

    @SubscribeEvent
    fun onArrowNock(event: ArrowNockEvent) {
        ClickGenes.handleInfinityStart(event)
    }

    @SubscribeEvent
    fun onArrowLoose(event: ArrowLooseEvent) {
        ClickGenes.handleInfinityEnd(event)
    }

    @SubscribeEvent
    fun onPickUpItem(event: ItemPickupEvent) {
        val item = event.stack

        if (item.`is`(ModItems.SYRINGE.get())) {
            val thrower = event.originalEntity.throwingEntity as? Player

            event.entity.hurt(SyringeItem.damageSourceStepOnSyringe(thrower), 1.0f)
        }

    }

    @SubscribeEvent
    fun onSendChatMessage(event: ServerChatEvent.Submitted) {
        OtherGenes.handleEmeraldHeart(event)
        OtherGenes.handleChatterbox(event)
        OtherGenes.handleCringeChat(event)
    }

    @SubscribeEvent
    fun onStartTracking(event: PlayerEvent.StartTracking) {
        val player = event.entity as? ServerPlayer ?: return

        val entity = event.target as? LivingEntity ?: return
        val genes = entity.getGenes() ?: return

        for (gene in genes.getGeneList()) {
            ModPacketHandler.messagePlayer(
                player,
                GeneChangedPacket(
                    entity.id,
                    gene.id,
                    true
                )
            )
        }
    }

}