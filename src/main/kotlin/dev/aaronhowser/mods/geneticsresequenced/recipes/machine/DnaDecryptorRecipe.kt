package dev.aaronhowser.mods.geneticsresequenced.recipes.machine

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.MobGeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem.Companion.setMob
import dev.aaronhowser.mods.geneticsresequenced.registries.ModItems
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


/**
 * Note that these "recipes" are only used for JEI compatibility. What the machines actually do is hardcoded.
 *
 * See [dev.aaronhowser.mods.geneticsresequenced.blocks.machines.dna_decryptor.DnaDecryptorBlockEntity.craftItem]
 */
class DnaDecryptorRecipe : Recipe<Container> {

    constructor(
        mobResourceLocation: ResourceLocation,
        gene: Gene,
        chance: Int = 100
    ) {
        this.mobResourceLocation = mobResourceLocation
        this.gene = gene
        this.chance = chance

        this.inputItem = ItemStack(ModItems.DNA_HELIX.get()).setMob(mobResourceLocation) ?: ItemStack.EMPTY
        this.outputItem = ItemStack(ModItems.DNA_HELIX.get()).setGene(gene)
    }

    constructor(
        inputItem: ItemStack,
        outputItem: ItemStack,
        chance: Int
    ) {
        this.inputItem = inputItem
        this.outputItem = outputItem

        val entityType = EntityDnaItem.getEntityType(inputItem)
            ?: throw IllegalStateException("Invalid entity type for input item")

        this.mobResourceLocation = ForgeRegistries.ENTITY_TYPES.getKey(entityType)
            ?: throw IllegalStateException("Invalid entity type for input item")

        this.gene = outputItem.getGene()
            ?: throw IllegalStateException("Invalid gene for output item")

        this.chance = chance
    }

    private val mobResourceLocation: ResourceLocation
    val entityType: EntityType<*>
        get() = OtherUtil.getEntityType(mobResourceLocation)

    val gene: Gene
    val chance: Int

    private val inputItem: ItemStack
    private val outputItem: ItemStack

    companion object {

        fun getAllRecipes(): List<DnaDecryptorRecipe> {
            val allEntityTypes = ForgeRegistries.ENTITY_TYPES.values.filter { it.category != MobCategory.MISC }
            val recipes = mutableListOf<DnaDecryptorRecipe>()

            for (entityType in allEntityTypes) {

                val entityRl = ForgeRegistries.ENTITY_TYPES.getKey(entityType)
                if (entityRl == null) {
                    GeneticsResequenced.LOGGER.error("Failed to get resource location for entity type: $entityType")
                    continue
                }

                val possibilities = MobGeneRegistry.getGeneWeights(entityType)
                val totalWeight = possibilities.values.sumOf { it }

                for ((gene, weight) in possibilities) {
                    val chance = (weight / totalWeight.toDouble() * 100).toInt()
                    recipes.add(DnaDecryptorRecipe(entityRl, gene, chance))
                }

            }

            return recipes
        }

        const val RECIPE_TYPE_NAME = "dna_decryptor"

        val RECIPE_TYPE = object : RecipeType<DnaDecryptorRecipe> {}

        val SERIALIZER = object : RecipeSerializer<DnaDecryptorRecipe> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): DnaDecryptorRecipe {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): DnaDecryptorRecipe? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: DnaDecryptorRecipe) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }
    }

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        if (pLevel.isClientSide) return false

        return MobGeneRegistry.getGeneWeights(entityType).contains(gene)
    }

    override fun assemble(pContainer: Container): ItemStack = outputItem.copy()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = outputItem.copy()

    override fun getId(): ResourceLocation {
        val geneString = gene.id.toString().replace(":", "/")
        val mobRlString = mobResourceLocation.toString().replace(':', '/')

        var outString = "$mobRlString/$geneString"

        if (inputItem.item == ModItems.GMO_CELL.get()) {
            outString += "/gmo"
        }

        return OtherUtil.modResource(
            "$RECIPE_TYPE_NAME/$outString"
        )
    }

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun getIngredients(): NonNullList<Ingredient> {
        val i = Ingredient.of(inputItem)
        return NonNullList.of(i, i)
    }
}