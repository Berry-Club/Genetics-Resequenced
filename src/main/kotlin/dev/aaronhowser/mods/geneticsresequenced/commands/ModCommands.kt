package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider

object ModCommands {

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val modCommands = dispatcher.register(
            Commands.literal(GeneticsResequenced.ID)
                .then(addGeneArgument())
        )

        dispatcher.register(Commands.literal("genes").redirect(modCommands))
    }

    fun addGeneArgument(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands.argument("gene", StringArgumentType.string())
                    .suggests { ctx, builder ->
                        SharedSuggestionProvider.suggest(
                            EnumGenes.values().map { it.name },
                            builder
                        )
                    }
            )
    }
}