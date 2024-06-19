package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.recipes.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.VirusRecipePage
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component

class VirusRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<VirusRecipePage> {

    companion object {

        val recipeType: RecipeType<VirusRecipePage> =
            RecipeType(
                OtherUtil.modResource(VirusRecipePage.RECIPE_TYPE_NAME),
                VirusRecipePage::class.java
            )

        val UID = OtherUtil.modResource(VirusRecipePage.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/two_to_one.png")
    }

    private val background: IDrawable = helper.createDrawable(TEXTURE, 0, 0, 100, 28)
    private val icon: IDrawable = helper.createDrawableIngredient(
        VanillaTypes.ITEM_STACK,
        BrewingRecipes.viralAgentsPotionStack
    )

    override fun getRecipeType(): RecipeType<VirusRecipePage> = GeneticsResequencedJeiPlugin.VIRUS_RECIPE_TYPE

    override fun getTitle(): Component = Component.translatable("recipe.geneticsresequenced.virus")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: VirusRecipePage, focuses: IFocusGroup) {

        val firstIngredient = recipe.ingredients[0]
        val secondIngredient = recipe.ingredients[1]

        builder.addSlot(
            RecipeIngredientRole.INPUT,
            6,
            6
        ).addIngredients(firstIngredient)

        builder.addSlot(
            RecipeIngredientRole.INPUT,
            31,
            6
        ).addIngredients(secondIngredient)

        builder.addSlot(
            RecipeIngredientRole.OUTPUT,
            78,
            6
        ).addItemStack(recipe.resultItem)
    }
}