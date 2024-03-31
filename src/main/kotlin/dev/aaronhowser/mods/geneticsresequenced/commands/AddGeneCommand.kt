package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.getGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component


object AddGeneCommand : Command<CommandSourceStack> {

    private const val GENE_ARGUMENT = "gene"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands.argument(GENE_ARGUMENT, StringArgumentType.string())
                    .suggests { ctx, builder ->
                        SharedSuggestionProvider.suggest(
                            EnumGenes.values().map { it.name },
                            builder
                        )
                    }
                    .executes(AddGeneCommand)
            )
    }

    @Throws(CommandSyntaxException::class)
    override fun run(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player ?: return 0

        val geneArgument = StringArgumentType.getString(context, GENE_ARGUMENT)
        val gene = EnumGenes.valueOf(geneArgument)

        val genes = player.getGenes()

        val success = genes.addGene(gene)

        @Suppress("LiftReturnOrAssignment")
        if (success) {
            context.source.sendSuccess(
                Component.literal("Added gene: ${gene.description}"),
                false
            )
            return 1
        } else {
            context.source.sendFailure(
                Component.literal("Failed to add gene: ${gene.description}")
            )
            return 0
        }
    }


}