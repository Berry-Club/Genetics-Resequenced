package dev.aaronhowser.mods.geneticsresequenced.event.player

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.behavior.TickGenes
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.packet.ModPacketHandler
import dev.aaronhowser.mods.geneticsresequenced.packet.server_to_client.SetGenesPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.entity.player.PlayerEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID
)
object OtherPlayerEvents {

	@SubscribeEvent
	fun onPlayerTick(event: TickEvent.PlayerTickEvent) {
		if (event.phase != TickEvent.Phase.START) return

		TickGenes.handleNoHunger(event.player)
		OtherGenes.handleWallClimbing(event.player)     // Requires clientside handling
		TickGenes.handleItemMagnet(event.player)
		TickGenes.handleXpMagnet(event.player)
	}

	@SubscribeEvent
	fun onPickUpItem(event: PlayerEvent.ItemPickupEvent) {
		val originalItemEntity = event.originalEntity
		val originalStack = originalItemEntity.item
		val player = event.entity

		if (originalStack.isSyringe()) {
			val thrower = originalItemEntity.owner as? LivingEntity

			player.hurt(
				SyringeItem.damageSourceStepOnSyringe(player.level(), thrower),
				1.0f
			)

			if (SyringeItem.isContaminated(originalStack)) {
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

		val packet = SetGenesPacket(entity.id, entity.geneHolders)
		ModPacketHandler.messagePlayer(packet, player)
	}

}