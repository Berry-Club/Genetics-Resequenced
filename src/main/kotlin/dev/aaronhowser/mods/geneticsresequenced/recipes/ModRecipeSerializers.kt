package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipes.machine.BloodPurifierRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SimpleRecipeSerializer
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject


object ModRecipeSerializers {

    val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GeneticsResequenced.ID)

    val ANTI_PLASMID_RECIPE_SERIALIZER: RegistryObject<RecipeSerializer<*>> =
        registerRecipeSerializer("set_anti_plasmid") { SimpleRecipeSerializer { AntiPlasmidRecipe() } }

    val BLOOD_PURIFIER_RECIPE_SERIALIZER: RegistryObject<RecipeSerializer<*>> =
        registerRecipeSerializer("blood_purifier") { SimpleRecipeSerializer { BloodPurifierRecipe() } }

    private fun registerRecipeSerializer(
        name: String,
        factory: () -> RecipeSerializer<*>
    ): RegistryObject<RecipeSerializer<*>> {
        return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
    }
}