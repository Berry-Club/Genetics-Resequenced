package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.command.gene.*
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object ModCommands : AaronCommandHelper {

	fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {

		val command = literal(GeneticsResequenced.MOD_ID) {
			then(ClearBioGlowCommand.register())

			thenLiteral("gene") {
				then(ListGenesCommand.register())
				then(ListAllGenesCommand.register())
				then(GiveGeneCommand.register())
				then(GiveTemporaryGeneCommand.register())
				then(GiveAllGenesCommand.register())
				then(RemoveGeneCommand.register())
				then(RemoveAllGenesCommand.register())
			}
		}

		val primary = dispatcher.register(command)

		dispatcher.register(Commands.literal("genetics").redirect(primary))
		dispatcher.register(Commands.literal("gr").redirect(primary))
	}

	val SUGGEST_GENE_RLS: SuggestionProvider<CommandSourceStack> =
		SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
			val allGeneResourceLocations = ModGenes
				.getRegistrySorted(context.source.registryAccess())
				.mapNotNull { it.key?.location() }

			SharedSuggestionProvider.suggestResource(allGeneResourceLocations, suggestionsBuilder)
		}

}