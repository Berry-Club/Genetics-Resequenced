package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeTypes {

    val RECIPE_TYPES_REGISTRY: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, GeneticsResequenced.ID)

    val ANTI_PLASMID_RECIPE_TYPE: DeferredHolder<RecipeType<*>, RecipeType<*>> =
        registerRecipeType("set_anti_plasmid") { object : RecipeType<SetAntiPlasmidRecipe> {} }

    private fun registerRecipeType(
        name: String,
        factory: () -> RecipeType<*>
    ): DeferredHolder<RecipeType<*>, RecipeType<*>> {
        return RECIPE_TYPES_REGISTRY.register(name, factory)
    }


}