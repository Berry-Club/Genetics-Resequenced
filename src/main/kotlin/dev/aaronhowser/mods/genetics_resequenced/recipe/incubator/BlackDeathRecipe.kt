package dev.aaronhowser.mods.genetics_resequenced.recipe.incubator

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.genetics_resequenced.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isDisabled
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isHelixOnly
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene.Companion.isNegative
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.SyringeItem
import dev.aaronhowser.mods.genetics_resequenced.recipe.base.IncubatorRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.genetics_resequenced.registry.ModPotions
import dev.aaronhowser.mods.genetics_resequenced.registry.ModRecipeSerializers
import dev.aaronhowser.mods.genetics_resequenced.util.OtherUtil
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level

object BlackDeathRecipe : IncubatorRecipe(
	topIngredient = Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(ModItemTagsProvider.SYRINGES)),
	bottomIngredient = OtherUtil.potionIngredient(ModPotions.VIRAL_AGENTS)
) {

	override fun matches(input: Input, level: Level): Boolean {
		val syringeStack = input.getTopItem()
		val potionStack = input.getBottomItem()

		if (!this.topIngredient.test(syringeStack)) return false
		if (!this.bottomIngredient.test(potionStack)) return false

		if (!SyringeItem.hasBlood(syringeStack) || SyringeItem.isContaminated(syringeStack)) return false

		val syringeGenes = SyringeItem.getGenes(syringeStack)
		val requiredGenes = getRequiredGenes(level.registryAccess())

		return syringeGenes.containsAll(requiredGenes)
	}

	override fun assemble(input: Input): ItemStack {
		val lookup = SyringeItem.getGenes(input.getTopItem())
			.firstNotNullOfOrNull { geneHolder -> geneHolder.unwrapLookup() }
			?: return ItemStack.EMPTY

		return DnaHelixItem.getHelixStack(lookup.getOrThrow(ModGenes.BLACK_DEATH))
	}

	fun createOutput(lookup: HolderLookup.Provider): ItemStack {
		return DnaHelixItem.getHelixStack(ModGenes.BLACK_DEATH.getHolderOrThrow(lookup))
	}

	override fun getSerializer(): RecipeSerializer<BlackDeathRecipe> {
		return ModRecipeSerializers.BLACK_DEATH.get()
	}

	fun getRequiredGenes(lookup: HolderLookup.Provider): List<Holder<Gene>> {
		return ModGenes.getRegistrySorted(lookup)
			.filter { it.isNegative && !it.isHelixOnly && !it.isDisabled }
			.minus(ModGenes.BLACK_DEATH.getHolderOrThrow(lookup))
	}

	val CODEC: MapCodec<BlackDeathRecipe> = MapCodec.unit(BlackDeathRecipe)

	val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, BlackDeathRecipe> =
		StreamCodec.unit(BlackDeathRecipe)

}
