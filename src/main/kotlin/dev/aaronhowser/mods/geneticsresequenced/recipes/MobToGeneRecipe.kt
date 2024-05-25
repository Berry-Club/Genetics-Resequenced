package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGenesRegistry
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class MobToGeneRecipe(
    private val mobResourceLocation: ResourceLocation,
    val gene: Gene,
    val chance: Int = 100
) : Recipe<Container> {

    val mob: EntityType<*>? = ForgeRegistries.ENTITY_TYPES.getValue(mobResourceLocation)
    private val inputItem = ItemStack(ModItems.DNA_HELIX.get()).setMob(mobResourceLocation) ?: ItemStack.EMPTY
    private val outputItem = ItemStack(ModItems.DNA_HELIX.get()).setGene(gene)

    companion object {

        fun getAllRecipes(): List<MobToGeneRecipe> {

            val allEntityTypes = ForgeRegistries.ENTITY_TYPES.values.filter { it.category != MobCategory.MISC }
            val recipes = mutableListOf<MobToGeneRecipe>()

            for (entityType in allEntityTypes) {

                val possibilities = MobGenesRegistry.getGenesForEntity(entityType)
                val totalWeight = possibilities.values.sumOf { it }

                for ((gene, weight) in possibilities) {
                    val chance = (weight / totalWeight.toDouble() * 100).toInt()
                    recipes.add(MobToGeneRecipe(ForgeRegistries.ENTITY_TYPES.getKey(entityType)!!, gene, chance))
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

    override fun getId(): ResourceLocation {
        val geneString = gene.id.toString().replace(":", "/")

        return OtherUtil.modResource(
            "$RECIPE_TYPE_NAME/${mobResourceLocation.toString().replace(':', '/')}/$geneString"
        )
    }

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(inputItem)
        return NonNullList.of(i, i)
    }
}