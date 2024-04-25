package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.recipes.PlasmidInfuserRecipe
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
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class PlasmidInfuserRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<PlasmidInfuserRecipe> {

    companion object {
        val recipeType: RecipeType<PlasmidInfuserRecipe> =
            RecipeType(
                ResourceLocation(GeneticsResequenced.ID, PlasmidInfuserRecipe.RECIPE_TYPE_NAME),
                PlasmidInfuserRecipe::class.java
            )

        val UID = ResourceLocation(GeneticsResequenced.ID, PlasmidInfuserRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = ResourceLocation(GeneticsResequenced.ID, "textures/gui/plasmid_infuser.png")
    }

    private val background: IDrawableStatic = helper.createDrawable(TEXTURE, 57, 30, 75, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModBlocks.PLASMID_INFUSER))

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
        val amountNeeded = gene.amountNeeded

        val line1 = Component
            .translatable("recipe.geneticsresequenced.plasmid_infuser.points_required", gene.nameComponent, amountNeeded)
            .withStyle { it.withColor(ChatFormatting.GRAY) }

        val line2 = Component
            .translatable("recipe.geneticsresequenced.plasmid_infuser.basic")
            .withStyle { it.withColor(ChatFormatting.GRAY) }

        val line3 = Component
            .translatable("recipe.geneticsresequenced.plasmid_infuser.matching")
            .withStyle { it.withColor(ChatFormatting.GRAY) }

        return mutableListOf(line1, line2, line3)
    }

}