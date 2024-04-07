package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.event.player.OtherPlayerEvents
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.LivingEntity

@Suppress("MoveVariableDeclarationIntoWhen")
object RemoveGeneCommand : Command<CommandSourceStack> {

    private const val GENE_ARGUMENT = "gene"
    private const val TARGET_ARGUMENT = "target"
    private const val ALL = "_all"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands.argument(GENE_ARGUMENT, StringArgumentType.string())
                    .suggests { ctx, builder ->
                        SharedSuggestionProvider.suggest(
                            EnumGenes.values().map { it.name }.plus(ALL),
                            builder
                        )
                    }
                    .then(
                        Commands
                            .argument(TARGET_ARGUMENT, EntityArgument.entity())
                            .executes(RemoveGeneCommand)
                    )
                    .executes(RemoveGeneCommand)
            )

    }

    @Throws(CommandSyntaxException::class)
    override fun run(context: CommandContext<CommandSourceStack>): Int {
        val geneArgument = StringArgumentType.getString(context, GENE_ARGUMENT)

        return when (geneArgument) {
            ALL -> removeAll(context)
            else -> removeGene(context, geneArgument)
        }
    }

    private fun removeGene(context: CommandContext<CommandSourceStack>, geneArgument: String): Int {
        val target =
            EntityArgument.getEntity(context, TARGET_ARGUMENT) as? LivingEntity
                ?: context.source.entity as? LivingEntity
                ?: return 0

        val geneToRemove = EnumGenes.valueOf(geneArgument)
        val targetGenes = target.getGenes()

        if (targetGenes == null) {

            val component = Component
                .literal("An error has occurred! ")
                .append(
                    target.displayName.copy().append(
                        Component
                            .literal(" does not have the required capability!")
                            .withStyle(ChatFormatting.RESET)
                    )
                )

            context.source.sendSuccess(component, false)

            return 0
        }

        val doesNotHaveGene = !targetGenes.hasGene(geneToRemove)

        if (doesNotHaveGene) {
            context.source.sendFailure(
                Component.literal("Failed to remove gene: ${geneToRemove.description} - Gene not present")
            )
            return 0
        }

        val success = targetGenes.removeGene(geneToRemove)

        @Suppress("LiftReturnOrAssignment")
        if (success) {
            context.source.sendSuccess(
                Component.literal("Removed gene: ${geneToRemove.description}"),
                false
            )

            OtherPlayerEvents.genesChanged(target, geneToRemove, false)

            return 1
        } else {
            context.source.sendFailure(
                Component.literal("Failed to remove gene: ${geneToRemove.description}")
            )
            return 0
        }
    }

    private fun removeAll(context: CommandContext<CommandSourceStack>): Int {
        val target =
            EntityArgument.getEntity(context, TARGET_ARGUMENT) as? LivingEntity
                ?: context.source.entity as? LivingEntity
                ?: return 0
        val targetGenes = target.getGenes() ?: return 0

        for (gene in targetGenes.getGeneList()) {
            targetGenes.removeGene(gene)
            OtherPlayerEvents.genesChanged(target, gene, false)
        }

        context.source.sendSuccess(
            Component.literal("Removed all genes!"),
            false
        )
        return 1
    }

}