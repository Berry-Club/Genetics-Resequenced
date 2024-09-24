package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
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
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class VirusRecipe(
    val inputDnaGene: ResourceKey<Gene>,
    val outputGene: ResourceKey<Gene>
) : IncubatorRecipe() {

    private val helixIngredient: Ingredient = Ingredient.of(ModItems.DNA_HELIX.get())
    private val potionIngredient: Ingredient =
        DataComponentIngredient.of(false, OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS))

    override val ingredients: List<Ingredient> = listOf(helixIngredient, potionIngredient)

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val helixStack = input.getTopItem()
        val potionStack = input.getBottomItem()

        if (!helixIngredient.test(helixStack)) return false
        if (!potionIngredient.test(potionStack)) return false

        return DnaHelixItem.getGeneHolder(helixStack) == inputDnaGene
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

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun getVirusRecipes(recipeManager: RecipeManager): List<RecipeHolder<VirusRecipe>> {
            val incubatorRecipes = getIncubatorRecipes(recipeManager)

            return incubatorRecipes.mapNotNull { if (it.value is VirusRecipe) it as? RecipeHolder<VirusRecipe> else null }
        }
    }

}