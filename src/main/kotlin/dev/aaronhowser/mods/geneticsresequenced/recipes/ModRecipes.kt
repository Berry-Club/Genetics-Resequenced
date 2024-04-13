package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModRecipes {

    val SERIALIZERS: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GeneticsResequenced.ID)

    val CELL_ANALYZER_SERIALIZER: RegistryObject<RecipeSerializer<CellAnalyzerRecipe>> =
        SERIALIZERS.register(CellAnalyzerRecipe.RECIPE_TYPE_NAME) { CellAnalyzerRecipe.SERIALIZER }

}