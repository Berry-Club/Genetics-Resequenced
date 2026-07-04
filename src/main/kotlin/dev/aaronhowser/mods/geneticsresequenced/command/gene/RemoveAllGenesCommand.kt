package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeAllGenes
import dev.aaronhowser.mods.geneticsresequenced.command.hasGamemasterPermission
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveAllGenesCommand : AaronCommandHelper {

	private const val TARGETS = "targets"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("remove-all") {
			requires { it.hasGamemasterPermission() }

			executes {
				val source = it.source
				val targets = listOf(source.entityOrException)

				removeAllGenes(source, targets)
			}

			thenArgument(TARGETS, EntityArgument.entities()) {
				executes {
					val source = it.source
					val entities = EntityArgument.getEntities(it, TARGETS)

					removeAllGenes(source, entities)
				}
			}
		}
	}

	private fun removeAllGenes(
		source: CommandSourceStack,
		targets: Collection<Entity>
	): Int {
		val actualTargets = targets.filterIsInstance<LivingEntity>()
		if (actualTargets.isEmpty()) return 0

		if (actualTargets.size == 1) {
			handleSingleTarget(source, actualTargets.first())
		} else {
			handleMultipleTargets(source, actualTargets)
		}

		return 1
	}

	private fun handleSingleTarget(
		source: CommandSourceStack,
		target: LivingEntity
	) {
		target.removeAllGenes()

		source.sendSuccess(
			{
				ModMessageLang.Commands.REMOVE_ALL_SINGLE.toComponent(
					target.displayName
				)
			},
			false
		)
	}

	private fun handleMultipleTargets(
		source: CommandSourceStack,
		targets: List<LivingEntity>
	) {
		for (target in targets) {
			target.removeAllGenes()
		}

		source.sendSuccess(
			{
				ModMessageLang.Commands.REMOVE_ALL_MULTIPLE.toComponent(
					targets.size
				)
			},
			false
		)
	}

}
