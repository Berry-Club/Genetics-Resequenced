package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.recipe.base.IncubatorRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModRecipeTypes {

	val RECIPE_TYPES_REGISTRY: DeferredRegister<RecipeType<*>> =
		DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, GeneticsResequenced.MOD_ID)

	val INCUBATOR: DeferredHolder<RecipeType<*>, RecipeType<IncubatorRecipe>> =
		registerRecipeType("incubator")

	private fun <T : Recipe<*>> registerRecipeType(
		name: String
	): DeferredHolder<RecipeType<*>, RecipeType<T>> {
		return RECIPE_TYPES_REGISTRY.register(name, Supplier { object : RecipeType<T> {} })
	}


}