package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
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

class BloodPurifierRecipe : Recipe<Container> {

    private val inputItem = ItemStack(ModItems.SYRINGE).apply {
        SyringeItem.setEntity(
            this,
            ClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
        )
    }
    private val outputItem = inputItem.copy().apply {
        SyringeItem.setContaminated(this@apply, false)
    }

    companion object {
        fun getAllRecipes(): List<BloodPurifierRecipe> {
            return listOf(BloodPurifierRecipe())
        }

        const val RECIPE_TYPE_NAME = "blood_purifier"

        val RECIPE_TYPE = object : RecipeType<BloodPurifierRecipe> {}

        val SERIALIZER = object : RecipeSerializer<BloodPurifierRecipe> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): BloodPurifierRecipe {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): BloodPurifierRecipe? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: BloodPurifierRecipe) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }

    }

    override fun getId(): ResourceLocation =
        OtherUtil.modResource(RECIPE_TYPE_NAME)

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        return !pLevel.isClientSide
    }

    override fun assemble(pContainer: Container): ItemStack = outputItem.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputItem.copy()

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(inputItem)
        return NonNullList.of(i, i)
    }

}