package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.IncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class SetPotionEntityRecipe : IncubatorRecipe() {

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val topItem = input.getTopItem()
        val bottomItem = input.getBottomItem()

        if (topItem.item != ModItems.CELL.get()) return false
        if (bottomItem.item != Items.POTION) return false

        val inputPotion = OtherUtil.getPotion(bottomItem)
        return inputPotion == ModPotions.CELL_GROWTH || inputPotion == ModPotions.MUTATION
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
            val CODEC: MapCodec<SetPotionEntityRecipe> = MapCodec.unit(SetPotionEntityRecipe())

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SetPotionEntityRecipe> =
                StreamCodec.unit(SetPotionEntityRecipe())
        }

    }

}