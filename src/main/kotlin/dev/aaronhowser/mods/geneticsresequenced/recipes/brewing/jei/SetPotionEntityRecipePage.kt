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

class SetPotionEntityRecipePage(
    private val entityType: EntityType<*>,
    private val isMutation: Boolean = false
) : Recipe<Container> {

    private val potionType = if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH
    private val unsetPotion = PotionUtils.setPotion(ItemStack(Items.POTION), potionType)
    private val cell = ItemStack(ModItems.CELL.get()).setMob(entityType) ?: ItemStack.EMPTY
    private val setPotion = unsetPotion.copy().setMob(entityType) ?: ItemStack.EMPTY

    companion object {
        fun getAllRecipes(): List<SetPotionEntityRecipePage> {
            val allLivingEntities = ForgeRegistries.ENTITY_TYPES.values.filter { it.category != MobCategory.MISC }

            val recipes = mutableListOf<SetPotionEntityRecipePage>()

            for (entityType in allLivingEntities) {
                recipes.add(SetPotionEntityRecipePage(entityType))
                recipes.add(SetPotionEntityRecipePage(entityType, true))
            }

            return recipes
        }

        const val RECIPE_TYPE_NAME = "set_cell_growth"

        val RECIPE_TYPE = object : RecipeType<SetPotionEntityRecipePage> {}

        val SERIALIZER = object : RecipeSerializer<SetPotionEntityRecipePage> {
            override fun fromJson(
                pRecipeId: ResourceLocation,
                pSerializedRecipe: JsonObject
            ): SetPotionEntityRecipePage {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(
                pRecipeId: ResourceLocation,
                pBuffer: FriendlyByteBuf
            ): SetPotionEntityRecipePage? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: SetPotionEntityRecipePage) {
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

    override fun assemble(pContainer: Container): ItemStack = setPotion.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = setPotion.copy()

    override fun getIngredients(): NonNullList<Ingredient> {

        val first = Ingredient.of(unsetPotion)
        val second = Ingredient.of(cell)

        // WHY THE HELL DOES NonNullList.of WORK LIKE THAT??
        return NonNullList.of(first, first, second)
    }

}