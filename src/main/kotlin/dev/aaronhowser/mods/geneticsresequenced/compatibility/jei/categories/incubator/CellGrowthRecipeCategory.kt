package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.CellGrowthRecipePage
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
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils

class CellGrowthRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<CellGrowthRecipePage> {

    companion object {

        val recipeType: RecipeType<CellGrowthRecipePage> =
            RecipeType(
                OtherUtil.modResource(CellGrowthRecipePage.RECIPE_TYPE_NAME),
                CellGrowthRecipePage::class.java
            )

        val UID = OtherUtil.modResource(CellGrowthRecipePage.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/two_to_one.png")
    }

    private val background: IDrawable = helper.createDrawable(TEXTURE, 0, 0, 100, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(
            VanillaTypes.ITEM_STACK,
            PotionUtils.setPotion(ItemStack(Items.POTION), ModPotions.CELL_GROWTH)
        )

    override fun getRecipeType(): RecipeType<CellGrowthRecipePage> =
        GeneticsResequencedJeiPlugin.CELL_GROWTH_RECIPE_TYPE

    override fun getTitle(): Component = Component.translatable("recipe.geneticsresequenced.cell_growth")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: CellGrowthRecipePage, focuses: IFocusGroup) {

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