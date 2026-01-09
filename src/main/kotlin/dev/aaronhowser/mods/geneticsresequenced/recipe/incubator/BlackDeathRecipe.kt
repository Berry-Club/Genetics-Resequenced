package dev.aaronhowser.mods.geneticsresequenced.recipe.incubator

import com.google.gson.JsonObject
import dev.aaronhowser.mods.aaron.AaronExtensions.partialNbtIngredient
import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.geneticsresequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.IncubatorRecipeInput
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import kotlin.jvm.optionals.getOrNull

object BlackDeathRecipe : AbstractIncubatorRecipe(
	topIngredient = Ingredient.of(ModItemTagsProvider.SYRINGES),
	bottomIngredient = OtherUtil.getPotionStack(ModPotions.VIRAL_AGENTS.get()).partialNbtIngredient()
) {

	override fun matches(input: IncubatorRecipeInput, level: Level): Boolean {
		val syringeStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(syringeStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		if (!SyringeItem.hasBlood(syringeStack) || SyringeItem.isContaminated(syringeStack)) return false

		val syringeGenes = SyringeItem.getGeneRks(syringeStack)
		val requiredGenes = getRequiredGenes(level.registryAccess())

		return syringeGenes.containsAll(requiredGenes)
	}

	override fun assemble(input: IncubatorRecipeInput, pRegistryAccess: RegistryAccess): ItemStack {
		return getResultItem(pRegistryAccess)
	}

	override fun getResultItem(pRegistryAccess: RegistryAccess): ItemStack {
		return DnaHelixItem.getHelixStack(ModGenes.BLACK_DEATH.getHolderOrThrow(pRegistryAccess))
	}

	override fun getId(): ResourceLocation = GeneticsResequenced.modResource("black_death")

	override fun getSerializer(): RecipeSerializer<*> {
		return ModRecipeSerializers.BLACK_DEATH.get()
	}

	fun getRequiredGenes(lookup: HolderLookup.Provider): List<ResourceKey<Gene>> {
		return ModGenes.getRegistrySorted(lookup)
			.filter { it.isNegative && !it.isHelixOnly && !it.isDisabled }
			.mapNotNull { it.unwrapKey().getOrNull() }
			.minus(ModGenes.BLACK_DEATH)
	}

	class Serializer : RecipeSerializer<BlackDeathRecipe> {
		override fun fromJson(id: ResourceLocation, json: JsonObject): BlackDeathRecipe {
			return BlackDeathRecipe
		}

		override fun fromNetwork(id: ResourceLocation, buf: FriendlyByteBuf): BlackDeathRecipe {
			return BlackDeathRecipe
		}

		override fun toNetwork(buf: FriendlyByteBuf, recipe: BlackDeathRecipe) {
			// No data to write
		}
	}

}