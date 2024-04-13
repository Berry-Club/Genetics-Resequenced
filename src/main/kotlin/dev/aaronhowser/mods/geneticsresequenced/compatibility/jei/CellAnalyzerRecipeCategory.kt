package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.recipes.CellAnalyzerRecipe
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class CellAnalyzerRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<CellAnalyzerRecipe> {

    companion object {
        val UID = ResourceLocation(GeneticsResequenced.ID, CellAnalyzerRecipe.RECIPE_TYPE_NAME)
        val TEXTURE = ResourceLocation(GeneticsResequenced.ID, "textures/gui/jei/cell_analyzer.png")
    }

    private val background: IDrawable = helper.createDrawable(TEXTURE, 0, 0, 100, 100)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModBlocks.CELL_ANALYZER))

    override fun getRecipeType(): RecipeType<CellAnalyzerRecipe> = GeneticsResequencedJeiPlugin.CELL_ANALYZER_TYPE

    override fun getTitle(): Component = Component.literal("Cell Analyzer")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: CellAnalyzerRecipe, focuses: IFocusGroup) {
        builder.addSlot(
            RecipeIngredientRole.INPUT,
            86,
            15
        ).addIngredients(recipe.ingredients.first())

        builder.addSlot(
            RecipeIngredientRole.OUTPUT,
            86,
            55
        ).addItemStack(recipe.resultItem)
    }
}