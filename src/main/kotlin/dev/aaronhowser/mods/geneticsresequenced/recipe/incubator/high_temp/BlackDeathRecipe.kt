package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isHidden
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem.Companion.isSyringe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.IncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class BlackDeathRecipe : IncubatorRecipe() {

    fun getRequiredGenes(lookup: HolderLookup.Provider): List<Holder<Gene>> {
        return GeneRegistry.getRegistrySorted(lookup)
            .filter { it.isNegative && !it.isHidden && !it.isDisabled } - ModGenes.BLACK_DEATH.getHolder(lookup)!!
    }

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val topStack = input.getTopItem()
        val bottomStack = input.getBottomItem()

        if (bottomStack.item != Items.POTION) return false
        if (!topStack.item.isSyringe()) return false

        val inputPotion = OtherUtil.getPotion(bottomStack)
        if (inputPotion != ModPotions.VIRAL_AGENTS) return false

        if (SyringeItem.isContaminated(topStack)) return false

        val syringeGenes = SyringeItem.getGenes(topStack)
        return syringeGenes == getRequiredGenes(level.registryAccess())
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        return getResultItem(lookup)
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        val output = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGeneHolder(output, ModGenes.BLACK_DEATH.getHolder(lookup)!!)

        return output
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.BLACK_DEATH.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.BLACK_DEATH.get()
    }

    class Serializer : RecipeSerializer<BlackDeathRecipe> {
        override fun codec(): MapCodec<BlackDeathRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, BlackDeathRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<BlackDeathRecipe> = MapCodec.unit(BlackDeathRecipe())

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BlackDeathRecipe> =
                StreamCodec.unit(BlackDeathRecipe())
        }

    }

}