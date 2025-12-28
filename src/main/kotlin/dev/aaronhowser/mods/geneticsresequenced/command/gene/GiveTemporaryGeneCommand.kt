package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasPermanentGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData.Companion.addTemporaryGene
import dev.aaronhowser.mods.geneticsresequenced.command.ModCommands.SUGGEST_GENE_RLS
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object GiveTemporaryGeneCommand {

	private const val GENE = "gene"
	private const val TARGETS = "targets"
	private const val DURATION = "duration"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return Commands
			.literal("give-temporary-gene")
			.requires { it.hasPermission(2) }
			.then(
				Commands
					.argument(GENE, ResourceLocationArgument.id())
					.suggests(SUGGEST_GENE_RLS)
					.executes { cmd ->
						val gene = ResourceLocationArgument.getId(cmd, GENE)
						val duration = 20 * 60 * 5
						val targets = listOf(cmd.source.playerOrException)
						addGene(cmd.source, gene, targets, duration)
					}
					.then(
						Commands
							.argument(DURATION, IntegerArgumentType.integer(1))
							.executes {
								val gene = ResourceLocationArgument.getId(it, GENE)
								val duration = IntegerArgumentType.getInteger(it, DURATION)
								val targets = listOf(it.source.playerOrException)
								addGene(it.source, gene, targets, duration)
							}
							.then(
								Commands
									.argument(TARGETS, EntityArgument.entities())
									.executes {
										val gene = ResourceLocationArgument.getId(it, GENE)
										val duration = IntegerArgumentType.getInteger(it, DURATION)
										val targets = EntityArgument.getEntities(it, TARGETS)
										addGene(it.source, gene, targets, duration)
									}
							)
					)
			)
	}

	private fun addGene(
		source: CommandSourceStack,
		geneRl: ResourceLocation,
		entities: Collection<Entity>,
		duration: Int
	): Int {
		val geneHolder = ModGenes.fromResourceLocation(source.registryAccess(), geneRl)
			?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

		val targets = entities.mapNotNull { it as? LivingEntity }

		if (targets.isEmpty()) {
			source.sendFailure(Component.literal("No valid living entity targets found!"))
			return 0
		}

		if (targets.size == 1) {
			handleSingleTarget(source, targets.first(), geneHolder, duration)
		} else {
			handleMultipleTargets(source, targets, geneHolder, duration)
		}

		return 1
	}

	private fun handleSingleTarget(
		source: CommandSourceStack,
		target: LivingEntity,
		geneHolder: Holder<Gene>,
		duration: Int
	) {
		val success = addGeneToTarget(target, geneHolder, duration)

		if (success) {
			source.sendSuccess(
				{
					ModLanguageProvider.Commands.ADD_SINGLE_SUCCESS.toComponent(
						geneHolder.getName(),
						target.name
					)
				},
				false
			)
		} else {
			source.sendFailure(
				ModLanguageProvider.Commands.ADD_SINGLE_FAIL.toComponent(
					geneHolder.getName(),
					target.name
				)
			)
		}
	}

	private fun handleMultipleTargets(
		source: CommandSourceStack,
		targets: List<LivingEntity>,
		geneHolder: Holder<Gene>,
		duration: Int
	) {
		var amountSuccess = 0
		var amountFail = 0

		for (target in targets) {
			val success = addGeneToTarget(target, geneHolder, duration)
			if (success) amountSuccess++ else amountFail++
		}

		if (amountSuccess != 0) {
			source.sendSuccess(
				{
					ModLanguageProvider.Commands.ADD_MULTIPLE_SUCCESS.toComponent(
						geneHolder.getName(),
						amountSuccess
					)
				},
				false
			)
		}

		if (amountFail != 0) {
			source.sendFailure(
				ModLanguageProvider.Commands.ADD_MULTIPLE_FAIL.toComponent(
					geneHolder.getName(),
					amountFail
				)
			)
		}
	}

	private fun addGeneToTarget(
		target: LivingEntity,
		geneHolder: Holder<Gene>,
		duration: Int,
	): Boolean {
		val alreadyHasGene = target.hasPermanentGene(geneHolder)

		if (alreadyHasGene) {
			GeneticsResequenced.LOGGER.info("Tried to add temporary gene ${geneHolder.key!!.location()} to ${target.name.string}, but they already have it as a permanent Gene!")
			return false
		}

		if (!geneHolder.value().canEntityHave(target)) {
			GeneticsResequenced.LOGGER.info("Tried to add temporary gene ${geneHolder.key!!.location()} to ${target.name.string}, but that entity type cannot have that gene!")
			return false
		}

		val success = target.addTemporaryGene(geneHolder, duration)

		return success
	}

}