package dev.aaronhowser.mods.geneticsresequenced.recipes.machine

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class BloodPurifierRecipe(
    private val usingMetalSyringe: Boolean
) : Recipe<Container> {

    private fun getInputItem(): ItemStack {

        val localPlayer = ClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")

        if (usingMetalSyringe) {
            val metalSyringe = ModItems.METAL_SYRINGE.itemStack

            SyringeItem.setEntity(
                metalSyringe,
                Cow(EntityType.COW, localPlayer.level)
            )

            return metalSyringe
        }

        val syringe = ModItems.SYRINGE.itemStack

        SyringeItem.setEntity(
            syringe,
            localPlayer
        )

        return syringe
    }

    private fun getOutputItem(): ItemStack {
        val output = getInputItem()
        SyringeItem.setContaminated(output, false)
        return output
    }

    companion object {
        fun getAllRecipes(): List<BloodPurifierRecipe> {
            return listOf(
                BloodPurifierRecipe(false),
                BloodPurifierRecipe(true)
            )
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

    override fun assemble(pContainer: Container): ItemStack = getOutputItem()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = getOutputItem()

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(getInputItem())
        return NonNullList.of(i, i)
    }

}