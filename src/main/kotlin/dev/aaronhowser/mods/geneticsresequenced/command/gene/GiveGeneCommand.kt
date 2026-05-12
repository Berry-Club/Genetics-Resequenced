package dev.aaronhowser.mods.geneticsresequenced.command.gene

import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.ArgumentBuilder
import dev.aaronhowser.mods.aaron.command.AaronCommandHelper
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.addGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.GenesData.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.attachment.TemporaryGenesData.Companion.addTemporaryGene
import dev.aaronhowser.mods.geneticsresequenced.command.ModCommands.SUGGEST_GENE_RLS
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

object GiveGeneCommand : AaronCommandHelper {

	private const val GENE = "gene"
	private const val TARGETS = "targets"
	private const val DURATION = "duration"

	fun register(): ArgumentBuilder<CommandSourceStack, *> {
		return literal("give") {
			requires { it.hasPermission(2) }

			thenArgument(GENE, ResourceLocationArgument.id()) {

				suggests(SUGGEST_GENE_RLS)

				executes {
					val source = it.source
					val geneRl = ResourceLocationArgument.getId(it, GENE)
					val targets = listOf(source.playerOrException)

					addGene(source, geneRl, targets, duration = -1)
				}

				thenArgument(TARGETS, EntityArgument.entities()) {
					executes {
						val source = it.source
						val geneRl = ResourceLocationArgument.getId(it, GENE)
						val targets = EntityArgument.getEntities(it, TARGETS)

						addGene(source, geneRl, targets, duration = -1)
					}

					thenLiteral("temporary") {
						thenArgument(DURATION, IntegerArgumentType.integer(1)) {
							executes {
								val source = it.source
								val geneRl = ResourceLocationArgument.getId(it, GENE)
								val targets = EntityArgument.getEntities(it, TARGETS)
								val duration = IntegerArgumentType.getInteger(it, DURATION)

								addGene(source, geneRl, targets, duration)
							}
						}
					}
				}
			}
		}
	}

	private fun addGene(
		source: CommandSourceStack,
		geneRl: ResourceLocation,
		entities: Collection<Entity>,
		duration: Int
	): Int {
		val gene = ModGenes.fromResourceLocation(source.registryAccess(), geneRl)
			?: throw IllegalArgumentException("Gene with id $geneRl does not exist!")

		return addGene(source, gene, entities, duration)
	}

	private fun addGene(
		source: CommandSourceStack,
		geneToAdd: Holder<Gene>,
		entities: Collection<Entity>,
		duration: Int
	): Int {
		val targets = entities.mapNotNull { it as? LivingEntity }

		if (targets.isEmpty()) {
			source.sendFailure(Component.literal("No valid living entity targets found!"))
			return 0
		}

		if (targets.size == 1) {
			handleSingleTarget(source, targets.first(), geneToAdd, duration)
		} else {
			handleMultipleTargets(source, targets, geneToAdd, duration)
		}

		return 1
	}

	private fun handleSingleTarget(
		source: CommandSourceStack,
		target: LivingEntity,
		geneHolder: Holder<Gene>,
		duration: Int = -1
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

			return
		}

		source.sendFailure(
			ModLanguageProvider.Commands.ADD_SINGLE_FAIL.toComponent(
				geneHolder.getName(),
				target.name
			)
		)

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
		duration: Int
	): Boolean {
		if (target.hasGene(geneHolder)) {
			GeneticsResequenced.LOGGER.info("Tried to add gene ${geneHolder.key!!.location()} to ${target.name.string}, but they already have it!")
			return false
		}

		if (!geneHolder.value().canEntityHave(target)) {
			GeneticsResequenced.LOGGER.info("Tried to add gene ${geneHolder.key!!.location()} to ${target.name.string}, but they can't have it!")
			return false
		}

		val success = if (duration < 0) {
			target.addGene(geneHolder)
		} else {
			target.addTemporaryGene(geneHolder, duration)
		}

		return success
	}

}