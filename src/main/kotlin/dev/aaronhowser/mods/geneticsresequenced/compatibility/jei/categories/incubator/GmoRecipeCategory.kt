package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories.incubator

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.genes.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei.GmoRecipePage
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class GmoRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<GmoRecipePage> {

    companion object {

        val recipeType: RecipeType<GmoRecipePage> =
            RecipeType(
                OtherUtil.modResource(GmoRecipePage.RECIPE_TYPE_NAME),
                GmoRecipePage::class.java
            )

        val UID = OtherUtil.modResource(GmoRecipePage.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/two_to_one.png")
    }

    private val background: IDrawable = helper.createDrawable(TEXTURE, 0, 0, 100, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModItems.GMO_CELL.get()))

    override fun getRecipeType(): RecipeType<GmoRecipePage> = GeneticsResequencedJeiPlugin.GMO_RECIPE_TYPE

    override fun getTitle(): Component = Component.translatable("recipe.geneticsresequenced.gmo")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: GmoRecipePage, focuses: IFocusGroup) {

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

    override fun getTooltipStrings(
        recipe: GmoRecipePage,
        recipeSlotsView: IRecipeSlotsView,
        mouseX: Double,
        mouseY: Double
    ): MutableList<Component> {
        val chanceGood = recipe.recipe.geneChance
        val chanceGoodString = (chanceGood * 100).toInt().toString() + '%'

        val goodGene = recipe.recipe.idealGene

        val line1 =
            Component.translatable(
                "tooltip.geneticsresequenced.gmo_recipe.line1",
                chanceGoodString,
                goodGene.nameComponent
            )

        val line2 = Component.translatable(
            "tooltip.geneticsresequenced.gmo_recipe.line2",
            ModGenes.basic.nameComponent
        )

        val line3 = Component.translatable("tooltip.geneticsresequenced.gmo_recipe.line3")

        return mutableListOf(
            line1,
            Component.literal("\n"),
            line2,
            Component.literal("\n"),
            line3
        )

    }

}