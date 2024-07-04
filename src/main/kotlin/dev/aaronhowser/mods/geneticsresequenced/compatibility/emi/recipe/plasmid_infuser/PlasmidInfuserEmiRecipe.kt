package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.plasmid_infuser

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.crafting.Ingredient

class PlasmidInfuserEmiRecipe(
    val gene: Gene,
    val basic: Boolean
) : EmiRecipe {

    private val helix: EmiIngredient
    private val plasmid: EmiStack

    init {
        val stackSize = if (basic) gene.dnaPointsRequired else gene.dnaPointsRequired / 2
        val helixStack = ModItems.DNA_HELIX.toStack(stackSize)

        val loreComponent = ItemLore(
            listOf(
                Component.literal(""),
                ModLanguageProvider.Recipe.REQUIRES_POINTS
                    .toComponent(gene.nameComponent.withColor(ChatFormatting.GRAY), gene.dnaPointsRequired)
                    .withColor(ChatFormatting.GRAY),
                ModLanguageProvider.Recipe.BASIC_WORTH
                    .toComponent()
                    .withColor(ChatFormatting.GRAY),
                ModLanguageProvider.Recipe.MATCHING_WORTH
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
        )

        DnaHelixItem.setGene(helixStack, if (basic) ModGenes.BASIC.get() else gene)
        helixStack.set(DataComponents.LORE, loreComponent)
        helix = EmiIngredient.of(Ingredient.of(helixStack))

        val plasmidStack = ModItems.PLASMID.toStack()
        PlasmidItem.setGene(plasmidStack, gene, gene.dnaPointsRequired)
        plasmid = EmiStack.of(plasmidStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.PLASMID_INFUSER_CATEGORY
    }

    override fun getId(): ResourceLocation {
        val geneString = gene.id.toString().replace(':', '/')
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

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(helix, 0, 0)
        widgets.addSlot(plasmid, 58, 0).recipeContext(this);
    }
}