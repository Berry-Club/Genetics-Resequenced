package dev.aaronhowser.mods.geneticsresequenced.recipes.machine

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
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

/**
 * Note that these "recipes" are only used for JEI compatibility. What the machines actually do is hardcoded.
 *
 * See [dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_extractor.DnaExtractorBlockEntity.craftItem]
 */
class DnaExtractorRecipe(
    private val inputItem: ItemStack,
    private val outputItem: ItemStack
) : Recipe<Container> {

    private val entityResourceLocation: ResourceLocation
        get() {
            val entityType = EntityDnaItem.getEntityType(inputItem)
                ?: throw IllegalStateException("Invalid entity type for input item")

            return OtherUtil.getEntityResourceLocation(entityType)
        }

    companion object {
        fun getAllRecipes(): List<DnaExtractorRecipe> {
            return getRegularCellRecipes() + getGmoRecipes()
        }

        private fun getRegularCellRecipes(): List<DnaExtractorRecipe> {
            val entityRls = ForgeRegistries.ENTITY_TYPES.values
                .filter { it.category != MobCategory.MISC }
                .mapNotNull { OtherUtil.getEntityResourceLocation(it) }

            val recipes = mutableListOf<DnaExtractorRecipe>()

            for (rl in entityRls) {
                val inputItem = ItemStack(ModItems.CELL.get()).setMob(rl) ?: ItemStack.EMPTY
                val outputItem = ItemStack(ModItems.DNA_HELIX.get()).setMob(rl) ?: ItemStack.EMPTY

                if (inputItem.isEmpty || outputItem.isEmpty) continue

                recipes.add(DnaExtractorRecipe(inputItem, outputItem))
            }

            return recipes
        }

        private fun getGmoRecipes(): List<DnaExtractorRecipe> {
            val cells = GmoCell.getAllGmoCells()

            val recipes = mutableListOf<DnaExtractorRecipe>()
            for (cell in cells) {
                val entityType = EntityDnaItem.getEntityType(cell) ?: continue
                val gene = cell.getGene() ?: continue
                val outputItem = ModItems.DNA_HELIX.itemStack.setMob(entityType)?.setGene(gene) ?: continue

                recipes.add(DnaExtractorRecipe(cell, outputItem))
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

    override fun getId(): ResourceLocation {
        var entityRlString = entityResourceLocation.toString().replace(':', '/')

        if (inputItem.item == ModItems.GMO_CELL.get()) {
            entityRlString += "/gmo"
        }

        return OtherUtil.modResource("$RECIPE_TYPE_NAME/$entityRlString")
    }

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