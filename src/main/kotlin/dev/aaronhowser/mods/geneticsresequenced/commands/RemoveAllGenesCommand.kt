package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.events.player.OtherPlayerEvents
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveAllGenesCommand {

    private const val TARGET_ARGUMENT = "target"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeAllGenes")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(TARGET_ARGUMENT, EntityArgument.entity())
                    .executes { cmd -> removeAllGenes(cmd, EntityArgument.getEntity(cmd, TARGET_ARGUMENT)) }
            )
            .executes { cmd -> removeAllGenes(cmd, null) }
    }

    private fun removeAllGenes(context: CommandContext<CommandSourceStack>, entity: Entity?): Int {

        val target = if (entity == null) {
            context.source.entity as? LivingEntity
        } else {
            entity as? LivingEntity
        } ?: return 0

        val targetGenes = target.getGenes() ?: return 0

        if (targetGenes.getGeneList().isEmpty()) {
            context.source.sendSuccess(
                Component.translatable("command.geneticsresequenced.remove.fail.all"),
                false
            )
            return 1
        }

        val amountGenes = targetGenes.getGeneList().size

        for (gene in targetGenes.getGeneList()) {
            targetGenes.removeGene(gene)
            OtherPlayerEvents.genesChanged(target, gene, false)
        }

        context.source.sendSuccess(
            Component.translatable("command.geneticsresequenced.remove.success.all", amountGenes),
            false
        )
        return 1
    }



}