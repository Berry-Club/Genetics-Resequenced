package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.events.player.OtherPlayerEvents
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

        val targets: List<LivingEntity?> =
            entities?.map { it as? LivingEntity } ?: listOf(context.source.entity as? LivingEntity)

        val geneToRemove = Gene.fromId(geneArgument)!!

        var amountSuccess = 0
        var amountFail = 0
        for (target in targets) {
            if (target == null) continue
            val success = removeGeneFromTarget(target, geneToRemove)

            if (success) amountSuccess++ else amountFail++
        }

        if (amountSuccess != 0) {
            val component = Component.translatable(
                "command.geneticsresequenced.remove_gene.success",
                amountSuccess,
                geneToRemove.nameComponent
            )
            context.source.sendSuccess(component, true)
        }
        if (amountFail != 0) {
            val component = Component.translatable(
                "command.geneticsresequenced.remove_gene.fail",
                amountFail,
                geneToRemove.nameComponent
            )
            context.source.sendSuccess(component, true)
        }

        return 1
    }

    private fun removeGeneFromTarget(target: LivingEntity, geneToRemove: Gene): Boolean {
        val targetGenes = target.getGenes()!!

        val doesNotHaveGene = !targetGenes.hasGene(geneToRemove)

        if (doesNotHaveGene) {
            return false
        }

        val success = targetGenes.removeGene(geneToRemove)

        if (success) {
            OtherPlayerEvents.genesChanged(target, geneToRemove, false)
            return true
        } else {
            return false
        }
    }

}