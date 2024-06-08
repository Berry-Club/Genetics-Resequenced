package dev.aaronhowser.mods.geneticsresequenced.recipes.machine

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

/**
 * Note that these "recipes" are only used for JEI compatibility. What the machines actually do is hardcoded.
 *
 * See [dev.aaronhowser.mods.geneticsresequenced.blocks.machines.cell_analyzer.CellAnalyzerBlockEntity.craftItem]
 */
class CellAnalyzerRecipe(
    private val entityResourceLocation: ResourceLocation
) : Recipe<Container> {

    private val inputItem = ItemStack(ModItems.ORGANIC_MATTER.get()).setMob(entityResourceLocation) ?: ItemStack.EMPTY
    private val outputItem = ItemStack(ModItems.CELL.get()).setMob(entityResourceLocation) ?: ItemStack.EMPTY

    companion object {
        fun getAllRecipes(): List<CellAnalyzerRecipe> {
            return ForgeRegistries.ENTITY_TYPES.values
                .filter { it.category != MobCategory.MISC }
                .mapNotNull { ForgeRegistries.ENTITY_TYPES.getKey(it) }
                .map { CellAnalyzerRecipe(it) }
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

    }

    override fun getId(): ResourceLocation =
        OtherUtil.modResource("$RECIPE_TYPE_NAME/${entityResourceLocation.toString().replace(':', '/')}")

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        if (pLevel.isClientSide) return false

        return ForgeRegistries.ENTITY_TYPES.containsKey(entityResourceLocation)
    }

    override fun assemble(pContainer: Container): ItemStack = outputItem.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputItem.copy()

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(inputItem)
        return NonNullList.of(i, i)
    }

}