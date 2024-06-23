package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.genes
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
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

        val targetGenesList = target.genes

        if (targetGenesList.isEmpty()) {
            context.source.sendSuccess(
                { Component.translatable(ModLanguageProvider.Commands.NO_GENES) },
                false
            )
            return 1
        }

        val messageComponent = Component.translatable(
            ModLanguageProvider.Commands.THEIR_GENES,
            target.displayName
        )

        for (gene in targetGenesList) {
            val geneComponent = Component.literal("\n• ").append(gene.nameComponent)
            messageComponent.append(geneComponent)
        }

        context.source.sendSuccess({ messageComponent }, false)
        return 1
    }

}