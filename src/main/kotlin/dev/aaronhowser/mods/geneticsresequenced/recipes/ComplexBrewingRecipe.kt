package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.hasGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.potions.ModPotions
import net.minecraft.world.entity.EntityType
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
    private var ingredientCellEntity: EntityType<*>? = null

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

    constructor(
        inputPotion: Potion,
        ingredientItem: Item,
        cellEntity: EntityType<*>,
        geneOutput: Gene
    ) {
        this.inputPotion = inputPotion
        this.ingredient = ingredientItem
        this.ingredientCellEntity = cellEntity
        this.inputGene = geneOutput

        val outputCell = ItemStack(ModItems.CELL.get())
        EntityDnaItem.setMob(outputCell, cellEntity)
        this.output = outputCell
    }

    //Input = the 3 potions at the bottom
    override fun isInput(pInput: ItemStack): Boolean {
        val pInputPotion = PotionUtils.getPotion(pInput)

        if (inputPotion == Potions.EMPTY || inputPotion == Potions.EMPTY || pInputPotion != inputPotion) return false

        if (pInputPotion == ModPotions.SUBSTRATE) return true

        /**
         * If pInput is a filled Cell, match is the EntityType of the Cell.
         * If pInput is a DNA Helix, match is the Gene of the Helix.
         * If pInput is a potion, match might be its Gene or EntityType, if it has one
         */
        var match: Any? = null

        if (pInput.item == ModItems.CELL.get() && EntityDnaItem.hasEntity(pInput)) {
            match = EntityDnaItem.getEntityType(pInput)
        } else if (pInput.item == ModItems.DNA_HELIX.get() && pInput.hasGene()) {
            match = pInput.getGene()
        } else if (pInputPotion != Potions.EMPTY) {

            if (pInput.hasGene()) {
                match = pInput.getGene()
            } else if (EntityDnaItem.hasEntity(pInput)) {
                match = EntityDnaItem.getEntityType(pInput)
            }

        }

        if (this.inputGene != null) {
            val pInputIsViral = pInputPotion == ModPotions.VIRAL_AGENTS
            val pInputHasNoGenes = !pInput.hasGene()
            if (pInputIsViral && pInputHasNoGenes) return true

            if (pInput.getGene() == match) return true
        }

        if (this.ingredientCellEntity == null) {
            val pPotionStackEntity = EntityDnaItem.getEntityType(pInput)
            val pPotionHasEntity = pPotionStackEntity != null

            if (pInputPotion == ModPotions.CELL_GROWTH && pPotionHasEntity) return true
            if (pInputPotion == ModPotions.MUTATION && pPotionHasEntity) return true
            if (pInputPotion == Potions.MUNDANE) return true
        }

        if (pInputPotion == ModPotions.VIRAL_AGENTS) return true

        return false
    }

    private val requiredGenes = setOf(
        DefaultGenes.CURSED, DefaultGenes.POISON_4, DefaultGenes.WITHER, DefaultGenes.WEAKNESS,
        DefaultGenes.BLINDNESS, DefaultGenes.SLOWNESS_6, DefaultGenes.NAUSEA, DefaultGenes.HUNGER,
        DefaultGenes.FLAMBE, DefaultGenes.MINING_WEAKNESS, DefaultGenes.LEVITATION,
        DefaultGenes.DEAD_CREEPERS, DefaultGenes.DEAD_UNDEAD, DefaultGenes.DEAD_HOSTILE, DefaultGenes.DEAD_OLD_AGE
    )

    override fun isIngredient(pIngredient: ItemStack): Boolean {
        return pIngredient.item == ingredient
    }

    override fun getOutput(pInput: ItemStack, pIngredient: ItemStack): ItemStack {
        return output
    }
}