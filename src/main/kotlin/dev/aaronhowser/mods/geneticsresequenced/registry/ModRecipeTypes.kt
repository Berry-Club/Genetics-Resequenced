package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.AbstractIncubatorRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject
import java.util.function.Supplier

object ModRecipeTypes {

	val RECIPE_TYPES_REGISTRY: DeferredRegister<RecipeType<*>> =
		DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, GeneticsResequenced.ID)

	val INCUBATOR: RegistryObject<RecipeType<AbstractIncubatorRecipe>> =
		registerRecipeType("incubator")

	private fun <T : Recipe<*>> registerRecipeType(
		name: String
	): RegistryObject<RecipeType<T>> {
		return RECIPE_TYPES_REGISTRY.register(name, Supplier { object : RecipeType<T> {} })
	}


}