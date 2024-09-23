package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.GmoRecipe
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
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class CellToHelixEmiRecipe(
    val cellStack: ItemStack,
    val helixStack: ItemStack
) : EmiRecipe {

    companion object {
        fun getAllRecipes(): List<CellToHelixEmiRecipe> {
            val recipes = mutableListOf<CellToHelixEmiRecipe>()

            val validEntityTypes = EntityDnaItem.validEntityTypes
            for (entityType in validEntityTypes) {
                val cellStack = ModItems.CELL.toStack()
                EntityDnaItem.setEntityType(cellStack, entityType)

                val helixStack = ModItems.DNA_HELIX.toStack()
                EntityDnaItem.setEntityType(helixStack, entityType)

                recipes.add(CellToHelixEmiRecipe(cellStack, helixStack))
            }

            val allGmoRecipes = BrewingRecipes.allRecipes.filterIsInstance<GmoRecipe>()
            for (recipe in allGmoRecipes) {
                val entityType = recipe.entityType
                val goodGeneRk = recipe.idealGeneRk

                val goodGmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(
                    goodGmoStack,
                    entityType,
                    goodGeneRk
                )

                val goodHelix = ModItems.DNA_HELIX.toStack()
                DnaHelixItem.setGeneHolder(goodHelix, goodGeneRk.getHolder(GeneticsResequenced.registryAccess)!!)

                recipes.add(CellToHelixEmiRecipe(goodGmoStack, goodHelix))

                if (recipes.any {
                        EntityDnaItem.getEntityType(it.cellStack) == entityType
                                && DnaHelixItem.getGeneHolder(
                            it.helixStack
                        ) == ModGenes.BASIC
                    }) {
                    continue
                }

                val badGmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(
                    badGmoStack,
                    entityType,
                    ModGenes.BASIC
                )

                val badHelix = ModItems.DNA_HELIX.toStack()
                DnaHelixItem.setBasic(badHelix, GeneticsResequenced.registryAccess)

                recipes.add(CellToHelixEmiRecipe(badGmoStack, badHelix))
            }

            return recipes.distinctBy { it.id }
        }
    }

    private val cell: EmiIngredient = EmiIngredient.of(DataComponentIngredient.of(false, cellStack))
    private val helix: EmiStack = EmiStack.of(helixStack)

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.DNA_EXTRACTOR_CATEGORY
    }

    override fun getId(): ResourceLocation {
        var string = "cell_to_helix/"

        val entityType = EntityDnaItem.getEntityType(cellStack) ?: error("Invalid entity type")
        val entityTypeRl = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
        val entityString = entityTypeRl.toString().replace(':', '/')

        string += entityString

        if (cellStack.item == ModItems.GMO_CELL.get()) {
            string += "/gmo/"

            val geneHolder =
                DnaHelixItem.getGeneHolder(helixStack) ?: error("Invalid gene")
            val geneString = geneHolder.key!!.location().toString().replace(':', '/')

            string += geneString
        }

        return OtherUtil.modResource(string)
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(cell)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(helix)
    }

    override fun getDisplayWidth(): Int {
        return 76
    }

    override fun getDisplayHeight(): Int {
        return 18
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
        widgets.addSlot(cell, 0, 0)
        widgets.addSlot(helix, 58, 0).recipeContext(this);
    }
}