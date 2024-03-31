package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ListGenesCommand : Command<CommandSourceStack> {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("listGenes")
            .executes(ListGenesCommand)
    }

    override fun run(context: CommandContext<CommandSourceStack>): Int {
        TODO("Not yet implemented")
    }
}