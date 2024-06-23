package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object ModCommands {

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {

        val modCommands = dispatcher.register(
            Commands.literal(GeneticsResequenced.ID)
                .then(AddGeneCommand.register())
                .then(AddAllGenesCommand.register())
                .then(RemoveGeneCommand.register())
                .then(RemoveAllGenesCommand.register())
        )

    }

    val SUGGEST_GENE_RLS: SuggestionProvider<CommandSourceStack> =
        SuggestionProvider { _: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
            val allGeneResourceLocations = Gene.getRegistry().filter { !it.isHidden }.map { it.id }
            SharedSuggestionProvider.suggestResource(allGeneResourceLocations, suggestionsBuilder)
        }

}