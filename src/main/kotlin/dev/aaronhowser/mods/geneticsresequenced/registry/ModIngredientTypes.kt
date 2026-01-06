package dev.aaronhowser.mods.geneticsresequenced.registry

import dev.aaronhowser.mods.geneticsresequenced.GeneticsResequenced
import dev.aaronhowser.mods.geneticsresequenced.recipe.base.PotionTagIngredient
import net.minecraftforge.registries.DeferredRegister
import java.util.function.Supplier

object ModIngredientTypes {

	val INGREDIENT_TYPE_REGISTRY: DeferredRegister<IngredientType<*>> =
		DeferredRegister.create(NeoForgeRegistries.INGREDIENT_TYPES, GeneticsResequenced.MOD_ID)

	val POTION_TAG: DeferredHolder<IngredientType<*>, IngredientType<PotionTagIngredient>> =
		INGREDIENT_TYPE_REGISTRY.register("potion_tag", Supplier { IngredientType(PotionTagIngredient.CODEC) })

}