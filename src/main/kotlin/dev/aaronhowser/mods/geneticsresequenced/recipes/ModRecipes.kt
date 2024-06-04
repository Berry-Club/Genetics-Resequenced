package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModRecipes {

    val RECIPE_TYPES_REGISTRY: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, GeneticsResequenced.ID)

    val ANTI_PLASMID_RECIPE_TYPE: RegistryObject<RecipeType<*>> =
        registerRecipeType("set_anti_plasmid") { object : RecipeType<AntiPlasmidRecipe> {} }

    private fun registerRecipeType(name: String, factory: () -> RecipeType<*>): RegistryObject<RecipeType<*>> {
        return RECIPE_TYPES_REGISTRY.register(name, factory)
    }
}