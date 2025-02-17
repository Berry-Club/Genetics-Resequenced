package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class OrganicMatterToCellEmiRecipe(
    val entityType: EntityType<*>
) : EmiRecipe {

    companion object {
        fun getAllRecipes(): List<OrganicMatterToCellEmiRecipe> {
            val recipes = mutableListOf<OrganicMatterToCellEmiRecipe>()

            val validEntityTypes = EntityDnaItem.validEntityTypes
            for (entityType in validEntityTypes) {
                recipes.add(OrganicMatterToCellEmiRecipe(entityType))
            }

            return recipes.distinctBy { it.id }
        }
    }

    private val organicMatter: EmiIngredient
    private val cell: EmiStack

    init {
        val matterStack = ModItems.ORGANIC_MATTER.toStack()
        EntityDnaItem.setEntityType(matterStack, entityType)

        val cellStack = ModItems.CELL.toStack()
        EntityDnaItem.setEntityType(cellStack, entityType)

        organicMatter = EmiIngredient.of(DataComponentIngredient.of(false, matterStack))
        cell = EmiStack.of(cellStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.CELL_ANALYZER_CATEGORY
    }

    override fun getId(): ResourceLocation {
        val entityTypeRl = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
        val entityString = entityTypeRl.toString().replace(':', '/')

        return OtherUtil.modResource("/cell_analyzer/$entityString")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(organicMatter)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(cell)
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(organicMatter, 0, 0)
        widgets.addSlot(cell, 58, 0).recipeContext(this)
    }
}