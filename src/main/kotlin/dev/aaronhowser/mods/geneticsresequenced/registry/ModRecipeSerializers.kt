package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeSerializers {

    val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, GeneticsResequenced.ID)

    val SET_ANTI_PLASMID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("anti_plasmid/set") { SimpleCraftingRecipeSerializer(::SetAntiPlasmidRecipe) }

    val UNSET_ANTI_PLASMID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("anti_plasmid/unset") { SimpleCraftingRecipeSerializer(::UnsetAntiPlasmidRecipe) }

    val GMO: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("incubator/gmo") { GmoRecipe.Serializer() }

    val SET_POTION_ENTITY: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("incubator/set_potion_entity") { SetPotionEntityRecipe.Serializer() }

    val DUPE_CELL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("incubator/dupe_cell") { DupeCellRecipe.Serializer() }

    val VIRUS: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("incubator/virus") { VirusRecipe.Serializer() }

    val BLACK_DEATH: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("incubator/black_death") { BlackDeathRecipe.Serializer() }

    val BASIC_INCUBATOR: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("incubator/basic") { BasicIncubatorRecipe.Serializer() }

    private fun registerRecipeSerializer(
        name: String,
        factory: () -> RecipeSerializer<*>
    ): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> {
        return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
    }

}