package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient

class GmoEmiRecipe(
    val entityType: EntityType<*>,
    ingredientItem: Item,
    val idealGene: Gene,
    val geneChance: Float,
    isMutation: Boolean = false
) : AbstractEmiBrewingRecipe() {

    companion object {
        fun getGmoRecipes(): List<GmoEmiRecipe> {
            val allRegularGmoRecipes = BrewingRecipes.allRecipes.filterIsInstance<GmoRecipe>()

            return allRegularGmoRecipes.map {
                GmoEmiRecipe(
                    it.entityType,
                    it.ingredientItem,
                    it.idealGene,
                    it.geneChance
                )
            }
        }
    }

    override val ingredient: EmiIngredient = EmiIngredient.of(Ingredient.of(ingredientItem))
    override val input: EmiIngredient
    override val output: EmiStack

    init {

        if (entityType === EntityType.BLAZE) {
            println("AAAAA A BLAZE")
            if (idealGene === ModGenes.BIOLUMINESCENCE.get()) {
                println("AAAAA A BLAZE BIOLUMINESCENCE")
            }
        }

        val requiredPotion = if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH
        val potionStack = OtherUtil.getPotionStack(requiredPotion)
        EntityDnaItem.setEntityType(potionStack, entityType)
        input = EmiIngredient.of(Ingredient.of(potionStack))

        val gmoStack = ModItems.GMO_CELL.toStack()
        EntityDnaItem.setEntityType(gmoStack, entityType)
        DnaHelixItem.setGene(gmoStack, idealGene)

        output = EmiStack.of(gmoStack)
    }

    override fun getId(): ResourceLocation {
        val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')
        val geneString = idealGene.id.toString().replace(':', '/')

        return OtherUtil.modResource("/gmo/$entityTypeString/$geneString")
    }

}