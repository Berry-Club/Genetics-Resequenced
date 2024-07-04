package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.resources.ResourceLocation

class GmoPotion(
    val gene: Gene
) : AbstractEmiBrewingRecipe() {

    override val ingredient: EmiIngredient
        get() = TODO("Not yet implemented")
    override val input: EmiIngredient
        get() = TODO("Not yet implemented")
    override val output: EmiStack
        get() = TODO("Not yet implemented")

    override fun getId(): ResourceLocation? {
        TODO("Not yet implemented")
    }

}