package dev.aaronhowser.mods.geneticsresequenced.recipes.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraftforge.common.brewing.IBrewingRecipe

class CellGrowthRecipe(
    private val entityType: EntityType<*>,
    private val ingredientItem: Item,
    private val outputGene: Gene,
    private val geneChance: Float
) : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val inputPotion = PotionUtils.getPotion(pBottomSlot)
        if (inputPotion != ModPotions.CELL_GROWTH) return false

        return EntityDnaItem.getEntityType(pBottomSlot) == entityType
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        return pTopSlot.item == ingredientItem
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val output = ModItems.GMO_CELL.get().defaultInstance

        GmoCell.setDetails(
            output,
            entityType,
            outputGene,
            geneChance
        )

        return output
    }

}