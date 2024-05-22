package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionUtils
import net.minecraft.world.item.alchemy.Potions
import net.minecraftforge.common.brewing.IBrewingRecipe

class ComplexBrewingRecipe : IBrewingRecipe {

    private val inputPotion: Potion
    private val ingredient: Item
    private val output: ItemStack

    private var inputGene: Gene? = null
    private var inputCellType: String = ""

    constructor(
        inputPotion: Potion,
        ingredientItem: Item,
        gene: Gene?,
        outputStack: ItemStack
    ) {
        this.inputPotion = inputPotion
        this.ingredient = ingredientItem
        this.inputGene = gene
        this.output = outputStack
    }

    // Holy fucking shit
    override fun isInput(pInput: ItemStack): Boolean {

        val pInputPotion = PotionUtils.getPotion(pInput)

        if (this.inputPotion != Potions.EMPTY && pInputPotion == this.inputPotion) {

            var match: Any? = null

            if (pInput.`is`(ModItems.CELL.get())) {
                match = EntityDnaItem.getEntityType(pInput)
            } else if (pInput.`is`(ModItems.DNA_HELIX.get())) {
                match = pInput.getGene()
            } else if (pInputPotion != Potions.EMPTY) {
                match = pInput.getGene()
            }

            if (pInputPotion == ModPotions.SUBSTRATE) return true

            if (this.inputGene != null) {
                val pInputIsViral = pInputPotion == ModPotions.VIRAL_AGENTS
                val pInputHasGene = pInput.getGene() != null

                if (pInputIsViral && pInputHasGene) {
                    return true
                }

                return this.inputGene == match
            }

            if (this.inputCellType.isEmpty()) {

                when (pInputPotion) {
                    ModPotions.CELL_GROWTH -> {
                        if (pInput.getOrCreateTag().contains("cellType")) {
                            return true
                        }
                    }

                    ModPotions.MUTATION -> {
                        if (pInput.getOrCreateTag().contains("cellType")) {
                            return true
                        }
                    }

                    Potions.MUNDANE -> return true
                }

            } else {
                if (pInputPotion == ModPotions.VIRAL_AGENTS) return true
                if (this.inputCellType == match) return true
                if (this.inputCellType == "*") return true
            }
        }

        return false
    }

    override fun isIngredient(p0: ItemStack): Boolean {
        TODO("Not yet implemented")
    }

    override fun getOutput(p0: ItemStack, p1: ItemStack): ItemStack {
        TODO("Not yet implemented")
    }
}