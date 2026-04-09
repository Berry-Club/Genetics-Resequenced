package dev.aaronhowser.mods.genetics_resequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.aaronhowser.mods.genetics_resequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object GiveAllGenesCommand {

	private const val TARGET_ARGUMENT = "targets"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("give-all-genes")
			.requires { it.hasPermission(2) }
			.executes { cmd ->
				giveAllGenes(
					cmd,
					listOf(cmd.source.entityOrException)
				)
			}
			.then(
				Commands
					.argument(TARGET_ARGUMENT, EntityArgument.entities())
					.executes { cmd ->
						giveAllGenes(
							cmd,
							EntityArgument.getEntities(cmd, TARGET_ARGUMENT).toList()
						)
					}
			)
	}

	private fun giveAllGenes(
		context: CommandContext<CommandSourceStack>,
		entities: List<Entity>
	): Int {
		val targets: List<LivingEntity> = entities.filterIsInstance<LivingEntity>()
		if (targets.isEmpty()) return 0

		if (targets.size == 1) {
			handleSingleTarget(context, targets.first())
		} else {
			handleMultipleTargets(context, targets)
		}

		return 1
	}

	private fun handleMultipleTargets(context: CommandContext<CommandSourceStack>, targets: List<LivingEntity>) {
		for (target in targets) {
			val genesToAdd =
				ModGenes
					.getAllGeneHolders(context.source.registryAccess())
					.filter { !it.isDisabled && !it.isNegative && it.value().canEntityHave(target) }

			for (gene in genesToAdd) {
				target.addGene(gene)
			}
		}

		val component = ModLanguageProvider.Commands.ADD_ALL_MULTIPLE
			.toComponent(targets.size)

		context.source.sendSuccess({ component }, false)
	}

	private fun handleSingleTarget(context: CommandContext<CommandSourceStack>, target: LivingEntity) {
		val genesToAdd =
			ModGenes
				.getAllGeneHolders(context.source.registryAccess())
				.filter { !it.isDisabled && !it.isNegative && it.value().canEntityHave(target) }

		for (gene in genesToAdd) {
			target.addGene(gene)
		}

		val component = ModLanguageProvider.Commands.ADD_ALL_SINGLE
			.toComponent(target.name)

		context.source.sendSuccess({ component }, false)
	}

}