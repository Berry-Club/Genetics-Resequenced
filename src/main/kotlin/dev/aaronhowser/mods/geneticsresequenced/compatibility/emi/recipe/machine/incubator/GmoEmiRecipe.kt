package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine.incubator

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.CommonComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager

class GmoEmiRecipe(
    val entityType: EntityType<*>,
    ingredient: Ingredient,
    val idealResourceKey: ResourceKey<Gene>,
    val geneChance: Float,
    isMutation: Boolean
) : EmiRecipe {

    companion object {
        fun getAllRecipes(recipeManager: RecipeManager): List<GmoEmiRecipe> {
            val allRegularGmoRecipes = GmoRecipe.getGmoRecipes(recipeManager)

            return allRegularGmoRecipes.map {
                GmoEmiRecipe(
                    it.value.entityType,
                    it.value.topIngredient,
                    it.value.idealGeneRk,
                    it.value.geneChance,
                    it.value.needsMutationPotion
                )
            }
        }
    }

    private val ingredient: EmiIngredient = EmiIngredient.of(ingredient)
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
        DnaHelixItem.setGeneHolder(gmoStack, idealResourceKey.getHolderOrThrow(ClientUtil.localRegistryAccess!!))
        goodOutput = EmiStack.of(gmoStack)

        val failCell = ModItems.GMO_CELL.toStack()
        EntityDnaItem.setEntityType(failCell, entityType)
        DnaHelixItem.setGeneHolder(failCell, ModGenes.BASIC.getHolderOrThrow(ClientUtil.localRegistryAccess!!))
        badOutput = EmiStack.of(failCell)
    }

    private val tooltips: List<Component> = listOf(
        ModLanguageProvider.Tooltips.GMO_TEMPERATURE_REQUIREMENT
            .toComponent()
            .withStyle(ChatFormatting.GRAY),
        CommonComponents.EMPTY,
        ModLanguageProvider.Tooltips.GMO_CHORUS
            .toComponent()
            .withStyle(ChatFormatting.GRAY)
    )

    override fun getId(): ResourceLocation {
        val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')
        val geneString = idealResourceKey.location().toString().replace(':', '/')

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
                    .withStyle(ChatFormatting.GREEN)
            )

        widgets.addSlot(badOutput, x, 2 + 18 + 2)
            .recipeContext(this)
            .appendTooltip(
                ModLanguageProvider.Tooltips.GMO_FAILURE
                    .toComponent()
                    .withStyle(ChatFormatting.RED)
            )

        x += slotSize + buffer

        val successChance = (geneChance * 100).toInt()

        widgets.addText(
            Component.literal("Success: $successChance%").withStyle(ChatFormatting.GREEN),
            x,
            6,
            0x000000,
            true
        )

        val failureChance = 100 - successChance

        widgets.addText(
            Component.literal("Failure: $failureChance%").withStyle(ChatFormatting.RED),
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