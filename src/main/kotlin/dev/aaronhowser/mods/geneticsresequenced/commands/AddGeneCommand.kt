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
import net.minecraft.world.entity.LivingEntity


object AddGeneCommand : Command<CommandSourceStack> {

    private const val GENE_ARGUMENT = "gene"
    private const val TARGET_ARGUMENT = "target"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(TARGET_ARGUMENT, EntityArgument.entity())
                    .then(
                        Commands.argument(GENE_ARGUMENT, StringArgumentType.string())
                            .suggests { ctx, builder ->
                                SharedSuggestionProvider.suggest(
                                    listOf("_all").plus(EnumGenes.values().map { it.name }),
                                    builder
                                )
                            }
                            .executes(AddGeneCommand)
                    )
            )
    }

    @Throws(CommandSyntaxException::class)
    override fun run(context: CommandContext<CommandSourceStack>): Int {

        val geneArgument = StringArgumentType.getString(context, GENE_ARGUMENT)

        return when (geneArgument) {
            "_all" -> addAll(context)
            else -> addGene(context, geneArgument)
        }
    }

    private fun addGene(
        context: CommandContext<CommandSourceStack>,
        geneArgument: String
    ): Int {
        val target = EntityArgument.getEntity(context, TARGET_ARGUMENT) as? LivingEntity ?: return 0

        val geneToAdd = EnumGenes.valueOf(geneArgument)
        val targetGenes = target.getGenes() ?: return 0

        val success = targetGenes.addGene(geneToAdd)

        @Suppress("LiftReturnOrAssignment")
        if (success) {
            context.source.sendSuccess(
                Component.literal("Added gene: ${geneToAdd.description}"),
                false
            )
            return 1
        } else {
            context.source.sendFailure(
                Component.literal("Failed to add gene: ${geneToAdd.description}")
            )
            return 0
        }
    }

    private fun addAll(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player ?: return 0
        val playerGenes = player.getGenes() ?: return 0

        playerGenes.addAllGenes()

        context.source.sendSuccess(
            Component.literal("Added all genes!"),
            false
        )
        return 1
    }


}