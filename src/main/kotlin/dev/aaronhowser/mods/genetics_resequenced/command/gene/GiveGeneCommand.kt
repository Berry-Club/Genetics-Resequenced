package dev.aaronhowser.mods.genetics_resequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.genetics_resequenced.command.ModCommands.SUGGEST_GENES
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.IdentifierArgument
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object GiveGeneCommand {

	private const val GENE_ARGUMENT = "gene"
	private const val TARGET_ARGUMENT = "targets"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("give-gene")
			.requires { it.hasPermission(2) }
			.then(
				Commands
					.argument(GENE_ARGUMENT, IdentifierArgument.id())
					.suggests(SUGGEST_GENES)
					.executes { cmd ->
						addGene(
							cmd.source,
							IdentifierArgument.getId(cmd, GENE_ARGUMENT),
							entities = listOf(cmd.source.playerOrException)
						)
					}
					.then(
						Commands
							.argument(TARGET_ARGUMENT, EntityArgument.entities())
							.executes { cmd ->
								addGene(
									cmd.source,
									IdentifierArgument.getId(cmd, GENE_ARGUMENT),
									EntityArgument.getEntities(cmd, TARGET_ARGUMENT)
								)
							}
					)
			)
	}

	private fun addGene(
		source: CommandSourceStack,
		geneRl: Identifier,
		entities: Collection<Entity>
	): Int {
		val gene = ModGenes.fromIdentifier(source.registryAccess(), geneRl)
			?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

		return addGene(source, gene, entities)
	}

	private fun addGene(
		source: CommandSourceStack,
		geneToAdd: Holder<Gene>,
		entities: Collection<Entity>
	): Int {
		val targets = entities.mapNotNull { it as? LivingEntity }

		if (targets.isEmpty()) {
			source.sendFailure(Component.literal("No valid living entity targets found!"))
			return 0
		}

		if (targets.size == 1) {
			handleSingleTarget(source, targets.first(), geneToAdd)
		} else {
			handleMultipleTargets(source, targets, geneToAdd)
		}

		return 1
	}

	private fun handleSingleTarget(
		source: CommandSourceStack,
		target: LivingEntity,
		geneHolder: Holder<Gene>,
	) {
		val success = addGeneToTarget(target, geneHolder)

		if (success) {
			source.sendSuccess(
				{
					ModLanguageProvider.Commands.ADD_SINGLE_SUCCESS.toComponent(
						geneHolder.getName(),
						target.name
					)
				},
				false
			)
		} else {
			source.sendFailure(
				ModLanguageProvider.Commands.ADD_SINGLE_FAIL.toComponent(
					geneHolder.getName(),
					target.name
				)
			)
		}
	}

	private fun handleMultipleTargets(
		source: CommandSourceStack,
		targets: List<LivingEntity>,
		geneHolder: Holder<Gene>
	) {
		var amountSuccess = 0
		var amountFail = 0

		for (target in targets) {
			val success = addGeneToTarget(target, geneHolder)
			if (success) amountSuccess++ else amountFail++
		}

		if (amountSuccess != 0) {
			source.sendSuccess(
				{
					ModLanguageProvider.Commands.ADD_MULTIPLE_SUCCESS.toComponent(
						geneHolder.getName(),
						amountSuccess
					)
				},
				false
			)
		}

		if (amountFail != 0) {
			source.sendFailure(
				ModLanguageProvider.Commands.ADD_MULTIPLE_FAIL.toComponent(
					geneHolder.getName(),
					amountFail
				)
			)
		}
	}

	private fun addGeneToTarget(
		target: LivingEntity,
		geneHolder: Holder<Gene>,
	): Boolean {
		val alreadyHasGene = target.hasGene(geneHolder)
		if (alreadyHasGene) {
			GeneticsResequenced.LOGGER.info("Tried to add gene ${geneHolder.key!!.identifier()} to ${target.name.string}, but they already have it!")
			return false
		}

		if (!geneHolder.value().canEntityHave(target)) {
			GeneticsResequenced.LOGGER.info("Tried to add gene ${geneHolder.key!!.identifier()} to ${target.name.string}, but they can't have it!")
			return false
		}

		val success = target.addGene(geneHolder)

		return success
	}

}