package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class BasicIncubatorRecipe(
    val topSlotIngredient: Ingredient,
    val bottomSlotIngredient: Ingredient,
    val outputStack: ItemStack
) : AbstractIncubatorRecipe() {

    override val ingredients: List<Ingredient> = listOf(topSlotIngredient, bottomSlotIngredient)

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val topItem = input.getTopItem()
        val bottomItem = input.getBottomItem()

        return topSlotIngredient.test(topItem) && bottomSlotIngredient.test(bottomItem)
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        return getResultItem(lookup)
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        return outputStack
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.BASIC_INCUBATOR.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.BASIC_INCUBATOR.get()
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
                            .forGetter(BasicIncubatorRecipe::topSlotIngredient),
                        Ingredient.CODEC_NONEMPTY
                            .fieldOf("bottom_slot")
                            .forGetter(BasicIncubatorRecipe::bottomSlotIngredient),
                        ItemStack.CODEC
                            .fieldOf("output")
                            .forGetter(BasicIncubatorRecipe::outputStack)
                    ).apply(instance, ::BasicIncubatorRecipe)
                }

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BasicIncubatorRecipe> =
                StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC, BasicIncubatorRecipe::topSlotIngredient,
                    Ingredient.CONTENTS_STREAM_CODEC, BasicIncubatorRecipe::bottomSlotIngredient,
                    ItemStack.STREAM_CODEC, BasicIncubatorRecipe::outputStack,
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