package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem.Companion.getPlasmid
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraftforge.registries.ForgeRegistries

object InformationRecipes {

    fun organicMatter(registration: IRecipeRegistration) {
        val allEntityTypes = ForgeRegistries.ENTITY_TYPES.values.filter { it.category != MobCategory.MISC }
        for (entityType in allEntityTypes) {
            val informationTextComponent = Component
                .translatable("info.geneticsresequenced.organic_matter", entityType.description)

            val organicMatterStack = ItemStack(ModItems.ORGANIC_MATTER.get()).setMob(entityType)

            if (organicMatterStack == null) {
                GeneticsResequenced.LOGGER.error("Failed to create organic matter stack for entity type: $entityType")
                continue
            }

            registration.addIngredientInfo(
                organicMatterStack,
                VanillaTypes.ITEM_STACK,
                informationTextComponent
            )
        }

    }

    fun mobGenes(registration: IRecipeRegistration) {
        val allMobGenePairs = MobGenesRegistry.getRegistry().entries
        for ((entityType, genes) in allMobGenePairs) {
            val informationTextComponent =
                Component.translatable("info.geneticsresequenced.mob_gene.line1", entityType.description)

            val sumOfWeights = genes.values.sum()

            for ((gene, weight) in genes) {
                val chance = (weight.toDouble() / sumOfWeights.toDouble() * 100).toInt()

                val component = Component.translatable(
                    "info.geneticsresequenced.mob_gene.line2",
                    chance,
                    gene.nameComponent
                )

                informationTextComponent.append(component)
            }

            registration.addIngredientInfo(
                ItemStack(ModItems.DNA_HELIX.get()).setMob(entityType)!!,
                VanillaTypes.ITEM_STACK,
                informationTextComponent
            )
        }
    }

    fun geneDescriptions(registration: IRecipeRegistration) {
        val registry = Gene.getRegistry()

        for (gene in registry) {
            if (gene.hidden) continue
            if (!gene.isActive) continue

            val components = mutableListOf(
                gene.nameComponent.copy().withStyle {
                    it.withColor(ChatFormatting.RESET).withUnderlined(true)
                }
            )

            val geneRl = gene.id.toString()
            val geneDescription = Component.translatable("info.geneticsresequenced.gene_description.$geneRl")

            components.add(geneDescription)

            val requiredGenes = gene.getRequiredGenes()
            if (requiredGenes.isNotEmpty()) {
                components.add(Component.literal("\n"))
                components.add(
                    Component.translatable("info.geneticsresequenced.requires_genes")
                )

                for (requiredGene in requiredGenes) {
                    val line = Component.literal("• ").append(requiredGene.nameComponent)
                    components.add(line)
                }
            }

            registration.addIngredientInfo(
                gene.getPlasmid(),
                VanillaTypes.ITEM_STACK,
                *components.toTypedArray()
            )
        }

    }

}