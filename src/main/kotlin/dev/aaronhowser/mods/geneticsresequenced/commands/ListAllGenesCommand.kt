package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object ListAllGenesCommand {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("listAllGenes")
            .executes { listAllGenes(it) }
    }

    private fun listAllGenes(context: CommandContext<CommandSourceStack>): Int {

        val messageComponent = Component.translatable("command.geneticsresequenced.geneList")

        for (gene in Gene.getRegistry()) {
            val geneComponent = Component
                .literal("\n• ")
                .append(gene.nameComponent)

            messageComponent.append(geneComponent)
        }

        context.source.sendSuccess(messageComponent, false)
        return 1
    }
}