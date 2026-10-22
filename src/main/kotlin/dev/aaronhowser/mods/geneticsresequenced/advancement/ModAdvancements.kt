package dev.aaronhowser.mods.geneticsresequenced.advancement

import dev.aaronhowser.mods.aaron.registry.actual.AaronCriterionTriggers
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player

object ModAdvancements {

	val ROOT = guide("root")
	val CELL_ANALYZER = guide("cell_analyzer")
	val DNA_EXTRACTOR = guide("dna_extractor")
	val DNA_DECRYPTOR = guide("dna_decryptor")
	val DECRYPT_DNA = guide("decrypt_dna")
	val BLACK_DEATH = guide("black_death")
	val PLASMID_INFUSER = guide("plasmid_infuser")
	val PLASMID_INJECTOR = guide("plasmid_injector")
	val GET_GENE = guide("get_gene")
	val GET_FLIGHT = guide("get_flight")
	val GET_ALL_SCARE_GENES = guide("get_all_scare_genes")
	val GET_CRINGE = guide("get_cringe")
	val GET_MILKED = guide("get_milked")
	val TRIGGER_SLIMY_DEATH = guide("trigger_slimy_death")
	val SYRINGE = guide("syringe")
	val BLOOD_PURIFIER = guide("blood_purifier")

	fun trigger(player: Player, action: ResourceLocation) {
		if (player !is ServerPlayer) return

		AaronCriterionTriggers.PLAYER_ACTION.get().trigger(player, action)
	}

	fun triggerGeneAdded(player: ServerPlayer, geneHolder: Holder<Gene>) {
		trigger(player, GET_GENE)
		triggerGene(player, geneHolder)
	}

	fun triggerCurrentGenes(player: ServerPlayer) {
		val geneHolders = player.permanentGeneHolders
		if (geneHolders.isEmpty()) return

		trigger(player, GET_GENE)

		for (geneHolder in geneHolders) {
			triggerGene(player, geneHolder)
		}
	}

	private fun triggerGene(player: ServerPlayer, geneHolder: Holder<Gene>) {
		val geneKey = geneHolder.unwrapKey().orElse(null) ?: return
		trigger(player, geneKey.location())
	}

	private fun guide(path: String): ResourceLocation {
		return GeneticsResequenced.modResource("guide/$path")
	}

}