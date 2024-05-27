package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.registration.IRecipeRegistration
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
                ?: throw IllegalStateException("Failed to create ItemStack for Organic Matter")

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

}