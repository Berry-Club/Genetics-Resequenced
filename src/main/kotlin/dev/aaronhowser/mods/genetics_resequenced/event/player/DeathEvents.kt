package dev.aaronhowser.mods.genetics_resequenced.event.player

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.removeAllGenes
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.genetics_resequenced.config.ServerConfig
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.AttributeGenes
import dev.aaronhowser.mods.genetics_resequenced.gene.behavior.DeathGenes
import net.minecraft.ChatFormatting
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent

@EventBusSubscriber(
	modid = GeneticsResequenced.MOD_ID
)
object DeathEvents {

	@SubscribeEvent(
		priority = EventPriority.HIGHEST    //So that graves etc don't dupe contents if you have the save inventory gene
	)
	fun keepInventory(event: LivingDeathEvent) {
		if (event.isCanceled) return
		val entity = event.entity

		if (entity is Player) {
			DeathGenes.saveInventory(entity)
		}
	}

	@SubscribeEvent(
		priority = EventPriority.LOWEST     //In case something else cancels the event after it was saved
	)
	fun keepInventoryFailsafe(event: LivingDeathEvent) {
		val player = event.entity as? Player ?: return
		if (event.isCanceled) DeathGenes.returnInventory(player)
	}


	@SubscribeEvent
	fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
		DeathGenes.returnInventory(event.entity)

		handleKeepGenesOnDeath(event)
		removeNegativeGenesOnDeath(event)
		AttributeGenes.returnModifiersOnDeath(event)
		AttributeGenes.respawnWithMaxHealth(event)
	}

	private fun handleKeepGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
		if (ServerConfig.CONFIG.keepGenesOnDeath.get()) return

		val player = event.entity
		val playerGenes = player.permanentGeneHolders

		if (playerGenes.isEmpty()) return

		val component =
			ModMessageLang.DEATH_GENE_REMOVAL.toComponent()
				.withStyle(ChatFormatting.GRAY)

		player.sendSystemMessage(component)
		player.removeAllGenes()
	}

	private fun removeNegativeGenesOnDeath(event: PlayerEvent.PlayerRespawnEvent) {
		val player = event.entity
		val playerGeneHolders = player.permanentGeneHolders

		if (playerGeneHolders.isEmpty()) return

		val negativeGenes = playerGeneHolders.filter { it.isNegative }
		if (negativeGenes.isEmpty()) return

		val component =
			ModMessageLang.DEATH_NEGATIVE_GENE_REMOVAL.toComponent()
				.withStyle(ChatFormatting.GRAY)

		player.sendSystemMessage(component)

		for (gene in negativeGenes) {
			player.removeGene(gene)
		}
	}

}