package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isContaminated
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.ServerChatEvent
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(
    modid = GeneticsResequenced.ID
)
object OtherPlayerEvents {

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent.Pre) {
        TickGenes.handleNoHunger(event.entity)
        TickGenes.handleMobSight(event.entity)
        AttributeGenes.handleWallClimbing(event.entity)     // Requires clientside handling
        TickGenes.handleItemMagnet(event.entity)
        TickGenes.handleXpMagnet(event.entity)
    }

    @SubscribeEvent
    fun onPickUpItem(event: ItemEntityPickupEvent.Post) {
        val originalStack = event.originalStack
        val player = event.player

        if (originalStack.isSyringe()) {
            val thrower = event.itemEntity.owner as? LivingEntity

            player.hurt(SyringeItem.damageSourceStepOnSyringe(event.player.level(), thrower), 1.0f)

            if (isContaminated(originalStack)) {
                player.addEffect(MobEffectInstance(MobEffects.POISON, 20 * 3))
            }
        }
    }

    @SubscribeEvent
    fun onLogIn(event: PlayerEvent.PlayerLoggedInEvent) {
        GenesData.syncPlayer(event.entity)
    }

    @SubscribeEvent
    fun onChangeDimension(event: PlayerEvent.PlayerChangedDimensionEvent) {
        GenesData.syncPlayer(event.entity)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        GenesData.syncPlayer(event.entity)
    }

    @SubscribeEvent
    fun onSendChatMessage(event: ServerChatEvent) {
        OtherGenes.handleEmeraldHeart(event)
        OtherGenes.handleCringeChat(event)
        OtherGenes.handleChatterbox(event)
        OtherGenes.handleSlimyChat(event)
    }

    @SubscribeEvent
    fun onStartTracking(event: PlayerEvent.StartTracking) {
        val player = event.entity as? ServerPlayer ?: return
        val entity = event.target as? LivingEntity ?: return

        for (gene in entity.geneHolders) {
            ModPacketHandler.messagePlayer(
                player,
                GeneChangedPacket(
                    entity.id,
                    gene.key!!.location(),
                    true
                )
            )
        }
    }

}