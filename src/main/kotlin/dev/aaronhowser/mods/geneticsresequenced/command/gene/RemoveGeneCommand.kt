package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.getActiveGenes
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModMessageLang
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.commands.arguments.selector.EntitySelector
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object RemoveGeneCommand {

	private const val GENE_ARGUMENT = "gene"
	private const val TARGETS_ARGUMENT = "targets"

	val SUGGEST_GENE_RLS: SuggestionProvider<CommandSourceStack> =
		SuggestionProvider { context: CommandContext<CommandSourceStack>, suggestionsBuilder: SuggestionsBuilder ->
			val targets = context.getArgument(TARGETS_ARGUMENT, EntitySelector::class.java)
				.findEntities(context.source)
				.filterIsInstance<LivingEntity>()

			val genesHeldByMobs = targets
				.flatMap { it.getActiveGenes() }
				.mapNotNull { it.key?.location() }

			SharedSuggestionProvider.suggestResource(genesHeldByMobs, suggestionsBuilder)
		}

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("remove-gene")
			.requires { it.hasPermission(2) }
			.then(
				Commands.argument(TARGETS_ARGUMENT, EntityArgument.entities())
					.then(
						Commands.argument(GENE_ARGUMENT, ResourceLocationArgument.id())
							.suggests(SUGGEST_GENE_RLS)
							.executes { cmd ->
								val geneRl = ResourceLocationArgument.getId(cmd, GENE_ARGUMENT)
								val entities = EntityArgument.getEntities(cmd, TARGETS_ARGUMENT)
								removeGene(cmd, geneRl, entities)
							}
					)
			)
	}

	private fun removeGene(
		context: CommandContext<CommandSourceStack>,
		geneRl: ResourceLocation,
		entities: MutableCollection<out Entity>
	): Int {
		val gene = ModGenes.fromResourceLocation(context.source.registryAccess(), geneRl)
			?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

		return removeGene(context, gene, entities)
	}

	private fun removeGene(
		context: CommandContext<CommandSourceStack>,
		geneToRemove: Holder<Gene>,
		entities: MutableCollection<out Entity>
	): Int {
		val targets = entities.mapNotNull { it as? LivingEntity }

		if (targets.size == 1) {
			handleSingleTarget(context, targets.first(), geneToRemove)
		} else {
			handleMultipleTargets(context, targets, geneToRemove)
		}

		return 1
	}

	private fun handleSingleTarget(
		context: CommandContext<CommandSourceStack>,
		target: LivingEntity,
		geneHolder: Holder<Gene>
	) {
		val success = removeGeneFromTarget(target, geneHolder)

		if (success) {
			context.source.sendSuccess(
				{
					ModMessageLang.Commands.REMOVE_SINGLE_SUCCESS.toComponent(
						geneHolder.getName(),
						target.displayName
					)
				},
				false
			)
		} else {
			context.source.sendFailure(
				ModMessageLang.Commands.REMOVE_SINGLE_FAIL.toComponent(
					geneHolder.getName(),
					target.displayName
				)
			)
		}
	}

	private fun handleMultipleTargets(
		context: CommandContext<CommandSourceStack>,
		targets: List<LivingEntity>,
		geneHolder: Holder<Gene>
	) {
		var amountSuccess = 0
		var amountFail = 0

		for (target in targets) {
			val success = removeGeneFromTarget(target, geneHolder)

			if (success) amountSuccess++ else amountFail++
		}

		if (amountSuccess != 0) {
			context.source.sendSuccess(
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
			context.source.sendFailure(
				ModMessageLang.Commands.REMOVE_MULTIPLE_FAIL.toComponent(
					geneHolder.getName(),
					amountFail
				)
			)
		}

	}

	private fun removeGeneFromTarget(target: LivingEntity, geneToRemove: Holder<Gene>): Boolean {
		val success = target.removeGene(geneToRemove)
		return success
	}

}