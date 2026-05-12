package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.getActiveGenes
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.commands.arguments.selector.EntitySelector
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveGeneCommand : AaronCommandHelper {

	private const val GENE = "gene"
	private const val TARGETS = "targets"

	val SUGGEST_GENE_RLS: SuggestionProvider<CommandSourceStack> =
		SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
			val targets = context.getArgument(TARGETS, EntitySelector::class.java)
				.findEntities(context.source)
				.filterIsInstance<LivingEntity>()

			val genesHeldByMobs = targets
				.flatMap { it.getActiveGenes() }
				.mapNotNull { it.key?.location() }

			SharedSuggestionProvider.suggestResource(genesHeldByMobs, suggestionsBuilder)
		}

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("remove") {
			requires { it.hasPermission(2) }

			thenArgument(TARGETS, EntityArgument.entities()) {
				thenArgument(GENE, ResourceLocationArgument.id()) {
					suggests(SUGGEST_GENE_RLS)

					executes { cmd ->
						val source = cmd.source
						val geneRl = ResourceLocationArgument.getId(cmd, GENE)
						val entities = EntityArgument.getEntities(cmd, TARGETS)
						removeGene(source, geneRl, entities)
					}
				}
			}
		}
	}

	private fun removeGene(
		source: CommandSourceStack,
		geneRl: ResourceLocation,
		targets: Collection<Entity>
	): Int {
		val geneHolder = ModGenes.fromResourceLocation(source.registryAccess(), geneRl)
			?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

		val actualTargets = targets.filterIsInstance<LivingEntity>()
		if (actualTargets.isEmpty()) return 0

		if (actualTargets.size == 1) {
			handleSingleTarget(source, actualTargets.first(), geneHolder)
		} else {
			handleMultipleTargets(source, actualTargets, geneHolder)
		}

		return 1
	}

	private fun handleSingleTarget(
		source: CommandSourceStack,
		target: LivingEntity,
		geneHolder: Holder<Gene>
	) {
		val success = target.removeGene(geneHolder)

		if (success) {
			source.sendSuccess(
				{
					ModMessageLang.Commands.REMOVE_SINGLE_SUCCESS.toComponent(
						geneHolder.getName(),
						target.displayName
					)
				},
				false
			)

			return
		}

		source.sendFailure(
			ModMessageLang.Commands.REMOVE_SINGLE_FAIL.toComponent(
				geneHolder.getName(),
				target.displayName
			)
		)
	}

	private fun handleMultipleTargets(
		source: CommandSourceStack,
		targets: List<LivingEntity>,
		geneHolder: Holder<Gene>
	) {
		var amountSuccess = 0
		var amountFail = 0

		for (target in targets) {
			val success = target.removeGene(geneHolder)
			if (success) amountSuccess++ else amountFail++
		}

		if (amountSuccess != 0) {
			source.sendSuccess(
				{
					ModMessageLang.Commands.REMOVE_MULTIPLE_SUCCESS.toComponent(
						geneHolder.getName(),
						amountSuccess
					)
				},
				true
			)
		}

		if (amountFail != 0) {
			source.sendFailure(
				ModMessageLang.Commands.REMOVE_MULTIPLE_FAIL.toComponent(
					geneHolder.getName(),
					amountFail
				)
			)
		}
	}

}