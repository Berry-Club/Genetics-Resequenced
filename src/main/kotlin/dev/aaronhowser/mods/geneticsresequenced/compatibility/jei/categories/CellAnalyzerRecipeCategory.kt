package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.recipes.machine.CellAnalyzerRecipe
import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlocks
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
import net.minecraft.world.item.ItemStack

class CellAnalyzerRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<CellAnalyzerRecipe> {

    companion object {

        val recipeType: RecipeType<CellAnalyzerRecipe> =
            RecipeType(
                OtherUtil.modResource(CellAnalyzerRecipe.RECIPE_TYPE_NAME),
                CellAnalyzerRecipe::class.java
            )

        val UID = OtherUtil.modResource(CellAnalyzerRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/cell_analyzer.png")
    }

    private val background: IDrawable = helper.createDrawable(TEXTURE, 57, 30, 75, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModBlocks.CELL_ANALYZER.get()))

    override fun getRecipeType(): RecipeType<CellAnalyzerRecipe> = GeneticsResequencedJeiPlugin.CELL_ANALYZER_TYPE

    override fun getTitle(): Component = Component.translatable("block.geneticsresequenced.cell_analyzer")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: CellAnalyzerRecipe, focuses: IFocusGroup) {
        builder.addSlot(
            RecipeIngredientRole.INPUT,
            6,
            6
        ).addIngredients(recipe.ingredients.first())

        builder.addSlot(
            RecipeIngredientRole.OUTPUT,
            53,
            6
        ).addItemStack(recipe.resultItem)
    }
}