package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import com.mojang.serialization.JsonOps
import dev.aaronhowser.mods.aaron.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.AaronExtensions.partialNbtIngredient
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.GsonHelper
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeManager
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

class GmoRecipe(
	private val id: ResourceLocation,
	val entityType: EntityType<*>,
	topIngredient: Ingredient,
	val idealGeneRk: ResourceKey<Gene>,
	val geneChance: Float,
	val needsMutationPotion: Boolean,
) : AbstractIncubatorRecipe(
	topIngredient = topIngredient,
	bottomIngredient = getBottomIngredient(needsMutationPotion, entityType)
) {

	override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
		val topSlotStack = input.getTopItem()
		val bottomSlotStack = input.getBottomItem()

		if (input.isHighTemp) return false
		if (!this.topIngredient.test(topSlotStack)) return false
		if (!this.bottomIngredient.test(bottomSlotStack)) return false

		return true //TODO: Make sure it actually detects the entity type too
	}

	override fun assemble(input: IncubatorRecipeInput, registries: RegistryAccess): ItemStack {
		return getResultItem(registries)
	}

	override fun getResultItem(registries: RegistryAccess): ItemStack {
		val output = ModItems.GMO_CELL.getDefaultInstance()

		GmoCell.setDetails(
			output,
			this.entityType,
			this.idealGeneRk.getHolderOrThrow(registries)
		)

		return output
	}

	override fun getId(): ResourceLocation = this.id

	fun getFailure(lookup: HolderLookup.Provider): ItemStack {
		val output = ModItems.GMO_CELL.getDefaultInstance()

		GmoCell.setDetails(
			output,
			this.entityType,
			ModGenes.BASIC.getHolderOrThrow(lookup)
		)

		return output
	}

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.GMO.get()
	}

	class Serializer : RecipeSerializer<GmoRecipe> {

		override fun fromJson(pRecipeId: ResourceLocation, pSerializedRecipe: JsonObject): GmoRecipe {
			val entityTypeString = GsonHelper.getAsString(pSerializedRecipe, "entity_type")
			val entityType = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation(entityTypeString))

			val ingredient = Ingredient.fromJson(pSerializedRecipe.getAsJsonObject("ingredient"))
			val idealGeneRk = ResourceKey.codec(ModGenes.GENE_REGISTRY_KEY)
				.parse(JsonOps.INSTANCE, pSerializedRecipe.get("ideal_gene"))
				.getOrThrow(false, ::IllegalArgumentException)

			val geneChance = GsonHelper.getAsFloat(pSerializedRecipe, "gene_chance", 1f)
			val needsMutationPotion = GsonHelper.getAsBoolean(pSerializedRecipe, "needs_mutation_potion", false)

			return GmoRecipe(
				id = pRecipeId,
				entityType = entityType,
				topIngredient = ingredient,
				idealGeneRk = idealGeneRk,
				geneChance = geneChance,
				needsMutationPotion = needsMutationPotion
			)
		}

		override fun fromNetwork(pRecipeId: ResourceLocation, pBuffer: FriendlyByteBuf): GmoRecipe? {
			val entityTypeRk = pBuffer.readResourceKey(Registries.ENTITY_TYPE)
			val entityType = ForgeRegistries.ENTITY_TYPES.getHolder(entityTypeRk).get().get()

			val ingredient = Ingredient.fromNetwork(pBuffer)
			val idealGeneRk = pBuffer.readResourceKey(ModGenes.GENE_REGISTRY_KEY)
			val geneChance = pBuffer.readFloat()
			val needsMutationPotion = pBuffer.readBoolean()

			return GmoRecipe(
				id = pRecipeId,
				entityType = entityType,
				topIngredient = ingredient,
				idealGeneRk = idealGeneRk,
				geneChance = geneChance,
				needsMutationPotion = needsMutationPotion
			)
		}

		override fun toNetwork(pBuffer: FriendlyByteBuf, pRecipe: GmoRecipe) {
			val entityTypeRk = ForgeRegistries.ENTITY_TYPES.getResourceKey(pRecipe.entityType).get()
			pBuffer.writeResourceKey(entityTypeRk)
			pRecipe.topIngredient.toNetwork(pBuffer)
			pBuffer.writeResourceKey(pRecipe.idealGeneRk)
			pBuffer.writeFloat(pRecipe.geneChance)
			pBuffer.writeBoolean(pRecipe.needsMutationPotion)
		}

	}

	companion object {

		private fun getBottomIngredient(needsMutationPotion: Boolean, entityType: EntityType<*>): Ingredient {
			val potionStack = OtherUtil.getPotionStack(
				if (needsMutationPotion) ModPotions.MUTATION.get() else ModPotions.CELL_GROWTH.get()
			)

			EntityDnaItem.setEntityType(potionStack, entityType)
			return potionStack.partialNbtIngredient()
		}

		fun getGmoRecipes(level: Level): List<GmoRecipe> {
			val recipeManager = level.recipeManager
			return getGmoRecipes(recipeManager)
		}

		@Suppress("UNCHECKED_CAST")
		fun getGmoRecipes(recipeManager: RecipeManager): List<GmoRecipe> {
			return getIncubatorRecipes(recipeManager).filterIsInstance<GmoRecipe>()
		}

		fun isValidIngredient(level: Level, itemStack: ItemStack): Boolean {
			return getGmoRecipes(level).any { recipeHolder ->
				recipeHolder.ingredients.any { ingredient -> ingredient.test(itemStack) }
			}
		}

		fun getGmoRecipe(level: Level, incubatorRecipeInput: IncubatorRecipeInput): GmoRecipe? {
			return getGmoRecipes(level).find { recipeHolder ->
				recipeHolder.matches(incubatorRecipeInput, level)
			}
		}

		fun getGmoRecipe(level: Level, topStack: ItemStack, bottomStack: ItemStack, isHighTemp: Boolean): GmoRecipe? {
			return getGmoRecipe(level, IncubatorRecipeInput(topStack, bottomStack, isHighTemp))
		}

	}

}
