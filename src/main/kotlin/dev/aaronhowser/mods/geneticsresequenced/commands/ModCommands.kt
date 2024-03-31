package dev.aaronhowser.mods.geneticsresequenced.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.world.entity.player.Player

object ModCommands {

    fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {
        val modCommands = dispatcher.register(
            Commands.literal(GeneticsResequenced.ID)
                .then(addGeneArgument(dispatcher))
        )
    }

    fun addGeneArgument(dispatcher: CommandDispatcher<CommandSourceStack>): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("addGene")
            .executes { context ->
                val player = context.source.player

                if (player != null) {
                    println("Player is not null")
                }

                return@executes 1
            }
    }
}