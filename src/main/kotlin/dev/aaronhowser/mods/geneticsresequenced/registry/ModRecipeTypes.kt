package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeTypes {

    val RECIPE_TYPES_REGISTRY: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, GeneticsResequenced.ID)

    val ANTI_PLASMID_RECIPE_TYPE: DeferredHolder<RecipeType<*>, RecipeType<*>> =
        registerRecipeType("set_anti_plasmid") { object : RecipeType<SetAntiPlasmidRecipe> {} }

    val UNSET_ANTI_PLASMID_RECIPE_TYPE: DeferredHolder<RecipeType<*>, RecipeType<*>> =
        registerRecipeType("unset_anti_plasmid") { object : RecipeType<UnsetAntiPlasmidRecipe> {} }

    val GMO: DeferredHolder<RecipeType<*>, RecipeType<*>> =
        registerRecipeType("gmo") { object : RecipeType<SetAntiPlasmidRecipe> {} }

    val SET_POTION_ENTITY: DeferredHolder<RecipeType<*>, RecipeType<*>> =
        registerRecipeType("set_potion_entity") { object : RecipeType<SetAntiPlasmidRecipe> {} }

    val SUBSTRATE_CELL: DeferredHolder<RecipeType<*>, RecipeType<*>> =
        registerRecipeType("substrate_cell") { object : RecipeType<SetAntiPlasmidRecipe> {} }

    private fun registerRecipeType(
        name: String,
        factory: () -> RecipeType<*>
    ): DeferredHolder<RecipeType<*>, RecipeType<*>> {
        return RECIPE_TYPES_REGISTRY.register(name, factory)
    }


}