package dev.aaronhowser.mods.genetics_resequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object ListAllGenesCommand {

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("list-all-genes")
			.executes { listAllGenes(it) }
	}

	private fun listAllGenes(context: CommandContext<CommandSourceStack>): Int {
		val messageComponent = ModLanguageProvider.Commands.LIST_ALL_GENES.toComponent()

		messageComponent.append(
			OtherUtil.componentList(
				ModGenes.getRegistrySorted(context.source.registryAccess())
					.map(Gene::getNameComponent)
			)
		)

		context.source.sendSuccess({ messageComponent }, false)
		return 1
	}

}