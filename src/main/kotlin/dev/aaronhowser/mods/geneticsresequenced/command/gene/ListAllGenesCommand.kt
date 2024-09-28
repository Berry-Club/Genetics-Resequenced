package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ListAllGenesCommand {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("listAll")
            .executes { listAllGenes(it) }
    }

    private fun listAllGenes(context: CommandContext<CommandSourceStack>): Int {

        val messageComponent = ModLanguageProvider.Commands.LIST_ALL_GENES.toComponent()

        messageComponent.append(
            OtherUtil.componentList(
                GeneRegistry.getRegistrySorted(context.source.registryAccess())
                    .map { Gene.getNameComponent(it) }
            )
        )

        context.source.sendSuccess({ messageComponent }, false)
        return 1
    }

}