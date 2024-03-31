package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.CommandDispatcher
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ModCommands {

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val modCommands = dispatcher.register(
            Commands.literal(GeneticsResequenced.ID)
                .then(AddGeneCommand.register())
                .then(ListAllGenesCommand.register())
                .then(ListGenesCommand.register())
        )

        dispatcher.register(Commands.literal("genes").redirect(modCommands))
    }


}