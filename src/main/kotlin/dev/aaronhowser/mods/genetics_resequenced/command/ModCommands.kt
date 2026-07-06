package dev.aaronhowser.mods.genetics_resequenced.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.command.gene.*
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.server.permissions.Permission
import net.minecraft.server.permissions.PermissionLevel
import java.util.concurrent.CompletableFuture

object ModCommands : AaronCommandHelper {

	fun register(dispatcher: CommandDispatcher<CommandSourceStack>) {

		val command = literal(GeneticsResequenced.MOD_ID) {
			then(ClearBioGlowCommand.register())

			thenLiteral("gene") {
				then(ListGenesCommand.register())
				then(ListAllGenesCommand.register())

				then(GiveGeneCommand.register())
				then(GiveAllGenesCommand.register())

				then(RemoveGeneCommand.register())
				then(RemoveAllGenesCommand.register())
			}
		}

		val primary = dispatcher.register(command)

		dispatcher.register(Commands.literal("genetics").redirect(primary))
		dispatcher.register(Commands.literal("gr").redirect(primary))
	}

	fun getGeneSuggestions(
		context: CommandContext<CommandSourceStack>,
		suggestionsBuilder: SuggestionsBuilder
	): CompletableFuture<Suggestions> {
		val allGeneIds = ModGenes
			.getRegistrySorted(context.source.registryAccess())
			.mapNotNull { it.key?.identifier() }

		return SharedSuggestionProvider.suggestResource(allGeneIds, suggestionsBuilder)
	}

	fun CommandSourceStack.hasGameMasterPermission(): Boolean {
		return permissions().hasPermission(Permission.HasCommandLevel(PermissionLevel.GAMEMASTERS))
	}

}