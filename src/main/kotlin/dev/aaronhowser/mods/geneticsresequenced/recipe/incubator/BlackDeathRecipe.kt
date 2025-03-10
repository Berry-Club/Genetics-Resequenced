package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class BlackDeathRecipe private constructor() : AbstractIncubatorRecipe(
    topIngredient = DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS)),
    bottomIngredient = Ingredient.of(ModItemTagsProvider.SYRINGES)
) {

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val syringeStack = input.getTopItem()
        val potionStack = input.getBottomItem()

        if (!topIngredient.test(potionStack)) return false
        if (!bottomIngredient.test(syringeStack)) return false

        if (!SyringeItem.hasBlood(syringeStack) || SyringeItem.isContaminated(syringeStack)) return false

        val syringeGenes = SyringeItem.getGenes(syringeStack)
        val requiredGenes = getRequiredGenes(level.registryAccess())

        return syringeGenes.containsAll(requiredGenes)
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        return getResultItem(lookup)
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        return DnaHelixItem.getHelixStack(ModGenes.BLACK_DEATH.getHolderOrThrow(lookup))
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.BLACK_DEATH.get()
    }

    class Serializer : RecipeSerializer<BlackDeathRecipe> {
        override fun codec(): MapCodec<BlackDeathRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, BlackDeathRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<BlackDeathRecipe> = MapCodec.unit(INSTANCE)

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BlackDeathRecipe> =
                StreamCodec.unit(INSTANCE)
        }

    }

    companion object {
        val INSTANCE = BlackDeathRecipe()

        fun getRequiredGenes(lookup: HolderLookup.Provider): List<Holder<Gene>> {
            return ModGenes.getRegistrySorted(lookup)
                .filter { it.isNegative && !it.isHelixOnly && !it.isDisabled }
                .minus(ModGenes.BLACK_DEATH.getHolderOrThrow(lookup))
        }
    }

}