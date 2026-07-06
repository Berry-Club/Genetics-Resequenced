package dev.aaronhowser.mods.genetics_resequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.genetics_resequenced.command.ModCommands.hasGameMasterPermission
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object GiveAllGenesCommand : AaronCommandHelper {

	private const val TARGETS = "targets"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("give-all") {
			requires { it.hasGameMasterPermission() }

			executes {
				val source = it.source
				val targets = listOf(source.entityOrException)

				giveAllGenes(source, targets)
			}

			thenArgument(TARGETS, EntityArgument.entities()) {
				executes {
					val source = it.source
					val entities = EntityArgument.getEntities(it, TARGETS)

					giveAllGenes(source, entities)
				}
			}
		}
	}

	private fun giveAllGenes(
		source: CommandSourceStack,
		targets: Collection<Entity>
	): Int {
		val actualTargets = targets.filterIsInstance<LivingEntity>()
		if (actualTargets.isEmpty()) return 0

		if (actualTargets.size == 1) {
			handleSingleTarget(source, actualTargets.first())
		} else {
			handleMultipleTargets(source, actualTargets)
		}

		return 1
	}

	private fun handleMultipleTargets(
		source: CommandSourceStack,
		targets: List<LivingEntity>
	) {
		for (target in targets) {
			val genesToAdd =
				ModGenes
					.getAllGeneHolders(source.registryAccess())
					.filter { !it.isDisabled && !it.isNegative && it.value().canEntityHave(target) }

			for (gene in genesToAdd) {
				target.addGene(gene)
			}
		}

		source.sendSuccess(
			{
				ModMessageLang.Commands.ADD_ALL_MULTIPLE
					.toComponent(targets.size)
			},
			false
		)
	}

	private fun handleSingleTarget(
		source: CommandSourceStack,
		target: LivingEntity
	) {
		val genesToAdd =
			ModGenes
				.getAllGeneHolders(source.registryAccess())
				.filter { !it.isDisabled && !it.isNegative && it.value().canEntityHave(target) }

		for (gene in genesToAdd) {
			target.addGene(gene)
		}

		source.sendSuccess(
			{
				ModMessageLang.Commands.ADD_ALL_SINGLE
					.toComponent(target.name)
			},
			false
		)
	}

}
