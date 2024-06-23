package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.data_attachment.GenesData.Companion.addGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

object AddAllGenesCommand {

    private const val TARGET_ARGUMENT = "targets"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addAllGenes")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(TARGET_ARGUMENT, EntityArgument.entities())
                    .executes { cmd -> addAllGenes(cmd, EntityArgument.getEntities(cmd, TARGET_ARGUMENT)) }
            )
            .executes { cmd -> addAllGenes(cmd) }
    }

    private fun addAllGenes(
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

    private fun handleMultipleTargets(context: CommandContext<CommandSourceStack>, targets: List<LivingEntity>) {

        for (target in targets) {
            val genesToAdd =
                Gene.getRegistry().filter { !it.isHidden && !it.isNegative && (target is Player || it.canMobsHave) }

            target.addGenes(*genesToAdd.toTypedArray())
        }

        val component =
            Component.translatable(
                "command.geneticsresequenced.add_all.multiple_targets",
                targets.size
            )

        context.source.sendSuccess({ component }, false)
    }

    private fun handleSingleTarget(context: CommandContext<CommandSourceStack>, target: LivingEntity) {
        val genesToAdd =
            Gene.getRegistry().filter { !it.isHidden && !it.isNegative && (target is Player || it.canMobsHave) }

        target.addGenes(*genesToAdd.toTypedArray())

        val component =
            Component.translatable(
                "command.geneticsresequenced.add_all.single_target",
                target.name
            )

        context.source.sendSuccess({ component }, false)
    }

}