package dev.aaronhowser.mods.genetics_resequenced.registry

import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.recipe.base.PotionTagIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModIngredientTypes {

	val INGREDIENT_TYPE_REGISTRY: DeferredRegister<IngredientType<*>> =
		DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, GeneticsResequenced.MOD_ID)

	val POTION_TAG: DeferredHolder<IngredientType<*>, IngredientType<PotionTagIngredient>> =
		INGREDIENT_TYPE_REGISTRY.register("potion_tag", Supplier { IngredientType(PotionTagIngredient.CODEC) })

}