package dev.aaronhowser.mods.genetics_resequenced.item

import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.attachment.TemporaryGenesData
import dev.aaronhowser.mods.genetics_resequenced.attachment.TemporaryGenesData.Companion.temporaryGenes
import dev.aaronhowser.mods.genetics_resequenced.data.EntityGenes
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level

class GeneCheckerItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResult {
		if (!level.isClientSide) {
			val targetEntity = OtherUtil.getLookedAtEntity(player) ?: player

			tellHeldGenes(player, targetEntity)
			tellPossibleGenes(player, targetEntity)
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		private fun tellHeldGenes(player: Player, targetEntity: LivingEntity) {
			tellPermanentGenes(player, targetEntity)
			tellTemporaryGenes(player, targetEntity)
		}

		private fun tellPermanentGenes(player: Player, targetEntity: LivingEntity) {
			val targetGeneHolders = targetEntity.permanentGeneHolders

			val component = if (targetGeneHolders.isEmpty()) {
				if (targetEntity == player) {
					ModMessageLang.GENE_CHECKER_SELF_NO_GENES.toComponent()
				} else {
					ModMessageLang.GENE_CHECKER_TARGET_NO_GENES.toComponent(targetEntity.name)
				}
			} else {
				val genesComponent = OtherUtil.componentList(targetGeneHolders.map(Gene::getNameComponent))

				if (targetEntity == player) {
					ModMessageLang.GENE_CHECKER_SELF_LIST.toComponent(genesComponent)
				} else {
					ModMessageLang.GENE_CHECKER_TARGET_LIST.toComponent(targetEntity.name, genesComponent)
				}
			}

			player.sendSystemMessage(component)
		}

		private fun tellTemporaryGenes(player: Player, targetEntity: LivingEntity) {
			val tempGenes = targetEntity.temporaryGenes

			if (tempGenes.isEmpty()) {
				return
			}

			val componentList = tempGenes.map(TemporaryGenesData.TemporaryGene::getComponent)
			val listComponent = OtherUtil.componentList(componentList)

			val component = if (targetEntity == player) {
				ModMessageLang.GENE_CHECKER_SELF_TEMPORARY_LIST.toComponent(listComponent)
			} else {
				ModMessageLang.GENE_CHECKER_TARGET_TEMPORARY_LIST.toComponent(targetEntity.name, listComponent)
			}

			player.sendSystemMessage(component)
		}

		private fun tellPossibleGenes(player: Player, targetEntityGenes: LivingEntity) {
			val possibleGenes = EntityGenes.getGeneHolderWeights(targetEntityGenes.type, player.registryAccess())

			if (possibleGenes.isEmpty()) {
				player.sendSystemMessage(
					ModMessageLang.GENE_CHECKER_NO_POSSIBLE_GENES.toComponent(targetEntityGenes.name)
				)
				return
			}

			val genesComponent = Component.empty()

			val entries = possibleGenes.toList()
			for ((i, entry) in entries.withIndex()) {
				val (geneHolder, weight) = entry

				genesComponent
					.append("•  ")
					.append(ModMessageLang.GENE_WEIGHT.toComponent(geneHolder.getName(), weight))

				if (i != entries.lastIndex) {
					genesComponent.append("\n")
				}
			}

			player.sendSystemMessage(
				ModMessageLang.GENE_CHECKER_POSSIBLE_GENES.toComponent(targetEntityGenes.name, genesComponent)
			)
		}

	}

}
