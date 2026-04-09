package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.command.gene.*
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object ModCommands {

	private val commandBaseStrings = listOf(
		GeneticsResequenced.ID,
		"genetics",
		"gr"
	)

	fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
		for (commandBaseString in commandBaseStrings) {
			dispatcher.register(
				Commands.literal(commandBaseString)
					.then(ClearBioGlowCommand.register())
					.then(ListGenesCommand.register())
					.then(ListAllGenesCommand.register())
					.then(GiveGeneCommand.register())
					.then(GiveTemporaryGeneCommand.register())
					.then(GiveAllGenesCommand.register())
					.then(RemoveGeneCommand.register())
					.then(RemoveAllGenesCommand.register())
			)
		}
	}

	val SUGGEST_GENE_RLS: SuggestionProvider<CommandSourceStack> =
		SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
			val allGeneIdentifiers = ModGenes
				.getRegistrySorted(context.source.registryAccess())
				.map { it.key!!.location() }

			SharedSuggestionProvider.suggestResource(allGeneIdentifiers, suggestionsBuilder)
		}

	val SUGGEST_GENE_STRINGS: SuggestionProvider<CommandSourceStack> =
		SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
			val allGeneStrings = ModGenes
				.getRegistrySorted(context.source.registryAccess())
				.map { it.key!!.location().path.toString() }

			SharedSuggestionProvider.suggest(allGeneStrings, suggestionsBuilder)
		}

}