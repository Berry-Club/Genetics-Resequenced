package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class SubstrateCellRecipePage(
    private val entityType: EntityType<*>,
) : Recipe<Container> {

    private val cellGrowth = PotionUtils.setPotion(ItemStack(Items.POTION), ModPotions.SUBSTRATE).apply {
        count = 3
    }
    private val cell = ItemStack(ModItems.CELL.get()).setMob(entityType) ?: ItemStack.EMPTY
    private val outputCell = cell.copy().apply {
        count = 3
    }

    companion object {
        fun getAllRecipes(): List<SubstrateCellRecipePage> {
            val allLivingEntities = ForgeRegistries.ENTITY_TYPES.values.filter { it.category != MobCategory.MISC }

            val recipes = mutableListOf<SubstrateCellRecipePage>()

            for (entityType in allLivingEntities) {
                recipes.add(SubstrateCellRecipePage(entityType))
            }

            return recipes
        }

        const val RECIPE_TYPE_NAME = "substrate_dupe"

        val RECIPE_TYPE = object : RecipeType<SubstrateCellRecipePage> {}

        val SERIALIZER = object : RecipeSerializer<SubstrateCellRecipePage> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): SubstrateCellRecipePage {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): SubstrateCellRecipePage? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: SubstrateCellRecipePage) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }

    }

    override fun getId(): ResourceLocation {
        val entityResourceLocation = EntityType.getKey(entityType)
        val entityString = entityResourceLocation.toString().replace(":", "/")

        return OtherUtil.modResource("$RECIPE_TYPE_NAME/$entityString")
    }

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        return !pLevel.isClientSide
    }

    override fun assemble(pContainer: Container): ItemStack = outputCell.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputCell.copy()

    override fun getIngredients(): NonNullList<Ingredient> {

        val first = Ingredient.of(cellGrowth)
        val second = Ingredient.of(cell)

        // WHY THE HELL DOES NonNullList.of WORK LIKE THAT??
        return NonNullList.of(first, first, second)
    }

}