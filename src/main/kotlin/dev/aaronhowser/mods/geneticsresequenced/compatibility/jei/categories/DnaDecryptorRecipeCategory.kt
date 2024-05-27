package dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.categories

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.jei.GeneticsResequencedJeiPlugin
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.recipes.DnaDecryptorRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
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
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class DnaDecryptorRecipeCategory(
    helper: IGuiHelper
) : IRecipeCategory<DnaDecryptorRecipe> {

    companion object {
        val recipeType: RecipeType<DnaDecryptorRecipe> =
            RecipeType(
                OtherUtil.modResource(DnaDecryptorRecipe.RECIPE_TYPE_NAME),
                DnaDecryptorRecipe::class.java
            )

        val UID = OtherUtil.modResource(DnaDecryptorRecipe.RECIPE_TYPE_NAME)
        private val TEXTURE = OtherUtil.modResource("textures/gui/dna_decryptor.png")
    }

    private val background: IDrawableStatic = helper.createDrawable(TEXTURE, 57, 30, 75, 28)
    private val icon: IDrawable =
        helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, ItemStack(ModItems.DNA_HELIX.get()))

    override fun getRecipeType(): RecipeType<DnaDecryptorRecipe> = GeneticsResequencedJeiPlugin.DNA_DECRYPTOR_TYPE

    override fun getTitle(): Component = Component.translatable("block.geneticsresequenced.dna_decryptor")

    override fun getBackground(): IDrawable = background

    override fun getIcon(): IDrawable = icon

    override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: DnaDecryptorRecipe, focuses: IFocusGroup) {
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
        recipe: DnaDecryptorRecipe,
        recipeSlotsView: IRecipeSlotsView,
        mouseX: Double,
        mouseY: Double
    ): MutableList<Component> {

        val mob = recipe.entityType
        val gene = recipe.gene
        val chance = recipe.chance

        val line1 = Component
            .translatable("recipe.geneticsresequenced.mob_gene.mob", mob.description)

        val line2 = Component
            .translatable("recipe.geneticsresequenced.mob_gene.gene", gene.nameComponent)

        val line3 = Component
            .translatable("recipe.geneticsresequenced.mob_gene.chance", chance)

        return mutableListOf(line1, line2, line3)
    }
}