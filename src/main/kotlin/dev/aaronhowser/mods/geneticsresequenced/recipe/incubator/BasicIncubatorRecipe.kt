package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class BasicIncubatorRecipe(
    topIngredient: Ingredient,
    bottomIngredient: Ingredient,
    val outputStack: ItemStack,
    val isLowTemp: Boolean
) : AbstractIncubatorRecipe(topIngredient, bottomIngredient) {

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val topItem = input.getTopItem()
        val bottomItem = input.getBottomItem()

        return isLowTemp == input.isLowTemp
                && topIngredient.test(topItem)
                && bottomIngredient.test(bottomItem)
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        return getResultItem(lookup)
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        return outputStack.copy()
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.BASIC_INCUBATOR.get()
    }

    class Serializer : RecipeSerializer<BasicIncubatorRecipe> {
        override fun codec(): MapCodec<BasicIncubatorRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, BasicIncubatorRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<BasicIncubatorRecipe> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        Ingredient.CODEC_NONEMPTY
                            .fieldOf("top_slot")
                            .forGetter(BasicIncubatorRecipe::topIngredient),
                        Ingredient.CODEC_NONEMPTY
                            .fieldOf("bottom_slot")
                            .forGetter(BasicIncubatorRecipe::bottomIngredient),
                        ItemStack.CODEC
                            .fieldOf("output")
                            .forGetter(BasicIncubatorRecipe::outputStack),
                        Codec.BOOL
                            .fieldOf("is_low_temperature")
                            .forGetter(BasicIncubatorRecipe::isLowTemp)
                    ).apply(instance, ::BasicIncubatorRecipe)
                }

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BasicIncubatorRecipe> =
                StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, BasicIncubatorRecipe::topIngredient,
                    Ingredient.CONTENTS_STREAM_CODEC, BasicIncubatorRecipe::bottomIngredient,
                    ItemStack.STREAM_CODEC, BasicIncubatorRecipe::outputStack,
                    ByteBufCodecs.BOOL, BasicIncubatorRecipe::isLowTemp,
                    ::BasicIncubatorRecipe
                )
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getBasicRecipes(recipeManager: RecipeManager): List<RecipeHolder<BasicIncubatorRecipe>> {
            val incubatorRecipes = getIncubatorRecipes(recipeManager)

            return incubatorRecipes.mapNotNull { if (it.value is BasicIncubatorRecipe) it as? RecipeHolder<BasicIncubatorRecipe> else null }
        }
    }

}