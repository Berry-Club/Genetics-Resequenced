package dev.aaronhowser.mods.geneticsresequenced.gene.behavior

import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.permanentGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.registry.ModAttributes
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent.PlayerRespawnEvent

object AttributeGenes {

	fun handleEfficiency(event: PlayerEvent.BreakSpeed) {
		val efficiencyAttribute = event.entity.attributes.getInstance(ModAttributes.EFFICIENCY) ?: return
		if (efficiencyAttribute.value <= 0.0) return

		event.newSpeed += (1 + efficiencyAttribute.value * efficiencyAttribute.value).toFloat()
	}

	fun returnModifiersOnDeath(event: PlayerRespawnEvent) {
		val player = event.entity

		for (geneHolder in player.permanentGeneHolders) {
			geneHolder.value().setAttributeModifiers(player, true)
		}
	}

	fun respawnWithMaxHealth(event: PlayerRespawnEvent) {
		val player = event.entity
		player.health = player.maxHealth
	}

}