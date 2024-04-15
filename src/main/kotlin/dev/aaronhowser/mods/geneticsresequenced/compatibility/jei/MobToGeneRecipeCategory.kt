package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.MobToGeneRecipe
import mezz.jei.api.constants.VanillaTypes
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawable
import mezz.jei.api.gui.drawable.IDrawableBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeIngredientRole
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.IRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class MobToGeneRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<MobToGeneRecipe> {

    companion object {
        val UID = ResourceLocation(GeneticsResequenced.ID, MobToGeneRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = ResourceLocation(GeneticsResequenced.ID, "textures/gui/jei/cell_analyzer.png")
    }

    private val background: IDrawableBuilder = helper.drawableBuilder(TEXTURE, 0, 0, 75, 28).setTextureSize(75, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModItems.DNA_HELIX))

    override fun getRecipeType(): RecipeType<MobToGeneRecipe> = GeneticsResequencedJeiPlugin.MOB_TO_GENE_TYPE

    override fun getTitle(): Component = Component.translatable("aaaaaaaaaaaa")

    override fun getBackground(): IDrawable = background.build()

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: MobToGeneRecipe, focuses: IFocusGroup) {
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