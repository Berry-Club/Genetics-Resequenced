package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.config.ServerConfig
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent

object AttributeGenes {

	fun handleEfficiency(event: PlayerEvent.BreakSpeed) {
		val efficiency = ModGenes.EFFICIENCY.getHolderOrThrow(event.entity.registryAccess())
		if (efficiency.isDisabled) return

		val efficiencyAttribute = event.entity.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
		if (efficiencyAttribute.value <= 0.0) return

		event.newSpeed += (1 + efficiencyAttribute.value * efficiencyAttribute.value).toFloat()
	}

	fun returnModifiersOnDeath(event: PlayerRespawnEvent) {
		val player = event.entity

		for (geneHolder in player.geneHolders) {
			geneHolder.value().setAttributeModifiers(player, true)
		}
	}

	fun respawnWithMaxHealth(event: PlayerRespawnEvent) {
		val player = event.entity
		player.health = player.maxHealth
	}

}