package dev.aaronhowser.mods.genetics_resequenced.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getLocationOrNull
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.command.gene.*
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object ModCommands {

	private val commandBaseStrings = listOf(
		GeneticsResequenced.MOD_ID,
		"genetics",
		"gr"
	)

	fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
		for (commandBaseString in commandBaseStrings) {
			dispatcher.register(
				Commands.literal(commandBaseString)
					.then(ClearBioGlowCommand.register())
					.then(
						Commands.literal("gene")
							.then(ListGenesCommand.register())
							.then(ListAllGenesCommand.register())
							.then(GiveGeneCommand.register())
							.then(GiveAllGenesCommand.register())
							.then(RemoveGeneCommand.register())
							.then(RemoveAllGenesCommand.register())
					)
			)
		}
	}

	val SUGGEST_GENE_RLS: SuggestionProvider<CommandSourceStack> =
		SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
			val allGeneResourceLocations = ModGenes
				.getRegistrySorted(context.source.registryAccess())
				.mapNotNull { it.getLocationOrNull() }

			SharedSuggestionProvider.suggestResource(allGeneResourceLocations, suggestionsBuilder)
		}

	val SUGGEST_GENE_STRINGS: SuggestionProvider<CommandSourceStack> =
		SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
			val allGeneStrings = ModGenes
				.getRegistrySorted(context.source.registryAccess())
				.mapNotNull { it.getLocationOrNull()?.path }

			SharedSuggestionProvider.suggest(allGeneStrings, suggestionsBuilder)
		}

}