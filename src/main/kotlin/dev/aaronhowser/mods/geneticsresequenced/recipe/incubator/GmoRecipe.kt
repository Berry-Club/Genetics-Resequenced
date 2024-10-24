package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.*
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class GmoRecipe(
    val entityType: EntityType<*>,
    val topSlotIngredient: Ingredient,
    val idealGeneRk: ResourceKey<Gene>,
    val geneChance: Float,
    val needsMutationPotion: Boolean,
) : AbstractIncubatorRecipe() {

    private val bottomSlotIngredient: Ingredient

    init {
        val potionStack = OtherUtil.getPotionStack(
            if (needsMutationPotion) ModPotions.MUTATION else ModPotions.CELL_GROWTH
        )

        EntityDnaItem.setEntityType(potionStack, entityType)

        bottomSlotIngredient = DataComponentIngredient.of(
            false,
            potionStack
        )
    }

    override val ingredients: List<Ingredient> = listOf(topSlotIngredient, bottomSlotIngredient)

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val topSlotStack = input.getTopItem()
        val bottomSlotStack = input.getBottomItem()

        if (input.isHighTemp) return false
        if (!topSlotIngredient.test(topSlotStack)) return false
        if (!bottomSlotIngredient.test(bottomSlotStack)) return false

        return true //TODO: Make sure it actually detects the entity type too
    }

    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        return getResultItem(lookup)
    }

    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        val output = ModItems.GMO_CELL.itemStack

        GmoCell.setDetails(
            output,
            entityType,
            idealGeneRk.getHolder(lookup)!!
        )

        return output
    }

    fun getFailure(lookup: HolderLookup.Provider): ItemStack {
        val output = ModItems.GMO_CELL.toStack()

        GmoCell.setDetails(
            output,
            entityType,
            ModGenes.BASIC.getHolder(lookup)!!
        )

        return output
    }

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.GMO.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.GMO.get()
    }

    class Serializer : RecipeSerializer<GmoRecipe> {
        override fun codec(): MapCodec<GmoRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, GmoRecipe> {
            return STREAM_CODEC
        }

        companion object {
            val CODEC: MapCodec<GmoRecipe> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        BuiltInRegistries.ENTITY_TYPE.byNameCodec()
                            .fieldOf("entity_type")
                            .forGetter(GmoRecipe::entityType),
                        Ingredient.CODEC_NONEMPTY
                            .fieldOf("ingredient")
                            .forGetter(GmoRecipe::topSlotIngredient),
                        ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
                            .fieldOf("ideal_gene")
                            .forGetter(GmoRecipe::idealGeneRk),
                        Codec.FLOAT
                            .optionalFieldOf("gene_chance", 1f)
                            .forGetter(GmoRecipe::geneChance),
                        Codec.BOOL
                            .optionalFieldOf("needs_mutation_potion", false)
                            .forGetter(GmoRecipe::needsMutationPotion)
                    ).apply(instance, ::GmoRecipe)
                }

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GmoRecipe> =
                StreamCodec.composite(
                    ByteBufCodecs.registry(Registries.ENTITY_TYPE), GmoRecipe::entityType,
                    Ingredient.CONTENTS_STREAM_CODEC, GmoRecipe::topSlotIngredient,
                    ResourceKey.streamCodec(ModGenes.GENE_REGISTRY_KEY), GmoRecipe::idealGeneRk,
                    ByteBufCodecs.FLOAT, GmoRecipe::geneChance,
                    ByteBufCodecs.BOOL, GmoRecipe::needsMutationPotion,
                    ::GmoRecipe
                )
        }

    }

    companion object {

        fun getGmoRecipes(level: Level): List<RecipeHolder<GmoRecipe>> {
            val recipeManager = level.recipeManager
            return getGmoRecipes(recipeManager)
        }

        @Suppress("UNCHECKED_CAST")
        fun getGmoRecipes(recipeManager: RecipeManager): List<RecipeHolder<GmoRecipe>> {
            val incubatorRecipes = getIncubatorRecipes(recipeManager)

            return incubatorRecipes.mapNotNull { if (it.value is GmoRecipe) it as? RecipeHolder<GmoRecipe> else null }
        }

        fun isValidIngredient(level: Level, itemStack: ItemStack): Boolean {
            return getGmoRecipes(level).any { recipeHolder ->
                recipeHolder.value.ingredients.any { ingredient -> ingredient.test(itemStack) }
            }
        }

        fun getGmoRecipe(level: Level, incubatorRecipeInput: IncubatorRecipeInput): GmoRecipe? {
            return getGmoRecipes(level).find { recipeHolder ->
                recipeHolder.value.matches(incubatorRecipeInput, level)
            }?.value
        }

        fun getGmoRecipe(level: Level, topStack: ItemStack, bottomStack: ItemStack, isHighTemp: Boolean): GmoRecipe? {
            return getGmoRecipe(level, IncubatorRecipeInput(topStack, bottomStack, isHighTemp))
        }

    }

}
