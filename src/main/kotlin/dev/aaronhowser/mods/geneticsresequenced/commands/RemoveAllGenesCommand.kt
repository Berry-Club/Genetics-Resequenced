package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.GenesCapability.Companion.getGenes
import dev.aaronhowser.mods.geneticsresequenced.events.entity.OtherEntityEvents
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveAllGenesCommand {

    private const val TARGET_ARGUMENT = "targets"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("removeAllGenes")
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

        val targets: List<LivingEntity?> =
            entities?.map { it as? LivingEntity } ?: listOf(context.source.entity as? LivingEntity)

        var amountSuccess = 0
        var amountFail = 0
        for (target in targets) {
            if (target == null) continue
            if (removeAllGenesFromTarget(target)) amountSuccess++ else amountFail++
        }

        if (amountSuccess != 0) {
            context.source.sendSuccess(
                Component.translatable("command.geneticsresequenced.remove_all.success", amountSuccess),
                false
            )
        }

        if (amountFail != 0) {
            context.source.sendFailure(
                Component.translatable("command.geneticsresequenced.remove_all.fail", amountFail)
            )
        }

        return 1
    }

    private fun removeAllGenesFromTarget(target: LivingEntity): Boolean {
        val targetGenes = target.getGenes() ?: return false

        for (gene in targetGenes.getGeneList()) {
            targetGenes.removeGene(target, gene)
            OtherEntityEvents.genesChanged(target, gene, false)
        }

        return true
    }


}