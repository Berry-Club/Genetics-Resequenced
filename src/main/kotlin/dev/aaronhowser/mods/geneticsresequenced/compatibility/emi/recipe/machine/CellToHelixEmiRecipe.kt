package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.machine

import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isGene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
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
import net.minecraft.world.item.crafting.RecipeManager
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class CellToHelixEmiRecipe(
    val cellStack: ItemStack,
    val helixStack: ItemStack
) : EmiRecipe {

    companion object {
        fun getAllRecipes(recipeManager: RecipeManager): List<CellToHelixEmiRecipe> {
            val recipes = mutableListOf<CellToHelixEmiRecipe>()

            val validEntityTypes = EntityDnaItem.validEntityTypes
            for (entityType in validEntityTypes) {
                val cellStack = ModItems.CELL.toStack()
                EntityDnaItem.setEntityType(cellStack, entityType)

                val helixStack = ModItems.DNA_HELIX.toStack()
                EntityDnaItem.setEntityType(helixStack, entityType)

                recipes.add(CellToHelixEmiRecipe(cellStack, helixStack))
            }

            val allGmoRecipes = GmoRecipe.getGmoRecipes(recipeManager)
            for (recipe in allGmoRecipes) {
                val entityType = recipe.value.entityType
                val goodGeneRk = recipe.value.idealGeneRk

                val goodGmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(
                    goodGmoStack,
                    entityType,
                    goodGeneRk.getHolderOrThrow(ClientUtil.localRegistryAccess!!)
                )

                val goodHelix = DnaHelixItem.getHelixStack(goodGeneRk, ClientUtil.localRegistryAccess!!)

                recipes.add(CellToHelixEmiRecipe(goodGmoStack, goodHelix))

                val recipeAlreadyExists = recipes.any {
                    EntityDnaItem.getEntityType(it.cellStack) == entityType
                            && DnaHelixItem.getGeneHolder(it.helixStack).isGene(ModGenes.BASIC)
                }

                if (recipeAlreadyExists) continue

                val badGmoStack = ModItems.GMO_CELL.toStack()
                GmoCell.setDetails(
                    badGmoStack,
                    entityType,
                    ModGenes.BASIC.getHolderOrThrow(ClientUtil.localRegistryAccess!!)
                )

                val badHelix = DnaHelixItem.getHelixStack(ModGenes.BASIC, ClientUtil.localRegistryAccess!!)

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

        val stringBuilder = StringBuilder()
            .append("/cell_to_helix/")

        val entityType = EntityDnaItem.getEntityType(cellStack) ?: error("Invalid entity type")
        val entityTypeRl = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
        val entityString = entityTypeRl.toString().replace(':', '/')

        stringBuilder.append(entityString)

        if (cellStack.item == ModItems.GMO_CELL.get()) {
            stringBuilder.append("/gmo/")

            val geneHolder =
                DnaHelixItem.getGeneHolder(helixStack) ?: error("Invalid gene")
            val geneString = geneHolder.key!!.location().toString().replace(':', '/')

            stringBuilder.append(geneString)
        }

        return OtherUtil.modResource(stringBuilder.toString())
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
        widgets.addSlot(helix, 58, 0).recipeContext(this)
    }
}