package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories

import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.recipes.machine.PlasmidInfuserRecipe
import dev.aaronhowser.mods.geneticsresequenced.registries.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.drawable.IDrawableStatic
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class PlasmidInfuserRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<PlasmidInfuserRecipe> {

    companion object {
        val recipeType: RecipeType<PlasmidInfuserRecipe> =
            RecipeType(
                OtherUtil.modResource(PlasmidInfuserRecipe.RECIPE_TYPE_NAME),
                PlasmidInfuserRecipe::class.java
            )

        val UID = OtherUtil.modResource(PlasmidInfuserRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/plasmid_infuser.png")
    }

    private val background: IDrawableStatic = helper.createDrawable(TEXTURE, 57, 30, 75, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModBlocks.PLASMID_INFUSER.get()))

    override fun getRecipeType(): RecipeType<PlasmidInfuserRecipe> = GeneticsResequencedJeiPlugin.PLASMID_INFUSER_TYPE

    override fun getTitle(): Component = Component.translatable("block.geneticsresequenced.plasmid_infuser")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PlasmidInfuserRecipe, focuses: IFocusGroup) {
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

    override fun getTooltipStrings(
        recipe: PlasmidInfuserRecipe,
        recipeSlotsView: IRecipeSlotsView,
        mouseX: Double,
        mouseY: Double
    ): MutableList<Component> {

        val gene = recipe.gene
        val amountNeeded = gene.dnaPointsRequired

        if (amountNeeded <= 1) return mutableListOf()

        val line1 = Component
            .translatable(
                "recipe.geneticsresequenced.plasmid_infuser.points_required",
                gene.nameComponent,
                amountNeeded
            )
            .withColor(ChatFormatting.GRAY)

        val line2 = Component
            .translatable("recipe.geneticsresequenced.plasmid_infuser.basic")
            .withColor(ChatFormatting.GRAY)

        val line3 = Component
            .translatable("recipe.geneticsresequenced.plasmid_infuser.matching")
            .withColor(ChatFormatting.GRAY)

        return mutableListOf(line1, line2, line3)
    }

}