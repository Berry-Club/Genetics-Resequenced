package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories

import dev.aaronhowser.mods.geneticsresequenced.blocks.ModBlocks
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.recipes.machine.PlasmidInjectorRecipe
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

class PlasmidInjectorRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<PlasmidInjectorRecipe> {

    companion object {
        val recipeType: RecipeType<PlasmidInjectorRecipe> =
            RecipeType(
                OtherUtil.modResource(PlasmidInjectorRecipe.RECIPE_TYPE_NAME),
                PlasmidInjectorRecipe::class.java
            )

        val UID = OtherUtil.modResource(PlasmidInjectorRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/two_to_one.png")
    }

    private val background: IDrawable = helper.createDrawable(TEXTURE, 0, 0, 100, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModBlocks.PLASMID_INJECTOR.get()))

    override fun getRecipeType(): RecipeType<PlasmidInjectorRecipe> = GeneticsResequencedJeiPlugin.PLASMID_INJECTOR_TYPE

    override fun getTitle(): Component = Component.translatable("block.geneticsresequenced.plasmid_injector")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: PlasmidInjectorRecipe, focuses: IFocusGroup) {

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
        recipe: PlasmidInjectorRecipe,
        recipeSlotsView: IRecipeSlotsView,
        mouseX: Double,
        mouseY: Double
    ): MutableList<Component> {
        return mutableListOf(
            Component.translatable(
                if (recipe.isAntiGene) {
                    "recipe.geneticsresequenced.plasmid_injector.anti_genes"
                } else {
                    "recipe.geneticsresequenced.plasmid_injector.genes"
                }
            )
        )
    }

}