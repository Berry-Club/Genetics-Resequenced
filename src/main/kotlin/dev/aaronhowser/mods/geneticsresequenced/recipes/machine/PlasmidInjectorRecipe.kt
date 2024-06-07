package dev.aaronhowser.mods.geneticsresequenced.recipes.machine

import com.google.gson.JsonObject
import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem.Companion.setAmount
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.core.NonNullList
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.Container
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

/**
 * Note that these "recipes" are only used for JEI compatibility. What the machines actually do is hardcoded.
 *
 * See [dev.aaronhowser.mods.geneticsresequenced.blocks.machines.plasmid_injector.PlasmidInjectorBlockEntity.craftItem]
 */
class PlasmidInjectorRecipe(
    private val gene: Gene,
    private val isMetalSyringe: Boolean,
    val isAntiGene: Boolean
) : Recipe<Container> {

    private fun getPlasmidInput(): ItemStack {
        val stack = if (isAntiGene) ModItems.ANTI_PLASMID.itemStack else ModItems.PLASMID.itemStack

        stack.setGene(gene)

        if (!isAntiGene) stack.setAmount(gene.dnaPointsRequired)

        return stack
    }

    private fun getSyringeInput(): ItemStack {
        val localPlayer = ClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")

        val syringeStack = if (isMetalSyringe) ModItems.METAL_SYRINGE.itemStack else ModItems.SYRINGE.itemStack

        if (isMetalSyringe) {
            SyringeItem.setEntity(
                syringeStack,
                Cow(EntityType.COW, localPlayer.level)
            )
        } else {
            SyringeItem.setEntity(
                syringeStack,
                localPlayer
            )
        }

        SyringeItem.setContaminated(syringeStack, false)

        return syringeStack
    }

    private fun getSyringeOutput(): ItemStack {
        val input = getSyringeInput()

        if (isAntiGene) {
            SyringeItem.addAntiGene(input, gene)
        } else {
            SyringeItem.addGene(input, gene)
        }

        return input
    }


    companion object {
        fun getAllRecipes(): List<PlasmidInjectorRecipe> {

            val geneGlassRecipes = mutableListOf<PlasmidInjectorRecipe>()
            val geneMetalRecipes = mutableListOf<PlasmidInjectorRecipe>()
            val antiGeneGlassRecipes = mutableListOf<PlasmidInjectorRecipe>()
            val antiGeneMetalRecipes = mutableListOf<PlasmidInjectorRecipe>()

            for (gene in Gene.getRegistry()) {
                if (!gene.isActive) continue
                if (gene.isHidden) continue

                geneGlassRecipes.add(PlasmidInjectorRecipe(gene, isMetalSyringe = false, isAntiGene = false))
                geneMetalRecipes.add(PlasmidInjectorRecipe(gene, isMetalSyringe = true, isAntiGene = false))
                antiGeneGlassRecipes.add(PlasmidInjectorRecipe(gene, isMetalSyringe = false, isAntiGene = true))
                antiGeneMetalRecipes.add(PlasmidInjectorRecipe(gene, isMetalSyringe = true, isAntiGene = true))
            }

            return geneGlassRecipes + geneMetalRecipes + antiGeneGlassRecipes + antiGeneMetalRecipes
        }

        const val RECIPE_TYPE_NAME = "plasmid_injector"

        val RECIPE_TYPE = object : RecipeType<PlasmidInjectorRecipe> {}

        val SERIALIZER = object : RecipeSerializer<PlasmidInjectorRecipe> {
            override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): PlasmidInjectorRecipe {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): PlasmidInjectorRecipe? {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }

            override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: PlasmidInjectorRecipe) {
                throw IllegalStateException("This recipe type does not support JSON serialization")
            }
        }

    }

    override fun getId(): ResourceLocation {

        val stringBuilder = StringBuilder()
        stringBuilder.append(RECIPE_TYPE_NAME)
        stringBuilder.append('/')
        stringBuilder.append(gene.id.toString().replace(':', '/'))
        if (isAntiGene) stringBuilder.append("/anti_plasmid")
        if (isMetalSyringe) stringBuilder.append("/metal")

        return OtherUtil.modResource(stringBuilder.toString())
    }

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun getType(): RecipeType<*> = RECIPE_TYPE

    override fun matches(pContainer: Container, pLevel: Level): Boolean {
        return !pLevel.isClientSide
    }

    override fun assemble(pContainer: Container): ItemStack = getSyringeOutput()

    override fun canCraftInDimensions(pWidth: Int, pHeight: Int): Boolean = true

    override fun getResultItem(): ItemStack = getSyringeOutput()

    override fun getIngredients(): NonNullList<Ingredient> {
        return NonNullList.of(
            Ingredient.EMPTY,
            Ingredient.of(getPlasmidInput()),
            Ingredient.of(getSyringeInput())
        )
    }

}