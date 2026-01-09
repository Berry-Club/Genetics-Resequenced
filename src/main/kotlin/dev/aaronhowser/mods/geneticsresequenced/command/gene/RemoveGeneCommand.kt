package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.builder.ArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.aaronhowser.mods.aaron.AaronExtensions.getLocationOrNull
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.getActiveGenes
import dev.aaronhowser.mods.geneticsresequenced.capability.GenesCapability.Companion.removeGene
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
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
				.mapNotNull { it.getLocationOrNull() }

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
								removeGene(
									cmd,
									ResourceLocationArgument.getId(cmd, GENE_ARGUMENT),
									EntityArgument.getEntities(cmd, TARGETS_ARGUMENT)
								)
							}
					)
			)
	}

	private fun removeGene(
		context: CommandContext<CommandSourceStack>,
		geneRl: ResourceLocation,
		entities: MutableCollection<out Entity>? = null
	): Int {

		val gene = ModGenes.fromResourceLocation(context.source.registryAccess(), geneRl)
			?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

		return removeGene(context, gene, entities)
	}

	private fun removeGene(
		context: CommandContext<CommandSourceStack>,
		geneString: String,
		entities: MutableCollection<out Entity>? = null
	): Int {

		val gene = ModGenes.fromIdPath(context.source.registryAccess(), geneString)
			?: throw IllegalArgumentException("Gene with id $geneString does not exist!")

		return removeGene(context, gene, entities)
	}

	private fun removeGene(
		context: CommandContext<CommandSourceStack>,
		geneToRemove: Holder<Gene>,
		entities: MutableCollection<out Entity>? = null
	): Int {

		val targets: List<LivingEntity> =
			entities?.mapNotNull { it as? LivingEntity } ?: listOfNotNull(context.source.entity as? LivingEntity)

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
			val component =
				ModLanguageProvider.Commands.REMOVE_SINGLE_SUCCESS.toComponent(
					geneHolder.getName(),
					target.displayName
				)

			context.source.sendSuccess({ component }, false)
		} else {
			val component =
				ModLanguageProvider.Commands.REMOVE_SINGLE_FAIL.toComponent(
					geneHolder.getName(),
					target.displayName
				)

			context.source.sendFailure(component)
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
			val component =
				ModLanguageProvider.Commands.REMOVE_MULTIPLE_SUCCESS.toComponent(
					geneHolder.getName(),
					amountSuccess
				)
			context.source.sendSuccess({ component }, true)
		}
		if (amountFail != 0) {
			val component =
				ModLanguageProvider.Commands.REMOVE_MULTIPLE_FAIL.toComponent(
					geneHolder.getName(),
					amountFail
				)
			context.source.sendFailure(component)
		}

	}

	private fun removeGeneFromTarget(target: LivingEntity, geneToRemove: Holder<Gene>): Boolean {
		val success = target.removeGene(geneToRemove)
		return success
	}

}