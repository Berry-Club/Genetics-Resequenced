package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancement.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.event.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isContaminated
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.util.InventoryListener
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
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

        if (originalStack.item.isSyringe()) {
            val thrower = event.itemEntity.owner as? LivingEntity

            player.hurt(SyringeItem.damageSourceStepOnSyringe(event.player.level(), thrower), 1.0f)

            if (isContaminated(originalStack)) {
                player.addEffect(MobEffectInstance(MobEffects.POISON, 20 * 3))
            }
        }
    }

    @SubscribeEvent
    fun onLogIn(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return

        InventoryListener.startListening(player)

        for (gene in player.geneHolders) {
            ModPacketHandler.messagePlayer(
                player,
                GeneChangedPacket(
                    player.id,
                    gene.id,
                    true
                )
            )
        }
    }

    @SubscribeEvent
    fun onLogOut(event: PlayerEvent.PlayerLoggedOutEvent) {
        val player = event.entity as? ServerPlayer ?: return

        InventoryListener.stopListening(player)
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
        val genes = entity.geneHolders

        for (gene in genes) {
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

    @SubscribeEvent
    fun onInventoryChange(event: CustomEvents.PlayerInventoryChangeEvent) {
        val (player: ServerPlayer, slot: Int, stack: ItemStack) = event

        AdvancementTriggers.decryptDnaAdvancement(player, stack)
        AdvancementTriggers.blackDeath(player, stack)
    }


}