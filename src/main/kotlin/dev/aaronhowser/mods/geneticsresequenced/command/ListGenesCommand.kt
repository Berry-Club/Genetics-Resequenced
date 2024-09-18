package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.geneHolders
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentUtils
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

        val targetGenesList = target.geneHolders

        if (targetGenesList.isEmpty()) {
            context.source.sendSuccess(
                { ModLanguageProvider.Commands.NO_GENES.toComponent() },
                false
            )
            return 1
        }

        val messageComponent =
            ModLanguageProvider.Commands.THEIR_GENES.toComponent(
                target.displayName
            )

        messageComponent.append(
            ComponentUtils.formatList(
                targetGenesList.map { Gene.getNameComponent(it, context.source.registryAccess()) },
                Component.literal("\n• ")
            )
        )

        context.source.sendSuccess({ messageComponent }, false)
        return 1
    }

}