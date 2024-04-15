package dev.aaronhowser.mods.geneticsresequenced.recipes

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
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
import mezz.jei.api.recipe.RecipeType as JEIRecipeType

class DnaExtractorRecipe(
    private val entityResourceLocation: ResourceLocation
) : Recipe<Container> {

    private val inputItem = ItemStack(ModItems.CELL).setMob(entityResourceLocation) ?: ItemStack.EMPTY
    private val outputItem = ItemStack(ModItems.DNA_HELIX).setMob(entityResourceLocation) ?: ItemStack.EMPTY

    companion object {
        fun getAllRecipes(): List<DnaExtractorRecipe> {
            return ForgeRegistries.ENTITY_TYPES.values
                .filter { it.category != MobCategory.MISC }
                .mapNotNull { ForgeRegistries.ENTITY_TYPES.getKey(it) }
                .map { DnaExtractorRecipe(it) }
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

        //FIXME: Does this crash if JEI isn't loaded?
        val JEI_RECIPE_TYPE: JEIRecipeType<DnaExtractorRecipe> =
            JEIRecipeType(
                ResourceLocation(GeneticsResequenced.ID, RECIPE_TYPE_NAME),
                DnaExtractorRecipe::class.java
            )
    }

    override fun getId(): ResourceLocation =
        ResourceLocation(
            GeneticsResequenced.ID,
            "$RECIPE_TYPE_NAME/${entityResourceLocation.toString().replace(':', '/')}"
        )

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