package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.removeGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveGeneCommand {

    private const val GENE_ARGUMENT = "gene"
    private const val TARGET_ARGUMENT = "targets"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(GENE_ARGUMENT, ResourceLocationArgument.id())
                    .suggests(ModCommands.SUGGEST_GENE_RLS)
                    .then(
                        Commands
                            .argument(TARGET_ARGUMENT, EntityArgument.entities())
                            .executes { cmd -> removeGene(cmd, EntityArgument.getEntities(cmd, TARGET_ARGUMENT)) }
                    )
                    .executes { cmd -> removeGene(cmd) }
            )

    }

    private fun removeGene(
        context: CommandContext<CommandSourceStack>,
        entities: MutableCollection<out Entity>? = null
    ): Int {
        val geneArgument = ResourceLocationArgument.getId(context, GENE_ARGUMENT)

        val targets: List<LivingEntity> =
            entities?.mapNotNull { it as? LivingEntity } ?: listOfNotNull(context.source.entity as? LivingEntity)

        val geneToRemove = Gene.fromId(geneArgument) ?: return 0

        if (targets.size == 1) {
            handleSingleTarget(context, targets.first(), geneToRemove)
        } else {
            handleMultipleTargets(context, targets, geneToRemove)
        }

        return 1
    }

    private fun handleSingleTarget(
        context: CommandContext<CommandSourceStack>,
        target: LivingEntity,
        geneToRemove: Gene
    ) {

        val success = removeGeneFromTarget(target, geneToRemove)

        if (success) {
            val component = Component.translatable(
                "command.geneticsresequenced.remove_gene.single_target.success",
                geneToRemove.nameComponent,
                target.displayName
            )

            context.source.sendSuccess({ component }, false)
        } else {
            val component = Component.translatable(
                "command.geneticsresequenced.remove_gene.single_target.fail",
                geneToRemove.nameComponent,
                target.displayName
            )

            context.source.sendFailure(component)
        }
    }

    private fun handleMultipleTargets(
        context: CommandContext<CommandSourceStack>,
        targets: List<LivingEntity>,
        geneToRemove: Gene
    ) {
        var amountSuccess = 0
        var amountFail = 0
        for (target in targets) {
            val success = removeGeneFromTarget(target, geneToRemove)

            if (success) amountSuccess++ else amountFail++
        }

        if (amountSuccess != 0) {
            val component = Component.translatable(
                "command.geneticsresequenced.remove_gene.multiple_targets.success",
                geneToRemove.nameComponent,
                amountSuccess
            )
            context.source.sendSuccess({ component }, true)
        }
        if (amountFail != 0) {
            val component = Component.translatable(
                "command.geneticsresequenced.remove_gene.multiple_targets.fail",
                geneToRemove.nameComponent,
                amountFail
            )
            context.source.sendFailure(component)
        }

    }

    private fun removeGeneFromTarget(target: LivingEntity, geneToRemove: Gene): Boolean {
        val success = target.removeGenes(geneToRemove)
        return success
    }

}