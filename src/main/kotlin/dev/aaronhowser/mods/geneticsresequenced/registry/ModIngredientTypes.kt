package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.PotionTagIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModIngredientTypes {

    val INGREDIENT_TYPE_REGISTRY: DeferredRegister<IngredientType<*>> =
        DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, GeneticsResequenced.ID)

    val POTION_TAG: DeferredHolder<IngredientType<*>, IngredientType<PotionTagIngredient>> =
        INGREDIENT_TYPE_REGISTRY.register("potion_tag", Supplier { IngredientType(PotionTagIngredient.CODEC) })

}