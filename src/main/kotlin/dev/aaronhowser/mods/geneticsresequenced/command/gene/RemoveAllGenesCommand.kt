package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeAllGenes
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveAllGenesCommand {

    private const val TARGET_ARGUMENT = "targets"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeAll")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(TARGET_ARGUMENT, EntityArgument.entities())
                    .executes { cmd -> removeAllGenes(cmd, EntityArgument.getEntities(cmd, TARGET_ARGUMENT)) }
            )
            .executes { cmd -> removeAllGenes(cmd) }
    }

    private fun removeAllGenes(
        context: CommandContext<CommandSourceStack>,
        entities: MutableCollection<out Entity>? = null
    ): Int {

        val targets: List<LivingEntity> =
            entities?.mapNotNull { it as? LivingEntity } ?: listOfNotNull(context.source.entity as? LivingEntity)

        if (targets.size == 1) {
            handleSingleTarget(context, targets.first())
        } else {
            handleMultipleTargets(context, targets)
        }

        return 1
    }

    private fun handleSingleTarget(context: CommandContext<CommandSourceStack>, target: LivingEntity) {

        target.removeAllGenes()

        val component =
            ModLanguageProvider.Commands.REMOVE_ALL_SINGLE.toComponent(
                target.displayName
            )

        context.source.sendSuccess({ component }, false)
    }

    private fun handleMultipleTargets(context: CommandContext<CommandSourceStack>, targets: List<LivingEntity>) {
        for (target in targets) {
            target.removeAllGenes()
        }

        val component =
            ModLanguageProvider.Commands.REMOVE_ALL_MULTIPLE.toComponent(
                targets.size
            )

        context.source.sendSuccess({ component }, false)

    }

}