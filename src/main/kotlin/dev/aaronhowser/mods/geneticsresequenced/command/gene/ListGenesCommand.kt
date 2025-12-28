package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.permanentGeneHolders
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object ListGenesCommand {

	private const val TARGET_ARGUMENT = "target"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("list-genes")
			.requires { it.hasPermission(2) }
			.executes {
				val target = it.source.entityOrException
				listGenes(it.source, target)
			}
			.then(
				Commands
					.argument(TARGET_ARGUMENT, EntityArgument.entity())
					.executes {
						val target = EntityArgument.getEntity(it, TARGET_ARGUMENT)
						listGenes(it.source, target)
					}
			)
	}

	private fun listGenes(
		source: CommandSourceStack,
		target: Entity
	): Int {
		if (target !is LivingEntity) {
			return 0
		}

		val targetGenesList = target.permanentGeneHolders

		if (targetGenesList.isEmpty()) {
			source.sendSuccess(
				{ ModLanguageProvider.Commands.NO_GENES.toComponent() },
				false
			)
			return 1
		}

		source.sendSuccess(
			{
				val messageComponent =
					ModLanguageProvider.Commands.THEIR_GENES.toComponent(
						target.displayName
					)

				messageComponent.append(
					OtherUtil.componentList(
						targetGenesList.map(Gene::getNameComponent)
					)
				)
			},
			false
		)

		return 1
	}

}