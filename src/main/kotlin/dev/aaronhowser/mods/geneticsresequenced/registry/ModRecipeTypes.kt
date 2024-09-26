package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.SetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.crafting.UnsetAntiPlasmidRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.incubator.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModRecipeTypes {

    val RECIPE_TYPES_REGISTRY: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, GeneticsResequenced.ID)

    val SET_ANTI_PLASMID: DeferredHolder<RecipeType<*>, RecipeType<SetAntiPlasmidRecipe>> =
        registerRecipeType("set_anti_plasmid")

    val UNSET_ANTI_PLASMID: DeferredHolder<RecipeType<*>, RecipeType<UnsetAntiPlasmidRecipe>> =
        registerRecipeType("unset_anti_plasmid")

    val GMO: DeferredHolder<RecipeType<*>, RecipeType<GmoRecipe>> =
        registerRecipeType("incubator_gmo")

    val SET_POTION_ENTITY: DeferredHolder<RecipeType<*>, RecipeType<SetPotionEntityRecipe>> =
        registerRecipeType("set_potion_entity")

    val DUPE_CELL: DeferredHolder<RecipeType<*>, RecipeType<DupeCellRecipe>> =
        registerRecipeType("dupe_cell")

    val VIRUS: DeferredHolder<RecipeType<*>, RecipeType<VirusRecipe>> =
        registerRecipeType("incubator_virus")

    val BLACK_DEATH: DeferredHolder<RecipeType<*>, RecipeType<BlackDeathRecipe>> =
        registerRecipeType("black_death")

    val BASIC_INCUBATOR: DeferredHolder<RecipeType<*>, RecipeType<BasicIncubatorRecipe>> =
        registerRecipeType("basic_incubator")

    private fun <T : Recipe<*>> registerRecipeType(
        name: String
    ): DeferredHolder<RecipeType<*>, RecipeType<T>> {
        return RECIPE_TYPES_REGISTRY.register(name, Supplier { object : RecipeType<T> {} })
    }


}