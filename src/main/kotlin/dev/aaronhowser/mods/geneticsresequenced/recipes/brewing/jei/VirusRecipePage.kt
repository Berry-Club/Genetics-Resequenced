package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.VirusRecipe
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registries.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class VirusRecipePage(
    private val recipe: VirusRecipe
) : Recipe<Container> {

    private val viralAgentsStack = PotionUtils.setPotion(ItemStack(Items.POTION), ModPotions.VIRAL_AGENTS)
    private val inputDnaStack = ModItems.DNA_HELIX.itemStack.setGene(recipe.inputDnaGene)
    private val outputDnaStack = ModItems.DNA_HELIX.itemStack.setGene(recipe.outputGene)

    companion object {
        fun getAllRecipes(): List<VirusRecipePage> {

            val allPotionRecipes = BrewingRecipes.allRecipes
            val viralRecipes = allPotionRecipes.filterIsInstance<VirusRecipe>()

            return viralRecipes.map { VirusRecipePage(it) }
        }

        const val RECIPE_TYPE_NAME = "virus_recipe"

        val RECIPE_TYPE = object : RecipeType<VirusRecipePage> {}

        val SERIALIZER = object : RecipeSerializer<VirusRecipePage> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): VirusRecipePage {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): VirusRecipePage? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: VirusRecipePage) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }

    }

    override fun getId(): ResourceLocation {
        val inputGenString = recipe.inputDnaGene.id.toString().replace(":", "_")
        val outputGenString = recipe.outputGene.id.toString().replace(":", "_")

        return OtherUtil.modResource("$RECIPE_TYPE_NAME/$inputGenString/to/$outputGenString")
    }

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        return !pLevel.isClientSide
    }

    override fun assemble(pContainer: Container): ItemStack = outputDnaStack.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputDnaStack.copy()

    override fun getIngredients(): NonNullList<Ingredient> {

        val first = Ingredient.of(viralAgentsStack)
        val second = Ingredient.of(inputDnaStack)

        // WHY THE HELL DOES NonNullList.of WORK LIKE THAT??
        return NonNullList.of(first, first, second)
    }

}