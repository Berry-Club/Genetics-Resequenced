package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Genes.getGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

object ListGenesCommand : Command<CommandSourceStack> {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("list")
            .executes(ListGenesCommand)
    }

    override fun run(context: CommandContext<CommandSourceStack>): Int {

        val player = context.source.player ?: return 0
        val genes = player.getGenes().getGeneList()

        if (genes.isEmpty()) {
            context.source.sendSuccess(Component.literal("No genes found!"), false)
            return 1
        }

        val messageComponent = Component.literal("Gene List:")

        for (gene in genes) {
            val geneComponent = Component.literal("\n• ${gene.description}")
            messageComponent.append(geneComponent)
        }

        context.source.sendSuccess(messageComponent, false)
        return 1
    }
}