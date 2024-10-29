package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.data.EntityGenes
import dev.aaronhowser.mods.geneticsresequenced.data.GeneRequirements
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isMutation
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.translationKey
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiInfoRecipe
import dev.emi.emi.api.stack.EmiIngredient
import net.minecraft.ChatFormatting
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.crafting.Ingredient

object ModInformationRecipes {

    fun getInformationRecipes(registries: HolderLookup.Provider): List<EmiInfoRecipe> {
        return organicMatter() + geneDescriptions(registries) + mobGenes(registries)
    }

    private fun geneDescriptions(registries: HolderLookup.Provider): List<EmiInfoRecipe> {
        val recipes = mutableListOf<EmiInfoRecipe>()

        for (geneHolder in ModGenes.getRegistrySorted(registries, includeHelixOnly = true)) {
            if (geneHolder.isDisabled) continue

            val components: MutableList<MutableComponent> = mutableListOf()
            components.add(
                Gene.getNameComponent(geneHolder)
                    .withStyle { it.withColor(ChatFormatting.RESET).withUnderlined(true) }
            )

            val translationKey = "info." + geneHolder.translationKey
            val geneDesc = Component.translatable(translationKey)

            if (geneDesc.toString() == translationKey) {
                GeneticsResequenced.LOGGER.error("Gene is missing information translation key: $translationKey")
            }

            components.add(geneDesc)

            val requiredGeneHolders = GeneRequirements.getGeneRequiredGeneHolders(geneHolder, registries)

            if (requiredGeneHolders.isNotEmpty()) {
                components.add(Component.literal("\n"))
                components.add(
                    ModLanguageProvider.Info.REQUIRED_GENES.toComponent()
                )

                for (requiredGeneHolder in requiredGeneHolders) {
                    val requiredGeneComponent = if (requiredGeneHolder.isNegative || requiredGeneHolder.isMutation) {
                        Gene.getNameComponent(requiredGeneHolder)
                    } else {
                        Gene.getNameComponent(requiredGeneHolder)
                            .withStyle { it.withColor(ChatFormatting.RESET) }
                    }

                    val line = Component.literal("• ").append(requiredGeneComponent)
                    components.add(line)

                }
            }

            val helix = DnaHelixItem.getHelixStack(geneHolder)

            val recipe = EmiInfoRecipe(
                listOf(
                    EmiIngredient.of(
                        Ingredient.of(helix)
                    )
                ),
                components.toList(),
                OtherUtil.modResource("/info/gene/${geneHolder.key!!.location().toString().replace(':', '/')}")
            )

            recipes.add(recipe)
        }

        return recipes
    }

    private fun organicMatter(): List<EmiInfoRecipe> {
        val noEntityRecipe = EmiInfoRecipe(
            listOf(
                EmiIngredient.of(
                    Ingredient.of(ModItems.ORGANIC_MATTER.toStack())
                )
            ),
            listOf(ModLanguageProvider.Info.ORGANIC_MATTER_EMPTY.toComponent()),
            OtherUtil.modResource("/info/organic_matter/no_entity")
        )

        val recipes: MutableList<EmiInfoRecipe> = mutableListOf(noEntityRecipe)

        for (entityType in EntityDnaItem.validEntityTypes) {

            val component = ModLanguageProvider.Info.ORGANIC_MATTER.toComponent(entityType.description)

            val organicMatterStack = ModItems.ORGANIC_MATTER.toStack()
            EntityDnaItem.setEntityType(organicMatterStack, entityType)

            val entityString = EntityType.getKey(entityType).toString().replace(':', '/')

            val recipe = EmiInfoRecipe(
                listOf(
                    EmiIngredient.of(
                        Ingredient.of(organicMatterStack)
                    )
                ),
                listOf(component),
                OtherUtil.modResource("/info/organic_matter/${entityString}")
            )

            recipes.add(recipe)
        }

        return recipes
    }

    private fun mobGenes(registries: HolderLookup.Provider): List<EmiInfoRecipe> {
        val recipes = mutableListOf<EmiInfoRecipe>()

        for (entityType in EntityDnaItem.validEntityTypes) {
            val geneWeights = EntityGenes.getGeneHolderWeights(entityType, registries)

            val informationTextComponent =
                ModLanguageProvider.Info.MOB_GENE_ONE.toComponent(entityType.description)

            val sumOfWeights = geneWeights.values.sum()

            for ((geneHolder, weight) in geneWeights) {
                val chance = (weight.toDouble() / sumOfWeights.toDouble() * 100).toInt()

                val geneComponent = if (geneHolder.isNegative || geneHolder.isMutation) {
                    Gene.getNameComponent(geneHolder)
                } else {
                    Gene.getNameComponent(geneHolder).withStyle { it.withColor(ChatFormatting.RESET) }
                }

                val component =
                    ModLanguageProvider.Info.MOB_GENE_TWO
                        .toComponent(chance, geneComponent)

                informationTextComponent.append(component)
            }

            val organicMatterStack = ModItems.ORGANIC_MATTER.toStack()
            EntityDnaItem.setEntityType(organicMatterStack, entityType)

            val mobSpawnEgg = SpawnEggItem.byId(entityType)

            val list = buildList {
                add(organicMatterStack)

                if (mobSpawnEgg != null) {
                    add(mobSpawnEgg.defaultInstance)
                }
            }

            val entityString = EntityType.getKey(entityType).toString().replace(':', '/')

            val recipe = EmiInfoRecipe(
                list.map { EmiIngredient.of(Ingredient.of(it)) },
                listOf(informationTextComponent),
                OtherUtil.modResource("/info/mob_genes/$entityString")
            )

            recipes.add(recipe)
        }

        return recipes
    }

}