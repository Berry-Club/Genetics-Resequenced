package dev.aaronhowser.mods.genetics_resequenced.event.player

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.OtherGenes
import dev.aaronhowser.mods.genetics_resequenced.item.SyringeItem
import dev.aaronhowser.mods.genetics_resequenced.item.SyringeItem.Companion.isContaminated
import dev.aaronhowser.mods.genetics_resequenced.packet.server_to_client.SetGenesPacket
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

@EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID
)
object OtherPlayerEvents {

	@SubscribeEvent
	fun onPickUpItem(event: ItemEntityPickupEvent.Post) {
		val originalStack = event.originalStack
		val player = event.player

		if (originalStack.isItem(ModItemTagsProvider.SYRINGES)) {
			val thrower = event.itemEntity.owner as? LivingEntity

			player.hurt(SyringeItem.getStepOnSyringeDamageSource(event.player.level(), thrower), 1.0f)

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

		val packet = SetGenesPacket(entity.id, entity.permanentGeneHolders)
		packet.messagePlayer(player)
	}

}