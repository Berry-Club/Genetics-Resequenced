package dev.aaronhowser.mods.geneticsresequenced.compatibility.recipe.jei.recipe

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.data.GeneRequirements
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModInfoLang
import dev.aaronhowser.mods.geneticsresequenced.datagen.lang.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.getName
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isMutation
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.translationKey
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.SpawnEggItem

object ModJeiInformationRecipes {

	fun addInformationRecipes(registration: IRecipeRegistration) {
		addOrganicMatter(registration)
		addGeneDescriptions(registration)
		addMobGenes(registration)
	}

	private fun addGeneDescriptions(registration: IRecipeRegistration) {
		val registries = ClientUtil.localRegistryAccess ?: return

		for (geneHolder in ModGenes.getRegistrySorted(registries, includeHelixOnly = true)) {
			if (geneHolder.isDisabled) continue

			val components: MutableList<MutableComponent> = mutableListOf()
			components.add(
				geneHolder.getName()
					.withStyle { it.withColor(ChatFormatting.RESET).withUnderlined(true) }
			)

			val translationKey = "info." + geneHolder.translationKey

			if (!I18n.exists(translationKey)) {
				GeneticsResequenced.LOGGER.error("Gene is missing information translation key: $translationKey")
			}

			components.add(Component.translatable(translationKey))

			val requiredGeneHolders = GeneRequirements.getRequiredGeneHolders(geneHolder, registries)

			if (requiredGeneHolders.isNotEmpty()) {
				components.add(Component.literal("\n"))
				components.add(ModInfoLang.REQUIRED_GENES.toComponent())

				for (requiredGeneHolder in requiredGeneHolders) {
					val requiredGeneComponent = if (requiredGeneHolder.isNegative || requiredGeneHolder.isMutation) {
						Gene.getNameComponent(requiredGeneHolder)
					} else {
						Gene.getNameComponent(requiredGeneHolder)
							.withStyle { it.withColor(ChatFormatting.RESET) }
					}

					components.add(Component.literal("• ").append(requiredGeneComponent))
				}
			}

			registration.addItemStackInfo(
				DnaHelixItem.getHelixStack(geneHolder),
				*components.toTypedArray()
			)
		}
	}

	private fun addOrganicMatter(registration: IRecipeRegistration) {
		registration.addItemStackInfo(
			ModItems.ORGANIC_MATTER.toStack(),
			ModInfoLang.ORGANIC_MATTER_EMPTY.toComponent()
		)

		for (entityType in EntityDnaItem.VALID_ENTITY_TYPES) {
			val organicMatterStack = EntityDnaItem.getOrganicStack(entityType)

			registration.addItemStackInfo(
				organicMatterStack,
				ModInfoLang.ORGANIC_MATTER.toComponent(entityType.description)
			)
		}
	}

	private fun addMobGenes(registration: IRecipeRegistration) {
		val registries = ClientUtil.localRegistryAccess ?: return

		for (entityType in EntityDnaItem.VALID_ENTITY_TYPES) {
			val geneWeights = EntityGenes.getGeneHolderWeights(entityType, registries)
			val informationTextComponent = ModInfoLang.MOB_GENE_ONE.toComponent(entityType.description)
			val sumOfWeights = geneWeights.values.sum()

			for ((geneHolder, weight) in geneWeights) {
				val chance = (weight.toDouble() / sumOfWeights.toDouble() * 100).toInt()

				val geneComponent = if (geneHolder.isNegative || geneHolder.isMutation) {
					geneHolder.getName()
				} else {
					geneHolder.getName().withStyle { it.withColor(ChatFormatting.RESET) }
				}

				informationTextComponent.append(
					ModInfoLang.MOB_GENE_TWO.toComponent(chance, geneComponent)
				)
			}

			val stacks = buildList {
				add(EntityDnaItem.getOrganicStack(entityType))
				add(EntityDnaItem.getCell(entityType))

				val mobSpawnEgg = SpawnEggItem.byId(entityType)
				if (mobSpawnEgg != null) add(mobSpawnEgg.defaultInstance)
			}

			registration.addItemStackInfo(stacks, informationTextComponent)
		}

	}

}
