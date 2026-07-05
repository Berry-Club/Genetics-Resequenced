package dev.aaronhowser.mods.genetics_resequenced.registry

import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.genetics_resequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModRecipeSerializers {

	val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
		DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, GeneticsResequenced.MOD_ID)

	val SET_ANTI_PLASMID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<SetAntiPlasmidRecipe>> =
		registerRecipeSerializer("anti_plasmid/set", SetAntiPlasmidRecipe.CODEC, SetAntiPlasmidRecipe.STREAM_CODEC)

	val UNSET_ANTI_PLASMID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<UnsetAntiPlasmidRecipe>> =
		registerRecipeSerializer("anti_plasmid/unset", UnsetAntiPlasmidRecipe.CODEC, UnsetAntiPlasmidRecipe.STREAM_CODEC)

	val GMO: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<GmoRecipe>> =
		registerRecipeSerializer("incubator/gmo", GmoRecipe.CODEC, GmoRecipe.STREAM_CODEC)

	val SET_POTION_ENTITY: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<SetPotionEntityRecipe>> =
		registerRecipeSerializer("incubator/set_potion_entity", SetPotionEntityRecipe.CODEC, SetPotionEntityRecipe.STREAM_CODEC)

	val DUPE_CELL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<DupeCellRecipe>> =
		registerRecipeSerializer("incubator/dupe_cell", DupeCellRecipe.CODEC, DupeCellRecipe.STREAM_CODEC)

	val VIRUS: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<VirusRecipe>> =
		registerRecipeSerializer("incubator/virus", VirusRecipe.CODEC, VirusRecipe.STREAM_CODEC)

	val BLACK_DEATH: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<BlackDeathRecipe>> =
		registerRecipeSerializer("incubator/black_death", BlackDeathRecipe.CODEC, BlackDeathRecipe.STREAM_CODEC)

	val BASIC_INCUBATOR: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<BasicIncubatorRecipe>> =
		registerRecipeSerializer("incubator/basic", BasicIncubatorRecipe.CODEC, BasicIncubatorRecipe.STREAM_CODEC)

	private fun <T : Recipe<*>> registerRecipeSerializer(
		name: String,
		codec: MapCodec<T>,
		streamCodec: StreamCodec<RegistryFriendlyByteBuf, T>
	): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<T>> {
		return RECIPE_SERIALIZERS_REGISTRY.register(name, Supplier { RecipeSerializer(codec, streamCodec) })
	}

}
