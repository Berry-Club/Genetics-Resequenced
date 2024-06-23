package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
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

        val messageComponent = Component.translatable(ModLanguageProvider.Commands.LIST_ALL_GENES)

        for (gene in Gene.getRegistry()) {
            val geneComponent = Component
                .literal("\n• ")
                .append(gene.nameComponent)

            messageComponent.append(geneComponent)
        }

        context.source.sendSuccess({ messageComponent }, false)
        return 1
    }

}