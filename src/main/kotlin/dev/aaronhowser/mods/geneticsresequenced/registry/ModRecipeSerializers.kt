package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.*
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModRecipeSerializers {

	val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
		DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GeneticsResequenced.MOD_ID)

	val SET_ANTI_PLASMID: RegistryObject<RecipeSerializer<SetAntiPlasmidRecipe>> =
		registerRecipeSerializer("anti_plasmid/set") { SimpleCraftingRecipeSerializer(::SetAntiPlasmidRecipe) }

	val UNSET_ANTI_PLASMID: RegistryObject<RecipeSerializer<UnsetAntiPlasmidRecipe>> =
		registerRecipeSerializer("anti_plasmid/unset") { SimpleCraftingRecipeSerializer(::UnsetAntiPlasmidRecipe) }

	val GMO: RegistryObject<RecipeSerializer<GmoRecipe>> =
		registerRecipeSerializer("incubator/gmo") { GmoRecipe.Serializer() }

	val SET_POTION_ENTITY: RegistryObject<RecipeSerializer<SetPotionEntityRecipe>> =
		registerRecipeSerializer("incubator/set_potion_entity") { SetPotionEntityRecipe.Serializer() }

	val DUPE_CELL: RegistryObject<RecipeSerializer<DupeCellRecipe>> =
		registerRecipeSerializer("incubator/dupe_cell") { DupeCellRecipe.Serializer() }

	val VIRUS: RegistryObject<RecipeSerializer<VirusRecipe>> =
		registerRecipeSerializer("incubator/virus") { VirusRecipe.Serializer() }

	val BLACK_DEATH: RegistryObject<RecipeSerializer<BlackDeathRecipe>> =
		registerRecipeSerializer("incubator/black_death") { BlackDeathRecipe.Serializer() }

	val BASIC_INCUBATOR: RegistryObject<RecipeSerializer<BasicIncubatorRecipe>> =
		registerRecipeSerializer("incubator/basic") { BasicIncubatorRecipe.Serializer() }

	private fun <T : Recipe<*>> registerRecipeSerializer(
		name: String,
		factory: () -> RecipeSerializer<T>
	): RegistryObject<RecipeSerializer<T>> {
		return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
	}

}