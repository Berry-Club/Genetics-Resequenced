package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.command.ModCommands.SUGGEST_GENE_RLS
import dev.aaronhowser.mods.geneticsresequenced.command.ModCommands.SUGGEST_GENE_STRINGS
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveGeneCommand {

    private const val GENE_RL_ARGUMENT = "geneRl"
    private const val GENE_STRING_ARGUMENT = "geneString"
    private const val TARGET_ARGUMENT = "targets"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeGene")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .literal("fromString")
                    .then(
                        Commands
                            .argument(GENE_STRING_ARGUMENT, StringArgumentType.string())
                            .suggests(SUGGEST_GENE_STRINGS)
                            .then(
                                Commands.argument(TARGET_ARGUMENT, EntityArgument.entities())
                                    .executes { cmd ->
                                        removeGene(
                                            cmd,
                                            StringArgumentType.getString(cmd, GENE_STRING_ARGUMENT),
                                            EntityArgument.getEntities(cmd, TARGET_ARGUMENT)
                                        )
                                    }
                            )
                            .executes { cmd ->
                                removeGene(
                                    cmd,
                                    StringArgumentType.getString(cmd, GENE_STRING_ARGUMENT),
                                    entities = null
                                )
                            }
                    )
            )
            .then(
                Commands.literal("fromId")
                    .then(
                        Commands
                            .argument(GENE_RL_ARGUMENT, ResourceLocationArgument.id())
                            .suggests(SUGGEST_GENE_RLS)
                            .then(
                                Commands
                                    .argument(TARGET_ARGUMENT, EntityArgument.entities())
                                    .executes { cmd ->
                                        removeGene(
                                            cmd,
                                            ResourceLocationArgument.getId(cmd, GENE_RL_ARGUMENT),
                                            EntityArgument.getEntities(cmd, TARGET_ARGUMENT)
                                        )
                                    }
                            )
                            .executes { cmd ->
                                removeGene(
                                    cmd,
                                    ResourceLocationArgument.getId(cmd, GENE_RL_ARGUMENT),
                                    entities = null
                                )
                            }
                    )
            )

    }

    private fun removeGene(
        context: CommandContext<CommandSourceStack>,
        geneRl: ResourceLocation,
        entities: MutableCollection<out Entity>? = null
    ): Int {

        val gene = GeneRegistry.fromResourceLocation(context.source.registryAccess(), geneRl)
            ?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

        return removeGene(context, gene, entities)
    }

    private fun removeGene(
        context: CommandContext<CommandSourceStack>,
        geneString: String,
        entities: MutableCollection<out Entity>? = null
    ): Int {

        val gene = GeneRegistry.fromIdPath(context.source.registryAccess(), geneString)
            ?: throw IllegalArgumentException("Gene with id $geneString does not exist!")

        return removeGene(context, gene, entities)
    }

    private fun removeGene(
        context: CommandContext<CommandSourceStack>,
        geneToRemove: Holder<Gene>,
        entities: MutableCollection<out Entity>? = null
    ): Int {

        val targets: List<LivingEntity> =
            entities?.mapNotNull { it as? LivingEntity } ?: listOfNotNull(context.source.entity as? LivingEntity)

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
        geneHolder: Holder<Gene>
    ) {

        val success = removeGeneFromTarget(target, geneHolder)

        if (success) {
            val component =
                ModLanguageProvider.Commands.REMOVE_SINGLE_SUCCESS.toComponent(
                    Gene.getNameComponent(geneHolder),
                    target.displayName
                )

            context.source.sendSuccess({ component }, false)
        } else {
            val component =
                ModLanguageProvider.Commands.REMOVE_SINGLE_FAIL.toComponent(
                    Gene.getNameComponent(geneHolder),
                    target.displayName
                )

            context.source.sendFailure(component)
        }
    }

    private fun handleMultipleTargets(
        context: CommandContext<CommandSourceStack>,
        targets: List<LivingEntity>,
        geneHolder: Holder<Gene>
    ) {
        var amountSuccess = 0
        var amountFail = 0
        for (target in targets) {
            val success = removeGeneFromTarget(target, geneHolder)

            if (success) amountSuccess++ else amountFail++
        }

        if (amountSuccess != 0) {
            val component =
                ModLanguageProvider.Commands.REMOVE_MULTIPLE_SUCCESS.toComponent(
                    Gene.getNameComponent(geneHolder),
                    amountSuccess
                )
            context.source.sendSuccess({ component }, true)
        }
        if (amountFail != 0) {
            val component =
                ModLanguageProvider.Commands.REMOVE_MULTIPLE_FAIL.toComponent(
                    Gene.getNameComponent(geneHolder),
                    amountFail
                )
            context.source.sendFailure(component)
        }

    }

    private fun removeGeneFromTarget(target: LivingEntity, geneToRemove: Holder<Gene>): Boolean {
        val success = target.removeGene(geneToRemove)
        return success
    }

}