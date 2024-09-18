package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.item.GmoCell
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.brewing.IBrewingRecipe
import net.neoforged.neoforge.registries.DeferredHolder

class GmoRecipe(
    val entityType: EntityType<*>,
    val ingredientItem: Item,
    val idealGeneHolder: ResourceKey<Gene>,
    val geneChance: Float,
    val isMutation: Boolean = false
) : IBrewingRecipe {

    val requiredPotion = if (isMutation) ModPotions.MUTATION else ModPotions.CELL_GROWTH

    fun getSuccess(): ItemStack {
        val output = ModItems.GMO_CELL.toStack()

        GmoCell.setDetails(
            output,
            entityType,
            idealGeneHolder
        )

        return output
    }

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val inputPotion = OtherUtil.getPotion(pBottomSlot) ?: return false
        if (inputPotion != requiredPotion) return false

        return EntityDnaItem.getEntityType(pBottomSlot) == entityType
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        return pTopSlot.item == ingredientItem
    }

    /**
     * Always returns a Basic Gene! You can only get the ideal gene if you run it through an Advanced Incubator at low temperature!
     * [dev.aaronhowser.mods.geneticsresequenced.block.machine.incubator_advanced.AdvancedIncubatorBlockEntity.craftItem]
     */
    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val output = ModItems.GMO_CELL.toStack()

        GmoCell.setDetails(
            output,
            entityType,
            DeferredHolder.create(ModGenes.BASIC)
        )

        return output
    }

}