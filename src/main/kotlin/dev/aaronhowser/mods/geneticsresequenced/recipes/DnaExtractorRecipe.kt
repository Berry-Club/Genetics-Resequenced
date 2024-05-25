package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.GmoItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.GmoRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class DnaExtractorRecipe(
    private val inputItem: ItemStack,
    private val outputItem: ItemStack,
) : Recipe<Container> {

    private val entityResourceLocation: ResourceLocation
        get() {
            val entityRl = EntityDnaItem.getEntityType(inputItem)
                ?: throw IllegalStateException("Invalid entity type for input item")

            return ForgeRegistries.ENTITY_TYPES.getKey(entityRl)
                ?: throw IllegalStateException("Invalid entity type for input item")
        }

    companion object {
        fun getAllRecipes(): List<DnaExtractorRecipe> {
            return getRegularCellRecipes() + getGmoCellRecipes()
        }

        private fun getGmoCellRecipes(): List<DnaExtractorRecipe> {
            val gmoRecipes = ModPotions.allRecipes.filterIsInstance<GmoRecipe>()

            val recipes = mutableListOf<DnaExtractorRecipe>()
            for (gmoRecipe in gmoRecipes) {

                val inputItem = ModItems.GMO_CELL.get().defaultInstance
                GmoItem.setDetails(
                    inputItem,
                    gmoRecipe.entityType,
                    gmoRecipe.outputGene,
                    gmoRecipe.geneChance
                )

                val outputItem = ModItems.GMO_DNA_HELIX.get().defaultInstance
                GmoItem.setDetails(
                    outputItem,
                    gmoRecipe.entityType,
                    gmoRecipe.outputGene,
                    gmoRecipe.geneChance
                )

                recipes.add(DnaExtractorRecipe(inputItem, outputItem))
            }

            return recipes
        }

        private fun getRegularCellRecipes(): MutableList<DnaExtractorRecipe> {
            val entityRls = ForgeRegistries.ENTITY_TYPES.values
                .filter { it.category != MobCategory.MISC }
                .mapNotNull { ForgeRegistries.ENTITY_TYPES.getKey(it) }

            val recipes = mutableListOf<DnaExtractorRecipe>()

            for (rl in entityRls) {
                val inputItem = ItemStack(ModItems.CELL.get()).setMob(rl) ?: ItemStack.EMPTY
                val outputItem = ItemStack(ModItems.DNA_HELIX.get()).setMob(rl) ?: ItemStack.EMPTY

                if (inputItem.isEmpty || outputItem.isEmpty) continue

                recipes.add(DnaExtractorRecipe(inputItem, outputItem))
            }

            return recipes
        }

        const val RECIPE_TYPE_NAME = "dna_extractor"

        val RECIPE_TYPE = object : RecipeType<DnaExtractorRecipe> {}

        val SERIALIZER = object : RecipeSerializer<DnaExtractorRecipe> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): DnaExtractorRecipe {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): DnaExtractorRecipe? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: DnaExtractorRecipe) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }
    }

    override fun getId(): ResourceLocation =
        OtherUtil.modResource("$RECIPE_TYPE_NAME/${entityResourceLocation.toString().replace(':', '/')}")

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        if (pLevel.isClientSide) return false

        return ForgeRegistries.ENTITY_TYPES.containsKey(entityResourceLocation)
    }

    override fun assemble(pContainer: Container): ItemStack = outputItem.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputItem.copy()

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(inputItem)
        return NonNullList.of(i, i)
    }

}