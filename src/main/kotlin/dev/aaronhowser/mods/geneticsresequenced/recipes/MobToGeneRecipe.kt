package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class MobToGeneRecipe(
    private val mobResourceLocation: ResourceLocation,
    private val gene: Gene
) : Recipe<Container> {

    private val inputItem = ItemStack(ModItems.DNA_HELIX).setMob(id) ?: ItemStack.EMPTY
    private val outputItem = ItemStack(ModItems.DNA_HELIX).setGene(gene) ?: ItemStack.EMPTY

    companion object {

        fun getAllRecipes(): List<MobToGeneRecipe> {
            val mobGeneRegistry = MobGenesRegistry.getRegistry()

            val recipes = mutableListOf<MobToGeneRecipe>()
            for ((entityType, genes) in mobGeneRegistry) {
                val entityId = ForgeRegistries.ENTITY_TYPES.getKey(entityType) ?: continue

                for (gene in genes) {
                    recipes.add(MobToGeneRecipe(entityId, gene))
                }
            }

            return recipes
        }

        const val RECIPE_TYPE_NAME = "mob_to_gene"

        val RECIPE_TYPE = object : RecipeType<MobToGeneRecipe> {}

        val SERIALIZER = object : RecipeSerializer<MobToGeneRecipe> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): MobToGeneRecipe {
                TODO()
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): MobToGeneRecipe? {
                TODO()
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: MobToGeneRecipe) {
                TODO()
            }
        }
    }

    override fun matches(pContainer: Container, pLevel: Level): Boolean {

        if (pLevel.isClientSide) return false

        TODO("Not yet implemented")
    }

    override fun assemble(pContainer: Container): ItemStack {
        TODO("Not yet implemented")
    }

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun getResultItem(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getId(): ResourceLocation {
        TODO("Not yet implemented")
    }

    override fun getSerializer(): RecipeSerializer<*> {
        TODO("Not yet implemented")
    }

    override fun getType(): RecipeType<*> {
        TODO("Not yet implemented")
    }
}