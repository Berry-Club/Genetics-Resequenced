package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModRecipes {

    val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, GeneticsResequenced.ID)

    val CELL_ANALYZER_SERIALIZER: RegistryObject<RecipeSerializer<CellAnalyzerRecipe>> =
        RECIPE_SERIALIZERS_REGISTRY.register(CellAnalyzerRecipe.RECIPE_TYPE_NAME) { CellAnalyzerRecipe.SERIALIZER }

    val MOB_TO_GENE_SERIALIZER: RegistryObject<RecipeSerializer<MobToGeneRecipe>> =
        RECIPE_SERIALIZERS_REGISTRY.register(MobToGeneRecipe.RECIPE_TYPE_NAME) { MobToGeneRecipe.SERIALIZER }

    val PLASMID_SERIALIZER: RegistryObject<RecipeSerializer<PlasmidInfuserRecipe>> =
        RECIPE_SERIALIZERS_REGISTRY.register(PlasmidInfuserRecipe.RECIPE_TYPE_NAME) { PlasmidInfuserRecipe.SERIALIZER }

}