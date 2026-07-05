package dev.aaronhowser.mods.genetics_resequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.genetics_resequenced.attachment.TemporaryGenesData
import dev.aaronhowser.mods.genetics_resequenced.attachment.TemporaryGenesData.Companion.temporaryGenes
import dev.aaronhowser.mods.genetics_resequenced.command.hasGamemasterPermission
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object ListGenesCommand : AaronCommandHelper {

	private const val TARGET_ARGUMENT = "target"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("list") {
			requires { it.hasGamemasterPermission() }

			executes {
				val source = it.source
				val target = source.entityOrException
				listGenes(source, target)
			}

			thenArgument(TARGET_ARGUMENT, EntityArgument.entity()) {
				executes {
					val source = it.source
					val target = EntityArgument.getEntity(it, TARGET_ARGUMENT)
					listGenes(source, target)
				}
			}
		}
	}

	private fun listGenes(
		source: CommandSourceStack,
		target: Entity
	): Int {
		if (target !is LivingEntity) {
			return 0
		}

		listPermanentGenes(source, target)
		listTemporaryGenes(source, target)

		return 1
	}

	private fun listPermanentGenes(
		source: CommandSourceStack,
		target: LivingEntity
	) {
		val targetGenesList = target.permanentGeneHolders

		if (targetGenesList.isEmpty()) {
			source.sendSuccess(
				{ ModMessageLang.Commands.NO_GENES.toComponent() },
				false
			)
			return
		}

		source.sendSuccess(
			{
				val messageComponent =
					ModMessageLang.Commands.TARGET_GENE_LIST.toComponent(
						target.displayName
					)

				messageComponent.append(
					OtherUtil.componentList(
						targetGenesList.map(Gene::getNameComponent)
					)
				)
			},
			false
		)
	}

	private fun listTemporaryGenes(
		source: CommandSourceStack,
		target: LivingEntity
	) {
		val tempGenes = target.temporaryGenes
		if (tempGenes.isEmpty()) {
			return
		}

		source.sendSuccess(
			{
				val messageComponent =
					ModMessageLang.Commands.TEMPORARY_GENE_LIST.toComponent(
						target.displayName
					)

				val componentList = tempGenes.map(TemporaryGenesData.TemporaryGene::getComponent)
				messageComponent.append(OtherUtil.componentList(componentList))
			},
			false
		)

	}

}
