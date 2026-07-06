package dev.aaronhowser.mods.genetics_resequenced.command

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.genetics_resequenced.command.ModCommands.hasGameMasterPermission
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.genetics_resequenced.registry.ModBlocks
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos

object ClearBioGlowCommand : AaronCommandHelper {

	private const val RADIUS = "radius"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("clear-bio-glow") {

			executes {
				val source = it.source
				val center = BlockPos.containing(source.position)

				removeNearbyLights(source, center, range = 25)
			}

			thenArgument(RADIUS, IntegerArgumentType.integer(1, 200)) {
				requires { it.hasGameMasterPermission() }

				executes { cmd ->
					val source = cmd.source
					val center = BlockPos.containing(source.position)
					val radius = IntegerArgumentType.getInteger(cmd, RADIUS)

					removeNearbyLights(source, center, radius)
				}
			}
		}
	}

	private fun removeNearbyLights(
		source: CommandSourceStack,
		center: BlockPos,
		range: Int
	): Int {
		val level = source.level

		var removed = 0

		val positions = BlockPos.betweenClosed(
			center.offset(-range, -range, -range),
			center.offset(range, range, range)
		)

		for (pos in positions) {
			val stateThere = level.getBlockState(pos)
			if (!stateThere.isBlock(ModBlocks.BIOLUMINESCENCE_BLOCK)) continue

			removed++
			level.removeBlock(pos, false)
		}

		source.sendSuccess(
			{ ModMessageLang.Commands.REMOVED_LIGHTS.toComponent(removed) },
			true
		)

		return removed
	}

}
