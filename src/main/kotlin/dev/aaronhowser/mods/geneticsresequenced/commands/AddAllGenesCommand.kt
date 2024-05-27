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
                    .argument(TARGET_ARGUMENT, EntityArgument.entities())
                    .executes { cmd -> addAllGenes(cmd, EntityArgument.getEntities(cmd, TARGET_ARGUMENT)) }
            )
            .executes { cmd -> addAllGenes(cmd) }
    }

    private fun addAllGenes(
        context: CommandContext<CommandSourceStack>,
        entities: MutableCollection<out Entity>? = null
    ): Int {

        val targets: List<LivingEntity?> =
            entities?.map { it as? LivingEntity } ?: listOf(context.source.entity as? LivingEntity)

        var amountSuccess = 0
        var amountFail = 0
        for (target in targets) {
            if (target == null) continue
            if (addAllGenesToTarget(target)) amountSuccess++ else amountFail++
        }

        if (amountSuccess != 0) {
            context.source.sendSuccess(
                Component.translatable("command.geneticsresequenced.add.success.all", amountSuccess),
                false
            )
        }
        if (amountFail != 0) {
            context.source.sendFailure(
                Component.translatable("command.geneticsresequenced.add.fail.all", amountFail)
            )
        }

        return 1
    }

    private fun addAllGenesToTarget(target: LivingEntity): Boolean {
        val targetGenes = target.getGenes() ?: return false

        for (gene in Gene.getRegistry()) {
            if (gene.isNegative) continue
            if (target !is Player && !gene.canMobsHave) continue

            targetGenes.addGene(gene)
            OtherPlayerEvents.genesChanged(target, gene, true)
        }

        return true
    }


}