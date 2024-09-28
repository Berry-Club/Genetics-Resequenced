package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.geneticsresequenced.command.gene.*
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object GeneCommand {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("gene")
            .then(ListGenesCommand.register())
            .then(ListAllGenesCommand.register())
            .then(AddGeneCommand.register())
            .then(AddAllGenesCommand.register())
            .then(RemoveGeneCommand.register())
            .then(RemoveAllGenesCommand.register())
    }

}