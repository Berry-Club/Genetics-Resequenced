package dev.aaronhowser.mods.geneticsresequenced.events.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.advancements.AdvancementTriggers
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.events.CustomEvents
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.AttributeGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.ClickGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.OtherGenes
import dev.aaronhowser.mods.geneticsresequenced.genes.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.packets.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packets.server_to_client.GeneChangedPacket
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.InventoryListener
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.TickEvent.PlayerTickEvent
import net.minecraftforge.event.entity.player.ArrowLooseEvent
import net.minecraftforge.event.entity.player.ArrowNockEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent
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
        val stack = event.stack
        val player = event.entity

        if (stack.item == ModItems.SYRINGE.get()) {
            val thrower = event.originalEntity.throwingEntity

            player.hurt(SyringeItem.damageSourceStepOnSyringe(thrower), 1.0f)
        }
    }

    @SubscribeEvent
    fun onLogIn(event: PlayerLoggedInEvent) {
        val player = event.entity as? ServerPlayer ?: return

        InventoryListener.startListening(player)
    }

    @SubscribeEvent
    fun onLogOut(event: PlayerEvent.PlayerLoggedOutEvent) {
        val player = event.entity as? ServerPlayer ?: return

        InventoryListener.stopListening(player)
    }

    @SubscribeEvent
    fun onSendChatMessage(event: ServerChatEvent.Submitted) {
        OtherGenes.handleEmeraldHeart(event)
        OtherGenes.handleCringeChat(event)
        OtherGenes.handleChatterbox(event)
        OtherGenes.handleSlimyChat(event)
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

    @SubscribeEvent
    fun onInventoryChange(event: CustomEvents.PlayerInventoryChangeEvent) {
        val (player: ServerPlayer, slot: Int, stack: ItemStack) = event

        AdvancementTriggers.decryptDnaAdvancement(player, stack)
    }

}