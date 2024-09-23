package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.low_temp.GmoRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeSerializers {

    val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, GeneticsResequenced.ID)

    val SET_ANTI_PLASMID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("set_anti_plasmid") { SimpleCraftingRecipeSerializer { SetAntiPlasmidRecipe() } }

    val UNSET_ANTI_PLASMID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
        registerRecipeSerializer("unset_anti_plasmid") { SimpleCraftingRecipeSerializer { UnsetAntiPlasmidRecipe() } }

    val GMO_RECIPE = registerRecipeSerializer("gmo") { GmoRecipe.Serializer() }

    private fun registerRecipeSerializer(
        name: String,
        factory: () -> RecipeSerializer<*>
    ): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> {
        return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
    }

}