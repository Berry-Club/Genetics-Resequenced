package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipes.crafting.AntiPlasmidRecipe
import net.minecraft.world.item.crafting.RecipeType
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModRecipeTypes {

    val RECIPE_TYPE_REGISTRY: DeferredRegister<RecipeType<*>> =
        DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, GeneticsResequenced.ID)

    val SET_ANTI_PLASMID_RECIPE_TYPE: RegistryObject<RecipeType<AntiPlasmidRecipe>> =
        RECIPE_TYPE_REGISTRY.register(AntiPlasmidRecipe.RECIPE_TYPE_NAME) { AntiPlasmidRecipe.RECIPE_TYPE }

}