package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModPotionTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.PotionTagIngredient
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

class SetPotionEntityRecipe private constructor() : AbstractIncubatorRecipe(
    topIngredient = Ingredient.of(ModItems.CELL.get()),
    bottomIngredient = PotionTagIngredient(ModPotionTagsProvider.CAN_HAVE_ENTITY).toVanilla()
) {

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val cellStack = input.getTopItem()
        val potionStack = input.getBottomItem()

        if (!topIngredient.test(cellStack)) return false
        if (!bottomIngredient.test(potionStack)) return false

        val topEntity = EntityDnaItem.getEntityType(cellStack) ?: return false
        val bottomEntity = EntityDnaItem.getEntityType(potionStack) ?: return true

        return topEntity != bottomEntity
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        val topItem = input.getTopItem()
        val bottomItem = input.getBottomItem()

        val topEntity = EntityDnaItem.getEntityType(topItem) ?: return ItemStack.EMPTY

        val output = bottomItem.copy()
        EntityDnaItem.setEntityType(output, topEntity)
        return output
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        return ItemStack.EMPTY
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.SET_POTION_ENTITY.get()
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