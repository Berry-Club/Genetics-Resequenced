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
import net.minecraft.world.entity.player.Player

object AddAllGenesCommand {

    private const val TARGET_ARGUMENT = "target"

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addAllGenes")
            .requires { it.hasPermission(2) }
            .then(
                Commands
                    .argument(TARGET_ARGUMENT, EntityArgument.entity())
                    .executes { cmd -> addAllGenes(cmd, EntityArgument.getEntity(cmd, TARGET_ARGUMENT)) }
            )
            .executes { cmd -> addAllGenes(cmd) }
    }

    private fun addAllGenes(context: CommandContext<CommandSourceStack>, entity: Entity? = null): Int {
        val target = if (entity == null) {
            context.source.entity as? LivingEntity
        } else {
            entity as? LivingEntity
        } ?: return 0

        val targetGenes = target.getGenes() ?: return 0

        for (gene in Gene.getRegistry()) {
            if (gene.isNegative) continue
            if (target !is Player && !gene.canMobsHave) continue

            targetGenes.addGene(gene)
            OtherPlayerEvents.genesChanged(target, gene, true)
        }

        context.source.sendSuccess(
            Component.translatable("command.geneticsresequenced.add.success.all"),
            false
        )
        return 1
    }

}