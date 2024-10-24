package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHidden
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object ModCommands {

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        dispatcher.register(
            Commands.literal(GeneticsResequenced.ID)
                .then(GeneCommand.register())
                .then(ClearBioluminescenceBlocksCommand.register())
        )
    }

    val SUGGEST_GENE_RLS: SuggestionProvider<CommandSourceStack> =
        SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
            val allGeneResourceLocations = ModGenes
                .getRegistrySorted(context.source.registryAccess())
                .filter { !it.isHidden }.map { it.key!!.location() }

            SharedSuggestionProvider.suggestResource(allGeneResourceLocations, suggestionsBuilder)
        }

    val SUGGEST_GENE_STRINGS: SuggestionProvider<CommandSourceStack> =
        SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
            val allGeneStrings = ModGenes
                .getRegistrySorted(context.source.registryAccess())
                .filter { !it.isHidden }.map { it.key!!.location().path.toString() }

            SharedSuggestionProvider.suggest(allGeneStrings, suggestionsBuilder)
        }

}