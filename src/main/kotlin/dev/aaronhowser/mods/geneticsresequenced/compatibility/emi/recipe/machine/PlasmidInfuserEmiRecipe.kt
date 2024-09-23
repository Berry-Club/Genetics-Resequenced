package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isHidden
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class PlasmidInfuserEmiRecipe(
    val geneHolder: Holder<Gene>,
    val basic: Boolean
) : EmiRecipe {

    companion object {
        fun getAllRecipes(): List<PlasmidInfuserEmiRecipe> {
            val recipes = mutableListOf<PlasmidInfuserEmiRecipe>()

            for (geneHolder in GeneRegistry
                .getRegistrySorted(ClientUtil.localRegistryAccess!!)
                .filterNot { it.isHidden }
            ) {
                recipes.add(PlasmidInfuserEmiRecipe(geneHolder, basic = true))
                recipes.add(PlasmidInfuserEmiRecipe(geneHolder, basic = false))
            }

            return recipes.distinctBy { it.id }
        }
    }

    private val helix: EmiIngredient
    private val plasmid: EmiStack

    init {
        val stackSize = maxOf(
            1, if (basic) {
                geneHolder.value().dnaPointsRequired
            } else {
                geneHolder.value().dnaPointsRequired / 2
            }
        )

        val helixStack = ModItems.DNA_HELIX.toStack(stackSize)

        DnaHelixItem.setGeneHolder(
            helixStack, if (basic) {
                ModGenes.BASIC.getHolder(GeneticsResequenced.registryAccess!!)!!
            } else {
                geneHolder
            }
        )
        helix = EmiIngredient.of(Ingredient.of(helixStack))

        val plasmidStack = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(plasmidStack, geneHolder, geneHolder.value().dnaPointsRequired)
        plasmid = EmiStack.of(plasmidStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.PLASMID_INFUSER_CATEGORY
    }

    override fun getId(): ResourceLocation {
        val geneString = geneHolder.key!!.location().toString().replace(':', '/')
        val basicString = if (basic) "/basic" else ""

        return OtherUtil.modResource("/plasmid_infuser/$geneString$basicString")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(helix)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(plasmid)
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    private val tooltips: List<Component> = listOf(
        ModLanguageProvider.Recipe.REQUIRES_POINTS
            .toComponent(
                Gene
                    .getNameComponent(geneHolder)
                    .withColor(ChatFormatting.GRAY),
                geneHolder
                    .value()
                    .dnaPointsRequired
            )
            .withColor(ChatFormatting.GRAY),
        ModLanguageProvider.Recipe.BASIC_WORTH
            .toComponent()
            .withColor(ChatFormatting.GRAY),
        ModLanguageProvider.Recipe.MATCHING_WORTH
            .toComponent()
            .withColor(ChatFormatting.GRAY)
    )

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(helix, 0, 0)
        widgets.addSlot(plasmid, 58, 0).recipeContext(this)

        widgets.addTooltipText(tooltips, 0, 0, 76, 18)
    }
}