package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

class GmoEmiRecipe(
    val entityType: EntityType<*>,
    ingredientItem: Item,
    val idealGene: Gene,
    val geneChance: Float,
    isMutation: Boolean
) : EmiRecipe {

    companion object {
        fun getAllRecipes(): List<GmoEmiRecipe> {
            val allRegularGmoRecipes = BrewingRecipes.allRecipes.filterIsInstance<GmoRecipe>()

            return allRegularGmoRecipes.map {
                GmoEmiRecipe(
                    it.entityType,
                    it.ingredientItem,
                    it.idealGene,
                    it.geneChance,
                    it.isMutation
                )
            }
        }
    }

    private val ingredient: EmiIngredient = EmiIngredient.of(Ingredient.of(ingredientItem))
    private val input: EmiIngredient
    private val goodOutput: EmiStack
    private val badOutput: EmiStack

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.GMO_CATEGORY
    }

    init {
        val requiredPotion = if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH
        val potionStack = OtherUtil.getPotionStack(requiredPotion)

        EntityDnaItem.setEntityType(potionStack, entityType)
        input = EmiIngredient.of(Ingredient.of(potionStack))

        val gmoStack = ModItems.GMO_CELL.toStack()
        EntityDnaItem.setEntityType(gmoStack, entityType)
        DnaHelixItem.setGene(gmoStack, idealGene)
        goodOutput = EmiStack.of(gmoStack)

        val failCell = ModItems.GMO_CELL.toStack()
        EntityDnaItem.setEntityType(failCell, entityType)
        DnaHelixItem.setGene(failCell, ModGenes.BASIC.get())
        badOutput = EmiStack.of(failCell)
    }

    private val tooltips: List<Component> = listOf(
        ModLanguageProvider.Tooltips.GMO_TEMPERATURE_REQUIREMENT
            .toComponent()
            .withColor(ChatFormatting.GRAY),
        CommonComponents.EMPTY,
        ModLanguageProvider.Tooltips.GMO_CHORUS
            .toComponent()
            .withColor(ChatFormatting.GRAY)
    )

    override fun getId(): ResourceLocation {
        val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')
        val geneString = idealGene.id.toString().replace(':', '/')

        return OtherUtil.modResource("/gmo/$entityTypeString/$geneString")
    }

    override fun addWidgets(widgets: WidgetHolder) {

        val buffer = 5
        val slotSize = 18
        var x = 0

        x += 5
        widgets.addSlot(input, x, displayHeight / 4)
        x += slotSize + buffer

        widgets.addSlot(ingredient, x, displayHeight / 4)
        x += slotSize + buffer / 2

        widgets.addTexture(EmiTexture.EMPTY_ARROW, x, displayHeight / 3 - 4)

        x += EmiTexture.EMPTY_ARROW.width + buffer / 2

        widgets.addSlot(goodOutput, x, 2)
            .recipeContext(this)
            .appendTooltip(
                ModLanguageProvider.Tooltips.GMO_SUCCESS
                    .toComponent()
                    .withColor(ChatFormatting.GREEN)
            )

        widgets.addSlot(badOutput, x, 2 + 18 + 2)
            .recipeContext(this)
            .appendTooltip(
                ModLanguageProvider.Tooltips.GMO_FAILURE
                    .toComponent()
                    .withColor(ChatFormatting.RED)
            )

        x += slotSize + buffer

        val successChance = (geneChance * 100).toInt()

        widgets.addText(
            Component.literal("Success: $successChance%").withColor(ChatFormatting.GREEN),
            x,
            6,
            0x000000,
            true
        )

        val failureChance = 100 - successChance

        widgets.addText(
            Component.literal("Failure: $failureChance%").withColor(ChatFormatting.RED),
            x,
            6 + 18 + 2,
            0x000000,
            true
        )

        widgets.addTooltipText(tooltips, 0, 0, displayWidth, displayHeight)
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(input, ingredient)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(goodOutput, badOutput)
    }

    override fun getDisplayWidth(): Int {
        val leftSide = 5 + 18 + 5 + 18 + (5 / 2)
        val rightSide = 18 + 5 + 18 + 5

        val textWidth = 50

        return leftSide + EmiTexture.EMPTY_ARROW.width + rightSide + textWidth
    }

    override fun getDisplayHeight(): Int {
        return 2 + 18 + 2 + 18 + 2
    }

}