package dev.aaronhowser.mods.geneticsresequenced.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.api.genes.GeneRegistry
import dev.aaronhowser.mods.geneticsresequenced.gene.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.registry.ModPotions
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.brewing.IBrewingRecipe
import kotlin.jvm.optionals.getOrNull

class BlackDeathRecipe : IBrewingRecipe {

    override fun isInput(pBottomSlot: ItemStack): Boolean {
        if (pBottomSlot.item != Items.POTION) return false

        val potionContents = pBottomSlot.get(DataComponents.POTION_CONTENTS) ?: return false
        val inputPotion = potionContents.potion.getOrNull()?.value()

        return inputPotion == ModPotions.VIRAL_AGENTS.get()
    }

    companion object {
        val requiredGeneHolders =
            GeneRegistry.getRegistrySorted(ClientUtil.localRegistryAccess!!)
                .filter { it.value().isNegative && it.value().isActive && !it.value().isHidden }
        //TODO - ModGenes.BLACK_DEATH
    }

    override fun isIngredient(pTopSlot: ItemStack): Boolean {
        val item = pTopSlot.item
        if (item != ModItems.SYRINGE.get() && item != ModItems.METAL_SYRINGE.get()) return false

        if (SyringeItem.isContaminated(pTopSlot)) return false

        val syringeGenes = SyringeItem.getGenes(pTopSlot)
        return syringeGenes.containsAll(requiredGeneHolders)
    }

    override fun getOutput(pBottomSlot: ItemStack, pTopSlot: ItemStack): ItemStack {
        if (!isInput(pBottomSlot)) return ItemStack.EMPTY
        if (!isIngredient(pTopSlot)) return ItemStack.EMPTY

        val output = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGeneRk(output, ModGenes.BLACK_DEATH, ClientUtil.localRegistryAccess!!)

        return output
    }

}