package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class MobToGeneRecipe(
    private val mobResourceLocation: ResourceLocation,
    private val gene: Gene
) : Recipe<Container> {

    private val mob: EntityType<*>? = ForgeRegistries.ENTITY_TYPES.getValue(mobResourceLocation)
    private val inputItem = ItemStack(ModItems.DNA_HELIX).setMob(mobResourceLocation) ?: ItemStack.EMPTY
    private val outputItem = ItemStack(ModItems.DNA_HELIX).setGene(gene) ?: ItemStack.EMPTY

    companion object {

        fun getAllRecipes(): List<MobToGeneRecipe> {
            val mobGeneRegistry = MobGenesRegistry.getRegistry()

            val recipes = mutableListOf<MobToGeneRecipe>()
            for ((entityType, genePossibilities) in mobGeneRegistry) {
                val entityId = ForgeRegistries.ENTITY_TYPES.getKey(entityType) ?: continue

                for ((gene, weight) in genePossibilities) {
                    recipes.add(MobToGeneRecipe(entityId, gene!!))
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

        if (mob == null) return false

        return MobGenesRegistry.getGenesForEntity(mob).contains(gene)
    }

    override fun assemble(pContainer: Container): ItemStack = outputItem.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputItem.copy()

    override fun getId(): ResourceLocation =
        ResourceLocation(
            GeneticsResequenced.ID,
            "$RECIPE_TYPE_NAME/${mobResourceLocation.toString().replace(':', '/')}/${gene.id.lowercase()}"
        )

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(inputItem)
        return NonNullList.of(i, i)
    }
}