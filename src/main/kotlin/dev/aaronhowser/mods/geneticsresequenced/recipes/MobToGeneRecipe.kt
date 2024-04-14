package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class MobToGeneRecipe(
    private val mobString: String,
    private val geneString: String
) : Recipe<Container> {

    companion object {
        const val RECIPE_TYPE_NAME = "mob_to_gene"

        val RECIPE_TYPE = object : RecipeType<MobToGeneRecipe> {}

        val SERIALIZER = object : RecipeSerializer<MobToGeneRecipe> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): MobToGeneRecipe {
                val mobString = pSerializedRecipe.get("mob").asString
                val geneString = pSerializedRecipe.get("gene").asString

                return MobToGeneRecipe(mobString, geneString)
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): MobToGeneRecipe? {
                val mobString = pBuffer.readUtf()
                val geneString = pBuffer.readUtf()

                return MobToGeneRecipe(mobString, geneString)
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: MobToGeneRecipe) {
                pBuffer.writeUtf(pRecipe.mobString)
                pBuffer.writeUtf(pRecipe.geneString)
            }
        }
    }

    override fun matches(pContainer: Container, pLevel: Level): Boolean {

        if (pLevel.isClientSide) return false

        TODO("Not yet implemented")
    }

    override fun assemble(pContainer: Container): ItemStack {
        TODO("Not yet implemented")
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getResultItem(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getId(): ResourceLocation {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> {
        TODO("Not yet implemented")
    }

    override fun getType(): RecipeType<*> {
        TODO("Not yet implemented")
    }
}