package dev.aaronhowser.mods.geneticsresequenced.datagen

import com.google.common.collect.Sets
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike

class NoAdvancementShapedRecipeBuilder(
    pResult: ItemLike,
    pCount: Int
) : ShapedRecipeBuilder(pResult, pCount) {

    override fun define(pSymbol: Char, pIngredient: Ingredient): NoAdvancementShapedRecipeBuilder {
        return super.define(pSymbol, pIngredient) as NoAdvancementShapedRecipeBuilder
    }

    override fun define(pSymbol: Char, pItem: ItemLike): NoAdvancementShapedRecipeBuilder {
        return super.define(pSymbol, pItem) as NoAdvancementShapedRecipeBuilder
    }

    override fun define(pSymbol: Char, pTag: TagKey<Item>): NoAdvancementShapedRecipeBuilder {
        return super.define(pSymbol, pTag) as NoAdvancementShapedRecipeBuilder
    }

    override fun pattern(pPattern: String): NoAdvancementShapedRecipeBuilder {
        return super.pattern(pPattern) as NoAdvancementShapedRecipeBuilder
    }

    /**@see ShapedRecipeBuilder.ensureValid*/
    override fun ensureValid(pId: ResourceLocation) {
        if (this.rows.isEmpty()) {
            throw IllegalStateException("No pattern is defined for shaped recipe $pId!")
        }

        val val1 = Sets.newHashSet(this.key.keys)
        val1.remove(' ')
        val var3 = this.rows.iterator()

        while (var3.hasNext()) {
            val val2: String = var3.next()

            for (char: Char in val2) {
                if (!this.key.containsKey(char) && char != ' ') {
                    throw IllegalStateException("Pattern in recipe $pId references undefined symbol '$char'")
                }

                val1.remove(char)
            }
        }

        if (val1.isNotEmpty()) {
            throw IllegalStateException("Ingredients are defined but not used in pattern for recipe $pId")
        } else if (this.rows.size == 1 && this.rows.first().length == 1) {
            throw IllegalStateException("Shaped recipe $pId only takes in a single item - should be a shapeless recipe instead!")
        } // There would normally a requirement that the recipe needs an advancement here. This was removed.

    }

}