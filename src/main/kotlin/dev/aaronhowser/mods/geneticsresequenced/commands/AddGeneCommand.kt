package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.commands.ModCommands.SUGGEST_GENE_RLS
import dev.aaronhowser.mods.geneticsresequenced.events.player.OtherPlayerEvents
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

object AddGeneCommand {

    private const val GENE_ARGUMENT = "gene"
    private const val TARGET_ARGUMENT = "targets"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(GENE_ARGUMENT, ResourceLocationArgument.id())
                    .suggests(SUGGEST_GENE_RLS)
                    .then(
                        Commands
                            .argument(TARGET_ARGUMENT, EntityArgument.entities())
                            .executes { cmd -> addGene(cmd, EntityArgument.getEntities(cmd, TARGET_ARGUMENT)) }
                    )
                    .executes { cmd -> addGene(cmd) }
            )
    }

    private fun addGene(
        context: CommandContext<CommandSourceStack>,
        entities: MutableCollection<out Entity>? = null
    ): Int {

        val geneArgument = ResourceLocationArgument.getId(context, GENE_ARGUMENT)

        val targets: List<LivingEntity?> =
            entities?.map { it as? LivingEntity } ?: listOf(context.source.entity as? LivingEntity)

        val geneToAdd = Gene.fromId(geneArgument)!!

        var amountSuccess = 0
        var amountFail = 0
        for (target in targets) {
            if (target == null) continue
            val geneAdded = addGeneToTarget(target, geneToAdd)
            if (geneAdded) amountSuccess++ else amountFail++
        }

        if (amountSuccess != 0) {
            val component = Component.translatable(
                "command.geneticsresequenced.add_gene.success",
                geneToAdd.nameComponent,
                amountSuccess
            )
            context.source.sendSuccess(component, false)
        }

        if (amountFail != 0) {
            val component = Component.translatable(
                "command.geneticsresequenced.add_gene.fail",
                geneToAdd.nameComponent,
                amountFail
            )
            context.source.sendFailure(component)
        }

        return 1
    }

    private fun addGeneToTarget(
        target: LivingEntity,
        geneToAdd: Gene,
    ): Boolean {
        val targetGenes = target.getGenes()!!

        val alreadyHasGene = targetGenes.hasGene(geneToAdd)
        if (alreadyHasGene) {
            GeneticsResequenced.LOGGER.info("Tried to add gene ${geneToAdd.id} to ${target.name.string}, but they already have it!")
            return false
        }

        val cantAddToMob = target !is Player && !geneToAdd.canMobsHave
        if (cantAddToMob) {
            GeneticsResequenced.LOGGER.info("Tried to add gene ${geneToAdd.id} to ${target.name.string}, but they can't have it!")
            return false
        }

        val success = targetGenes.addGene(geneToAdd)

        if (success) {
            OtherPlayerEvents.genesChanged(target, geneToAdd, true)

            return true
        } else {
            return false
        }
    }

}