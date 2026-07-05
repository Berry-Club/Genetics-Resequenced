package dev.aaronhowser.mods.genetics_resequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.getActiveGenes
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.genetics_resequenced.command.ModCommands
import dev.aaronhowser.mods.genetics_resequenced.command.hasGamemasterPermission
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.IdentifierArgument
import net.minecraft.commands.arguments.selector.EntitySelector
import net.minecraft.core.Holder
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import java.util.concurrent.CompletableFuture

object RemoveGeneCommand : AaronCommandHelper {

	private const val GENE = "gene"
	private const val TARGETS = "targets"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("remove") {
			requires { it.hasGamemasterPermission() }

			thenArgument(TARGETS, EntityArgument.entities()) {
				thenArgument(GENE, IdentifierArgument.id()) {
					suggests(ModCommands::getGeneSuggestions)

					executes { cmd ->
						val source = cmd.source
						val geneRl = IdentifierArgument.getId(cmd, GENE)
						val entities = EntityArgument.getEntities(cmd, TARGETS)
						removeGene(source, geneRl, entities)
					}
				}
			}
		}
	}

	//FIXME: For some reason the context doesn't have the arguments when this is called, which is sad
	private fun getGeneSuggestions(
		context: CommandContext<CommandSourceStack>,
		suggestionsBuilder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val targets = context.getArgument(TARGETS, EntitySelector::class.java)
			.findEntities(context.source)
			.filterIsInstance<LivingEntity>()

		val genesHeldByMobs = targets
			.flatMap { it.getActiveGenes() }
			.mapNotNull { it.key?.identifier() }

		return SharedSuggestionProvider.suggestResource(genesHeldByMobs, suggestionsBuilder)
	}

	private fun removeGene(
		source: CommandSourceStack,
		geneRl: Identifier,
		targets: Collection<Entity>
	): Int {
		val geneHolder = ModGenes.fromIdentifier(source.registryAccess(), geneRl)
			?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

		val actualTargets = targets.filterIsInstance<LivingEntity>()
		if (actualTargets.isEmpty()) return 0

		if (actualTargets.size == 1) {
			handleSingleTarget(source, actualTargets.first(), geneHolder)
		} else {
			handleMultipleTargets(source, actualTargets, geneHolder)
		}

		return 1
	}

	private fun handleSingleTarget(
		source: CommandSourceStack,
		target: LivingEntity,
		geneHolder: Holder<Gene>
	) {
		val success = target.removeGene(geneHolder)

		if (success) {
			source.sendSuccess(
				{
					ModMessageLang.Commands.REMOVE_SINGLE_SUCCESS.toComponent(
						geneHolder.getName(),
						target.displayName
					)
				},
				false
			)

			return
		}

		source.sendFailure(
			ModMessageLang.Commands.REMOVE_SINGLE_FAIL.toComponent(
				geneHolder.getName(),
				target.displayName
			)
		)
	}

	private fun handleMultipleTargets(
		source: CommandSourceStack,
		targets: List<LivingEntity>,
		geneHolder: Holder<Gene>
	) {
		var amountSuccess = 0
		var amountFail = 0

		for (target in targets) {
			val success = target.removeGene(geneHolder)
			if (success) amountSuccess++ else amountFail++
		}

		if (amountSuccess != 0) {
			source.sendSuccess(
				{
					ModMessageLang.Commands.REMOVE_MULTIPLE_SUCCESS.toComponent(
						geneHolder.getName(),
						amountSuccess
					)
				},
				true
			)
		}

		if (amountFail != 0) {
			source.sendFailure(
				ModMessageLang.Commands.REMOVE_MULTIPLE_FAIL.toComponent(
					geneHolder.getName(),
					amountFail
				)
			)
		}
	}

}
