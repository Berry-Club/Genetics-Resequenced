package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.events.player.OtherPlayerEvents
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveGeneCommand {

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
                            Gene.getRegistry().map { it.id }.plus(ALL),
                            builder
                        )
                    }
                    .then(
                        Commands
                            .argument(TARGET_ARGUMENT, EntityArgument.entity())
                            .executes { cmd -> removeGene(cmd, EntityArgument.getEntity(cmd, TARGET_ARGUMENT)) }
                    )
                    .executes { cmd -> removeGene(cmd) }
            )

    }

    private fun removeGene(context: CommandContext<CommandSourceStack>, entity: Entity? = null): Int {

        val geneArgument = StringArgumentType.getString(context, GENE_ARGUMENT)

        if (geneArgument == ALL) return removeAll(context, entity)

        val target = if (entity == null) {
            context.source.entity as? LivingEntity
        } else {
            entity as? LivingEntity
        } ?: return 0

        val geneToRemove = Gene.fromId(geneArgument)!!
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
            context.source.sendFailure(Component.translatable("command.geneticsresequenced.remove.fail.no_gene"))
            return 0
        }

        val success = targetGenes.removeGene(geneToRemove)

        if (success) {
            context.source.sendSuccess(
                Component.translatable("command.geneticsresequenced.remove.success")
                    .append(geneToRemove.nameComponent),
                false
            )

            OtherPlayerEvents.genesChanged(target, geneToRemove, false)

            return 1
        } else {
            val component = Component
                .translatable("command.geneticsresequenced.remove.fail.other")
                .append(geneToRemove.nameComponent)

            context.source.sendFailure(component)
            return 0
        }
    }

    private fun removeAll(context: CommandContext<CommandSourceStack>, entity: Entity?): Int {

        val target = if (entity == null) {
            context.source.entity as? LivingEntity
        } else {
            entity as? LivingEntity
        } ?: return 0

        val targetGenes = target.getGenes() ?: return 0

        if (targetGenes.getGeneList().isEmpty()) {
            context.source.sendSuccess(
                Component.translatable("command.geneticsresequenced.remove.fail.all"),
                false
            )
            return 1
        }

        val amountGenes = targetGenes.getGeneList().size

        for (gene in targetGenes.getGeneList()) {
            targetGenes.removeGene(gene)
            OtherPlayerEvents.genesChanged(target, gene, false)
        }

        context.source.sendSuccess(
            Component.translatable("command.geneticsresequenced.remove.success.all", amountGenes),
            false
        )
        return 1
    }

}