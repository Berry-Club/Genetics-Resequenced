package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.high_temp

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes.getHolder
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.IncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
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
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

class GmoRecipe(
    val entityType: EntityType<*>,
    val ingredientItem: Item,
    val idealGeneRk: ResourceKey<Gene>,
    val geneChance: Float,
    val isMutation: Boolean = false
) : IncubatorRecipe() {

    private val inputIngredient = Ingredient.of(ingredientItem)
    private val potionIngredient: Ingredient

    init {
        val potionStack = OtherUtil.getPotionStack(
            if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH
        )

        EntityDnaItem.setEntityType(potionStack, entityType)

        potionIngredient = DataComponentIngredient.of(
            false,
            potionStack
        )
    }

    override val ingredients: List<Ingredient> = listOf(inputIngredient, potionIngredient)

    override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
        val ingredientStack = input.getTopItem()
        val potionStack = input.getBottomItem()

        if (!inputIngredient.test(ingredientStack)) return false
        if (!potionIngredient.test(potionStack)) return false

        return true //TODO: Make sure it actually detects the entity type too
    }

    /**
     * Always returns a Basic Gene! You can only get the ideal gene if you run it through an Advanced Incubator at low temperature!
     * [dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorBlockEntity.craftItem]
     */
    override fun assemble(input: IncubatorRecipeInput, lookup: HolderLookup.Provider): ItemStack {
        return getResultItem(lookup)
    }

    /**
     * Always returns a Basic Gene! You can only get the ideal gene if you run it through an Advanced Incubator at low temperature!
     * [dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorBlockEntity.craftItem]
     */
    override fun getResultItem(lookup: HolderLookup.Provider): ItemStack {
        val output = ModItems.GMO_CELL.itemStack

        GmoCell.setDetails(
            output,
            entityType,
            ModGenes.BASIC.getHolder(lookup)!!
        )

        return output
    }

    fun getSuccess(lookup: HolderLookup.Provider): ItemStack {
        val output = ModItems.GMO_CELL.toStack()

        GmoCell.setDetails(
            output,
            entityType,
            idealGeneRk.getHolder(lookup)!!
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
                        BuiltInRegistries.ITEM.byNameCodec()
                            .fieldOf("ingredient_item")
                            .forGetter(GmoRecipe::ingredientItem),
                        ResourceKey.codec(GeneRegistry.GENE_REGISTRY_KEY)
                            .fieldOf("ideal_gene")
                            .forGetter(GmoRecipe::idealGeneRk),
                        Codec.FLOAT
                            .fieldOf("gene_chance")
                            .forGetter(GmoRecipe::geneChance),
                        Codec.BOOL
                            .optionalFieldOf("is_mutation", false)
                            .forGetter(GmoRecipe::isMutation)
                    ).apply(instance, ::GmoRecipe)
                }

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, GmoRecipe> =
                StreamCodec.composite(
                    ByteBufCodecs.registry(Registries.ENTITY_TYPE), GmoRecipe::entityType,
                    ByteBufCodecs.registry(Registries.ITEM), GmoRecipe::ingredientItem,
                    ResourceKey.streamCodec(GeneRegistry.GENE_REGISTRY_KEY), GmoRecipe::idealGeneRk,
                    ByteBufCodecs.FLOAT, GmoRecipe::geneChance,
                    ByteBufCodecs.BOOL, GmoRecipe::isMutation,
                    ::GmoRecipe
                )
        }

    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun getGmoRecipes(level: Level): List<RecipeHolder<GmoRecipe>> {
            val incubatorRecipes = getIncubatorRecipes(level)

            return incubatorRecipes.mapNotNull { it as? RecipeHolder<GmoRecipe> }
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

        fun getGmoRecipe(level: Level, topStack: ItemStack, bottomStack: ItemStack): GmoRecipe? {
            return getGmoRecipe(level, IncubatorRecipeInput(topStack, bottomStack))
        }

    }

}
