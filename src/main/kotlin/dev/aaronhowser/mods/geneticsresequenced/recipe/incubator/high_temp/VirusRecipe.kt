package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class VirusRecipe(
    val inputDnaGene: ResourceKey<Gene>,
    val outputGene: ResourceKey<Gene>
) : Recipe<IncubatorRecipeInput> {

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val topStack = input.getTopItem()
        val bottomStack = input.getBottomItem()

        if (bottomStack.item != Items.POTION) return false
        if (topStack.item != ModItems.DNA_HELIX.get()) return false

        val inputPotion = OtherUtil.getPotion(bottomStack)
        if (inputPotion != ModPotions.VIRAL_AGENTS) return false

        return DnaHelixItem.getGeneHolder(topStack) == inputDnaGene
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        return getResultItem(lookup)
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        val output = DnaHelixItem.setGeneHolder(
            ModItems.DNA_HELIX.toStack(),
            outputGene.getHolder(lookup)!!
        )

        return output
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.VIRUS.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.VIRUS.get()
    }

    override fun canCraftInDimensions(p0: Int, p1: Int): Boolean = true

    class Serializer : RecipeSerializer<VirusRecipe> {

        override fun codec(): MapCodec<VirusRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, VirusRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<VirusRecipe> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY)
                            .fieldOf("input_gene")
                            .forGetter(VirusRecipe::inputDnaGene),
                        ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY)
                            .fieldOf("output_gene")
                            .forGetter(VirusRecipe::outputGene)
                    ).apply(instance, ::VirusRecipe)
                }

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, VirusRecipe> =
                StreamCodec.composite(
                    ResourceKey.streamCodec(GeneRegistry.GENE_REGISTRY_KEY), VirusRecipe::inputDnaGene,
                    ResourceKey.streamCodec(GeneRegistry.GENE_REGISTRY_KEY), VirusRecipe::outputGene,
                    ::VirusRecipe
                )

        }

    }

}