package dev.aaronhowser.mods.genetics_resequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.commands.CommandSourceStack

object ListAllGenesCommand : AaronCommandHelper {

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return noArg("list-all", ::listAllGenes)
	}

	private fun listAllGenes(
		source: CommandSourceStack
	): Int {
		source.sendSuccess(
			{
				ModMessageLang.Commands.LIST_ALL_GENES.toComponent()
					.append(
						OtherUtil.componentList(
							ModGenes.getRegistrySorted(source.registryAccess())
								.map(Gene::getNameComponent)
						)
					)
			},
			false
		)

		return 1
	}

}