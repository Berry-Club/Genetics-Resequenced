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

object AddGeneCommand {

    private const val GENE_ARGUMENT = "gene"
    private const val TARGET_ARGUMENT = "target"
    private const val ALL = "_all"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(GENE_ARGUMENT, StringArgumentType.string())
                    .suggests { ctx, builder ->
                        SharedSuggestionProvider.suggest(
                            Gene.getRegistry().map { it.id }.plus(ALL),
                            builder
                        )
                    }
                    .then(
                        Commands
                            .argument(TARGET_ARGUMENT, EntityArgument.entity())
                            .executes { cmd -> addGene(cmd, EntityArgument.getEntity(cmd, TARGET_ARGUMENT)) }
                    )
                    .executes { cmd -> addGene(cmd) }
            )
    }

    private fun addGene(context: CommandContext<CommandSourceStack>, entity: Entity? = null): Int {

        val geneArgument = StringArgumentType.getString(context, GENE_ARGUMENT)

        if (geneArgument == ALL) return addAll(context, entity)

        val target = if (entity == null) {
            context.source.entity as? LivingEntity
        } else {
            entity as? LivingEntity
        } ?: return 0

        val geneToAdd = Gene.valueOf(geneArgument)
        val targetGenes = target.getGenes()

        if (targetGenes == null) {
            val component = Component
                .literal("An error has occurred! ")
                .append(
                    target.displayName.copy().append(
                        Component
                            .literal(" does not the required capability!")
                            .withStyle(ChatFormatting.RESET)
                    )
                )
            context.source.sendSuccess(component, false)
            return 0
        }

        val alreadyHasGene = targetGenes.hasGene(geneToAdd)
        if (alreadyHasGene) {

            val component = Component.translatable("command.geneticsresequenced.add.fail.already_present")

            context.source.sendFailure(component)
            return 0
        }

        val success = targetGenes.addGene(geneToAdd)

        if (success) {
            val component = Component
                .translatable("command.geneticsresequenced.add.success")
                .append(geneToAdd.nameComponent)

            context.source.sendSuccess(component, false)

            OtherPlayerEvents.genesChanged(target, geneToAdd, true)

            return 1
        } else {
            val component = Component
                .translatable("command.geneticsresequenced.add.fail.other")
                .append(geneToAdd.nameComponent)

            context.source.sendFailure(component)
            return 0
        }
    }

    private fun addAll(context: CommandContext<CommandSourceStack>, entity: Entity? = null): Int {
        val target = if (entity == null) {
            context.source.entity as? LivingEntity
        } else {
            entity as? LivingEntity
        } ?: return 0

        val targetGenes = target.getGenes() ?: return 0

        for (gene in Gene.getRegistry()) {
            if (gene.isNegative) continue
            targetGenes.addGene(gene)
            OtherPlayerEvents.genesChanged(target, gene, true)
        }

        context.source.sendSuccess(
            Component.translatable("command.geneticsresequenced.add.success.all"),
            false
        )
        return 1
    }


}