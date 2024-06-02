package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.jei

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.recipes.brewing.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class BlackDeathRecipePage(private val metalSyringe: Boolean) : Recipe<Container> {

    private val badSyringe: ItemStack
        get() {
            val item = if (metalSyringe) ModItems.METAL_SYRINGE.get() else ModItems.SYRINGE.get()
            val stack = item.defaultInstance

            SyringeItem.setEntity(
                stack,
                ClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")
            )

            for (badGene in BlackDeathRecipe.allBadGenes) {
                SyringeItem.addGene(stack, badGene)
            }

            SyringeItem.setContaminated(stack, false)

            return stack
        }

    private val viralAgentStack = PotionUtils.setPotion(ItemStack(Items.POTION), ModPotions.VIRAL_AGENTS)

    private val blackDeathDna = ItemStack(ModItems.DNA_HELIX.get()).setGene(DefaultGenes.BLACK_DEATH)

    companion object {
        fun getAllRecipes(): List<BlackDeathRecipePage> {
            return listOf(BlackDeathRecipePage(false), BlackDeathRecipePage(true))
        }

        const val RECIPE_TYPE_NAME = "black_death_recipe"

        val RECIPE_TYPE = object : RecipeType<BlackDeathRecipePage> {}

        val SERIALIZER = object : RecipeSerializer<BlackDeathRecipePage> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): BlackDeathRecipePage {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): BlackDeathRecipePage? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: BlackDeathRecipePage) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }

    }

    override fun getId(): ResourceLocation {
        val type = if (metalSyringe) "metal" else "glass"

        return OtherUtil.modResource("$RECIPE_TYPE_NAME/black_death/$type")
    }

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        return !pLevel.isClientSide
    }

    override fun assemble(pContainer: Container): ItemStack = blackDeathDna.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = blackDeathDna.copy()

    override fun getIngredients(): NonNullList<Ingredient> {

        val first = Ingredient.of(badSyringe)
        val second = Ingredient.of(viralAgentStack)

        // WHY THE HELL DOES NonNullList.of WORK LIKE THAT??
        return NonNullList.of(first, first, second)
    }

}