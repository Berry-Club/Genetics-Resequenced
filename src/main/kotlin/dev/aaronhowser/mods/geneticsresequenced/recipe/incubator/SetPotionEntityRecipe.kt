package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModPotionTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.PotionTagIngredient
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class SetPotionEntityRecipe private constructor() : AbstractIncubatorRecipe() {

    private val cellIngredient = Ingredient.of(ModItems.CELL.get())
    private val potionIngredient = PotionTagIngredient(ModPotionTagsProvider.CAN_HAVE_ENTITY).toVanilla()

    override val ingredients: List<Ingredient> = listOf(cellIngredient, potionIngredient)

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val cellStack = input.getTopItem()
        val potionStack = input.getBottomItem()

        if (!cellIngredient.test(cellStack)) return false
        if (!potionIngredient.test(potionStack)) return false

        return EntityDnaItem.hasEntity(cellStack)
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        val topItem = input.getTopItem()
        val bottomItem = input.getBottomItem()

        val ingredientEntity = EntityDnaItem.getEntityType(topItem) ?: return ItemStack.EMPTY

        val output = bottomItem.copy()
        EntityDnaItem.setEntityType(output, ingredientEntity)
        return output
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.SET_POTION_ENTITY.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.SET_POTION_ENTITY.get()
    }

    class Serializer : RecipeSerializer<SetPotionEntityRecipe> {

        override fun codec(): MapCodec<SetPotionEntityRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SetPotionEntityRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<SetPotionEntityRecipe> = MapCodec.unit(INSTANCE)

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetPotionEntityRecipe> =
                StreamCodec.unit(INSTANCE)
        }

    }

    companion object {
        val INSTANCE = SetPotionEntityRecipe()
    }

}