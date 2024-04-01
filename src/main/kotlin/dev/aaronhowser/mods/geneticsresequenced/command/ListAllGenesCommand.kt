package dev.aaronhowser.mods.geneticsresequenced.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.EnumGenes
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent

object ListAllGenesCommand : Command<CommandSourceStack> {

    fun register(): ArgumentBuilder<CommandSourceStack, *> {
        return Commands
            .literal("allGeneIds")
            .executes(ListAllGenesCommand)
    }

    override fun run(context: CommandContext<CommandSourceStack>): Int {

        val messageComponent = Component.literal("Gene List:")

        for (gene in EnumGenes.values()) {

            val enumComponent = Component
                .literal(gene.name)
                .withStyle {
                    it
                        .withColor(ChatFormatting.DARK_AQUA)
                        .withUnderlined(true)
                        .withHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Copy \"${gene.name}\"")))
                        .withClickEvent(ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, gene.name))
                }

            val descriptionComponent = Component
                .literal(" - ${gene.description}")
                .withStyle(ChatFormatting.GRAY)

            val lineComponent = Component
                .literal("\n")
                .append(enumComponent)
                .append(descriptionComponent)

            messageComponent.append(lineComponent)
        }

        context.source.sendSuccess(messageComponent, false)
        return 1
    }
}