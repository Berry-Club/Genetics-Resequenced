package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.GmoItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraftforge.common.brewing.IBrewingRecipe

class GmoRecipe(
    val entityType: EntityType<*>,
    val ingredientItem: Item,
    val outputGene: Gene,
    val geneChance: Float,
    isMutation: Boolean = false
) : IBrewingRecipe {

    val requiredPotion = if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val inputPotion = PotionUtils.getPotion(pBottomSlot)
        if (inputPotion != requiredPotion) return false

        return EntityDnaItem.getEntityType(pBottomSlot) == entityType
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        return pTopSlot.item == ingredientItem
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val output = ModItems.GMO_CELL.get().itemStack

        GmoItem.setDetails(
            output,
            entityType,
            outputGene,
            geneChance
        )

        return output
    }

}