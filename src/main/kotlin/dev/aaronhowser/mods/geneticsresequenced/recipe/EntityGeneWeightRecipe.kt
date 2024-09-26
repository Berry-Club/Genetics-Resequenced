package dev.aaronhowser.mods.geneticsresequenced.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.EmptyRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeTypes
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class EntityGeneWeightRecipe(
    val entityType: EntityType<*>,
    val geneHolder: Holder<Gene>,
    val weight: Int
) : Recipe<EmptyRecipeInput> {

    override fun matches(p0: EmptyRecipeInput, p1: Level): Boolean = false
    override fun assemble(p0: EmptyRecipeInput, p1: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
    override fun getResultItem(p0: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
    override fun canCraftInDimensions(p0: Int, p1: Int): Boolean = false

    override fun getSerializer(): RecipeSerializer<*> {
        return ModRecipeSerializers.ENTITY_GENE_WEIGHT.get()
    }

    override fun getType(): RecipeType<*> {
        return ModRecipeTypes.ENTITY_GENE_WEIGHT.get()
    }

    class Serializer : RecipeSerializer<EntityGeneWeightRecipe> {

        override fun codec(): MapCodec<EntityGeneWeightRecipe> {
            return CODEC
        }

        override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, EntityGeneWeightRecipe> {
            return STREAM_CODEC
        }

        companion object {

            val CODEC: MapCodec<EntityGeneWeightRecipe> =
                RecordCodecBuilder.mapCodec { instance ->
                    instance.group(
                        BuiltInRegistries.ENTITY_TYPE.byNameCodec()
                            .fieldOf("entity_type")
                            .forGetter(EntityGeneWeightRecipe::entityType),
                        Gene.CODEC
                            .fieldOf("gene")
                            .forGetter(EntityGeneWeightRecipe::geneHolder),
                        Codec.INT
                            .fieldOf("weight")
                            .forGetter(EntityGeneWeightRecipe::weight)
                    ).apply(instance, ::EntityGeneWeightRecipe)
                }

            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EntityGeneWeightRecipe> =
                StreamCodec.composite(
                    ByteBufCodecs.registry(Registries.ENTITY_TYPE), EntityGeneWeightRecipe::entityType,
                    Gene.STREAM_CODEC, EntityGeneWeightRecipe::geneHolder,
                    ByteBufCodecs.INT, EntityGeneWeightRecipe::weight,
                    ::EntityGeneWeightRecipe
                )

        }

    }


    class ReloadListener : PreparableReloadListener {
        override fun reload(
            p0: PreparableReloadListener.PreparationBarrier,
            p1: ResourceManager,
            p2: ProfilerFiller,
            p3: ProfilerFiller,
            p4: Executor,
            p5: Executor
        ): CompletableFuture<Void> {
            return CompletableFuture.runAsync {
                // I have no idea what im doing lmao
            }.thenCompose(p0::wait).thenAcceptAsync({
                cachedRecipes.clear()
                entityGeneMap.clear()
                println("Cached recipes cleared!")
            }, p5)
        }
    }

    companion object {

        private val cachedRecipes = mutableListOf<EntityGeneWeightRecipe>()

        fun getRecipes(recipeManager: RecipeManager): List<EntityGeneWeightRecipe> {
            if (cachedRecipes.isEmpty()) {
                cachedRecipes.addAll(recipeManager.recipes.mapNotNull {
                    if (it.value is EntityGeneWeightRecipe) it.value as? EntityGeneWeightRecipe else null
                })
            }

            return cachedRecipes
        }

        private val entityGeneMap: MutableMap<EntityType<*>, Map<Holder<Gene>, Int>> = mutableMapOf()

        fun getEntityGeneMap(recipeManager: RecipeManager): MutableMap<EntityType<*>, Map<Holder<Gene>, Int>> {
            if (entityGeneMap.isEmpty()) {
                for (recipe in getRecipes(recipeManager)) {
                    val gene = recipe.geneHolder
                    val currentGeneWeights = entityGeneMap[recipe.entityType] ?: mutableMapOf()

                    val newWeight = currentGeneWeights.getOrDefault(recipe.geneHolder, 0) + recipe.weight

                    entityGeneMap[recipe.entityType] = mapOf(gene to newWeight)
                }
            }

            return entityGeneMap
        }

        fun getEntityGeneWeights(
            entityType: EntityType<*>,
            recipeManager: RecipeManager
        ): Map<Holder<Gene>, Int> {
            return getEntityGeneMap(recipeManager)[entityType] ?: emptyMap()
        }

    }

}