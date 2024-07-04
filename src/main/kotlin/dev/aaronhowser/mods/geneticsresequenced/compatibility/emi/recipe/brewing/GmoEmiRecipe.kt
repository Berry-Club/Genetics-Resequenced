package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.ModEmiPlugin
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.crafting.Ingredient

class GmoEmiRecipe(
    val entityType: EntityType<*>,
    ingredientItem: Item,
    val idealGene: Gene,
    val geneChance: Float,
    isMutation: Boolean
) : AbstractEmiBrewingRecipe() {

    companion object {
        fun getAllRecipes(): List<GmoEmiRecipe> {
            val allRegularGmoRecipes = BrewingRecipes.allRecipes.filterIsInstance<GmoRecipe>()

            return allRegularGmoRecipes.map {
                GmoEmiRecipe(
                    it.entityType,
                    it.ingredientItem,
                    it.idealGene,
                    it.geneChance,
                    it.isMutation
                )
            }
        }
    }

    override val ingredient: EmiIngredient = EmiIngredient.of(Ingredient.of(ingredientItem))
    override val input: EmiIngredient
    override val output: EmiStack
    override val tertiaryItem: EmiStack

    override fun getCategory(): EmiRecipeCategory {
        return ModEmiPlugin.GMO_CATEGORY
    }

    init {
        val chanceString = String.format("%.2f%%", geneChance * 100)

        val loreComponent = ItemLore(
            listOf(
                Component.literal(""),
                ModLanguageProvider.Tooltips.GMO_LINE_ONE
                    .toComponent(chanceString, idealGene.nameComponent)
                    .withColor(ChatFormatting.GRAY),
                ModLanguageProvider.Tooltips.GMO_LINE_TWO
                    .toComponent(ModGenes.BASIC.get().nameComponent)
                    .withColor(ChatFormatting.GRAY)
            )
        )

        val requiredPotion = if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH
        val potionStack = OtherUtil.getPotionStack(requiredPotion)
        EntityDnaItem.setEntityType(potionStack, entityType)
        potionStack.set(DataComponents.LORE, loreComponent)
        input = EmiIngredient.of(Ingredient.of(potionStack))

        val gmoStack = ModItems.GMO_CELL.toStack()
        EntityDnaItem.setEntityType(gmoStack, entityType)
        DnaHelixItem.setGene(gmoStack, idealGene)
        gmoStack.set(DataComponents.LORE, loreComponent)

        output = EmiStack.of(gmoStack)

        val chorusStack = Items.CHORUS_FRUIT.itemStack
        val chorusLore = ItemLore(
            listOf(
                Component.literal(""),
                ModLanguageProvider.Tooltips.GMO_CHORUS
                    .toComponent()
                    .withColor(ChatFormatting.GRAY)
            )
        )
        chorusStack.set(DataComponents.LORE, chorusLore)
        tertiaryItem = EmiStack.of(chorusStack)
    }

    override fun getId(): ResourceLocation {
        val entityTypeString = EntityType.getKey(entityType).toString().replace(':', '/')
        val geneString = idealGene.id.toString().replace(':', '/')

        return OtherUtil.modResource("/gmo/$entityTypeString/$geneString")
    }

}