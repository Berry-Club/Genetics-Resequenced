package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object ListGenesCommand {

    private const val TARGET_ARGUMENT = "target"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("list")
            .then(
                Commands
                    .argument(TARGET_ARGUMENT, EntityArgument.entity())
                    .executes { cmd -> run(cmd, EntityArgument.getEntity(cmd, TARGET_ARGUMENT)) }
            )
            .executes { cmd -> run(cmd) }
    }

    private fun run(context: CommandContext<CommandSourceStack>, entity: Entity? = null): Int {

        val target = if (entity == null) {
            context.source.entity as? LivingEntity
        } else {
            entity as? LivingEntity
        } ?: return 0

        val targetGenesList = target.getGenes()?.getGeneList()

        if (targetGenesList == null) {

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

        if (targetGenesList.isEmpty()) {
            context.source.sendSuccess(Component.literal("No genes found!"), false)
            return 1
        }

        val messageComponent = target.displayName.copy().append(Component.literal("'s genes:"))

        for (gene in targetGenesList) {
            val geneComponent = Component.literal("\n• ${gene.description}")
            messageComponent.append(geneComponent)
        }

        context.source.sendSuccess(messageComponent, false)
        return 1
    }
}