package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
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
    val recipe: GmoRecipe
) : Recipe<Container> {

    private val entityType: EntityType<*> = recipe.entityType
    private val ingredientInput: ItemStack = recipe.ingredientItem.itemStack

    private val successOutput: ItemStack
        get() {
            val output = ModItems.GMO_CELL.itemStack
            GmoCell.setDetails(
                output,
                entityType,
                recipe.idealGene
            )
            return output
        }

    private val potionStack: ItemStack
        get() {
            val potion = ItemStack(Items.POTION)
            PotionUtils.setPotion(potion, recipe.requiredPotion)
            potion.setMob(entityType)
            return potion
        }

    companion object {
        fun getAllRecipes(): List<GmoRecipePage> {
            val allPotionRecipes = ModPotions.allRecipes
            val gmoRecipes = allPotionRecipes.filterIsInstance<GmoRecipe>()

            return gmoRecipes.map { GmoRecipePage(it) }
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

    override fun assemble(pContainer: Container): ItemStack = successOutput.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = successOutput.copy()

    override fun getIngredients(): NonNullList<Ingredient> {

        val first = Ingredient.of(potionStack)
        val second = Ingredient.of(ingredientInput)

        // WHY THE HELL DOES NonNullList.of WORK LIKE THAT??
        return NonNullList.of(first, first, second)
    }

}