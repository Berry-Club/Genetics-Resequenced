package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component

object RemoveGeneCommand : Command<CommandSourceStack> {

    private const val GENE_ARGUMENT = "gene"
    private const val TARGET_ARGUMENT = "target"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands.argument(TARGET_ARGUMENT, EntityArgument.entity())
                    .then(
                        Commands.argument(GENE_ARGUMENT, StringArgumentType.string())
                            .suggests { ctx, builder ->
                                SharedSuggestionProvider.suggest(
                                    listOf("_all").plus(EnumGenes.values().map { it.name }),
                                    builder
                                )
                            }
                            .executes(RemoveGeneCommand)
                    )
            )
    }

    @Throws(CommandSyntaxException::class)
    override fun run(context: CommandContext<CommandSourceStack>): Int {
        @Suppress("MoveVariableDeclarationIntoWhen", "RedundantSuppression")
        val geneArgument = StringArgumentType.getString(context, GENE_ARGUMENT)

        return when (geneArgument) {
            "_all" -> removeAll(context)
            else -> removeGene(context, geneArgument)
        }
    }

    private fun removeGene(context: CommandContext<CommandSourceStack>, geneArgument: String): Int {
        val player = context.source.player ?: return 0

        val geneToRemove = EnumGenes.valueOf(geneArgument)
        val playerGenes = player.getGenes() ?: return 0

        val success = playerGenes.removeGene(geneToRemove)

        @Suppress("LiftReturnOrAssignment")
        if (success) {
            context.source.sendSuccess(
                Component.literal("Removed gene: ${geneToRemove.description}"),
                false
            )
            return 1
        } else {
            context.source.sendFailure(
                Component.literal("Failed to remove gene: ${geneToRemove.description}")
            )
            return 0
        }
    }

    private fun removeAll(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player ?: return 0
        val playerGenes = player.getGenes() ?: return 0

        playerGenes.removeAllGenes()

        context.source.sendSuccess(
            Component.literal("Removed all genes!"),
            false
        )
        return 1
    }

}