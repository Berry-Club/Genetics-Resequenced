package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries
import mezz.jei.api.recipe.RecipeType as JEIRecipeType

class CellAnalyzerRecipe(
    private val id: ResourceLocation
) : Recipe<Container> {

    private val inputItem = ItemStack(ModItems.ORGANIC_MATTER).setMob(id) ?: ItemStack.EMPTY
    private val outputItem = ItemStack(ModItems.CELL).setMob(id) ?: ItemStack.EMPTY

    companion object {
        fun getAllRecipes(): List<CellAnalyzerRecipe> {
            return ForgeRegistries.ENTITY_TYPES.keys.map {
                CellAnalyzerRecipe(it)
            }
        }

        const val RECIPE_TYPE_NAME = "cell_analyzer"

        val RECIPE_TYPE = object : RecipeType<CellAnalyzerRecipe> {}

        val SERIALIZER = object : RecipeSerializer<CellAnalyzerRecipe> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): CellAnalyzerRecipe {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): CellAnalyzerRecipe? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: CellAnalyzerRecipe) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }

        val JEI_RECIPE_TYPE: JEIRecipeType<CellAnalyzerRecipe> =
            JEIRecipeType(
                ResourceLocation(GeneticsResequenced.ID, RECIPE_TYPE_NAME),
                CellAnalyzerRecipe::class.java
            )
    }

    override fun getId(): ResourceLocation = ResourceLocation(GeneticsResequenced.ID, RECIPE_TYPE_NAME)

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        if (pLevel.isClientSide) return false

        return ForgeRegistries.ENTITY_TYPES.containsKey(id)
    }

    override fun assemble(pContainer: Container): ItemStack = outputItem.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputItem.copy()

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(inputItem)
        return NonNullList.of(i, i)
    }

}