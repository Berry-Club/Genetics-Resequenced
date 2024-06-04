package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.setGene
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.PlasmidItem
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.itemStack
import net.minecraft.world.inventory.CraftingContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CustomRecipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.item.crafting.SimpleRecipeSerializer
import net.minecraft.world.level.Level

class AntiPlasmidRecipe : CustomRecipe(
    OtherUtil.modResource("set_anti_plasmid")
) {

    override fun matches(inventory: CraftingContainer, level: Level): Boolean {
        val amountPlasmids = inventory.countItem(ModItems.PLASMID.get())
        val amountAntiPlasmids = inventory.countItem(ModItems.ANTI_PLASMID.get())

        if (amountPlasmids != 1 || amountAntiPlasmids != 1) return false

        var hasValidPlasmid = false
        var hasValidAntiPlasmid = false

        for (i in 0 until inventory.containerSize) {
            val stack = inventory.getItem(i)
            if (stack.isEmpty) continue

            val item = stack.item

            if (item == ModItems.PLASMID.get()) {
                val plasmidIsComplete = PlasmidItem.isComplete(stack)
                if (plasmidIsComplete) hasValidPlasmid = true
            }

            if (item == ModItems.ANTI_PLASMID.get()) {
                val antiPlasmidHasGene = stack.hasGene()
                if (!antiPlasmidHasGene) hasValidAntiPlasmid = true
            }
        }

        return hasValidPlasmid && hasValidAntiPlasmid

    }

    override fun assemble(inventory: CraftingContainer): ItemStack {

        var plasmidStack: ItemStack? = null

        for (i in 0 until inventory.containerSize) {
            val stack = inventory.getItem(i)
            if (stack.item == ModItems.PLASMID.get()) {
                plasmidStack = stack
                break
            }
        }
        if (plasmidStack == null) return ItemStack.EMPTY

        val plasmidGene = plasmidStack.getGene() ?: return ItemStack.EMPTY

        val antiPlasmidStack = ModItems.PLASMID.itemStack
        antiPlasmidStack.setGene(plasmidGene)

        return antiPlasmidStack
    }

    override fun canCraftInDimensions(width: Int, height: Int): Boolean {
        return width * height >= 2
    }

    override fun getSerializer(): RecipeSerializer<*> = SimpleRecipeSerializer { AntiPlasmidRecipe() }

}