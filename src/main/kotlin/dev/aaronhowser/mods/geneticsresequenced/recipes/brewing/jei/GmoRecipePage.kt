package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.GmoItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class GmoRecipePage(
    private val entityType: EntityType<*>,
    private val ingredientInput: ItemStack,
    private val cellOutput: ItemStack
) : Recipe<Container> {

    private val cellGrowthPotion: ItemStack
        get() {
            val potion = ItemStack(Items.POTION)
            PotionUtils.setPotion(potion, ModPotions.CELL_GROWTH)
            potion.setMob(entityType)
            return potion
        }

    companion object {
        fun getAllRecipes(): List<GmoRecipePage> {

            val allPotionRecipes = ModPotions.allRecipes
            val gmoRecipes = allPotionRecipes.filterIsInstance<GmoRecipe>()

            val recipePages = mutableListOf<GmoRecipePage>()

            for (recipe in gmoRecipes) {
                val ingredient = recipe.ingredientItem.defaultInstance

                val output = ModItems.GMO_CELL.get().defaultInstance
                GmoItem.setDetails(
                    output,
                    recipe.entityType,
                    recipe.outputGene,
                    recipe.geneChance
                )

                recipePages.add(GmoRecipePage(recipe.entityType, ingredient, output))
            }

            return recipePages
        }

        const val RECIPE_TYPE_NAME = "gmo_recipe"

        val RECIPE_TYPE = object : RecipeType<GmoRecipePage> {}

        val SERIALIZER = object : RecipeSerializer<GmoRecipePage> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): GmoRecipePage {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): GmoRecipePage? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: GmoRecipePage) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }

    }

    override fun getId(): ResourceLocation {
        val entityResourceLocation = EntityType.getKey(entityType)
        val entityString = entityResourceLocation.toString().replace(":", "/")

        return OtherUtil.modResource("$RECIPE_TYPE_NAME/$entityString")
    }

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        return !pLevel.isClientSide
    }

    override fun assemble(pContainer: Container): ItemStack = cellOutput.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = cellOutput.copy()

    override fun getIngredients(): NonNullList<Ingredient> {

        val first = Ingredient.of(cellGrowthPotion)
        val second = Ingredient.of(ingredientInput)

        // WHY THE HELL DOES NonNullList.of WORK LIKE THAT??
        return NonNullList.of(first, first, second)
    }

}