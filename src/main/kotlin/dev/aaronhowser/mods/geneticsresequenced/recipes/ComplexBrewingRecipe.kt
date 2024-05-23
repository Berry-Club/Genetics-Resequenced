package dev.aaronhowser.mods.geneticsresequenced.recipes

import dev.aaronhowser.mods.geneticsresequenced.api.capability.genes.Gene
import dev.aaronhowser.mods.geneticsresequenced.default_genes.DefaultGenes
import dev.aaronhowser.mods.geneticsresequenced.items.DnaHelixItem.Companion.getGene
import dev.aaronhowser.mods.geneticsresequenced.items.EntityDnaItem
import dev.aaronhowser.mods.geneticsresequenced.items.ModItems
import dev.aaronhowser.mods.geneticsresequenced.items.SyringeItem
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
    private var inputCellEntity: EntityType<*>? = null

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
        this.inputCellEntity = cellEntity
        this.inputGene = geneOutput

        val outputCell = ItemStack(ModItems.CELL.get())
        EntityDnaItem.setMob(outputCell, cellEntity)
        this.output = outputCell
    }

    // Holy fucking shit
    override fun isInput(pInput: ItemStack): Boolean {

        val pInputPotion = PotionUtils.getPotion(pInput)

        if (this.inputPotion != Potions.EMPTY && pInputPotion == this.inputPotion) {

            var match: Any? = null

            if (pInput.item == ModItems.CELL.get()) {
                match = EntityDnaItem.getEntityType(pInput)
            } else if (pInput.item == ModItems.DNA_HELIX.get()) {
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

            if (this.inputCellEntity != null) {

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
                if (this.inputCellEntity == match) return true
//                if (this.inputCellEntity == "*") return true
            }
        }

        return false
    }

    private val requiredGenes = setOf(
        DefaultGenes.CURSED, DefaultGenes.POISON_4, DefaultGenes.WITHER, DefaultGenes.WEAKNESS,
        DefaultGenes.BLINDNESS, DefaultGenes.SLOWNESS_6, DefaultGenes.NAUSEA, DefaultGenes.HUNGER,
        DefaultGenes.FLAMBE, DefaultGenes.MINING_WEAKNESS, DefaultGenes.LEVITATION,
        DefaultGenes.DEAD_CREEPERS, DefaultGenes.DEAD_UNDEAD, DefaultGenes.DEAD_HOSTILE, DefaultGenes.DEAD_OLD_AGE
    )

    override fun isIngredient(pIngredient: ItemStack): Boolean {
        if (this.ingredient == ModItems.SYRINGE.get() && pIngredient.item == ModItems.SYRINGE.get()) {
            //Special case - Black Death
            val genes = SyringeItem.getGenes(pIngredient)
            return requiredGenes.all { it in genes }
        }

        if (inputGene != null) {
            return pIngredient.getGene() == inputGene
        }

        if (inputCellEntity != null) {
            if (pIngredient.item == ModItems.DNA_HELIX.get()) {
                return pIngredient.getGene() == inputGene
            }
        }

        return pIngredient.item == this.ingredient
    }

    override fun getOutput(pInput: ItemStack, pIngredient: ItemStack): ItemStack {

        if (!isInput(pInput) || !isIngredient(pIngredient)) {
            return ItemStack.EMPTY
        }

        val result = output.copy()

        //this handles cell copying
//        if (pIngredient.hasTagCompound() && recipeOutput.getItem() === GRItems.Cell) {
//            result.setTagCompound(pIngredient.getTagCompound())
//            result.getTagCompound().removeTag("forceGene")
//            result.getTagCompound().removeTag("chance")
//            return result
//        }

        val pInputPotion = PotionUtils.getPotion(pInput)

        //Cell focus/mutation
        if (this.output.item == ModItems.CELL.get() && pInputPotion in setOf(
                ModPotions.CELL_GROWTH,
                ModPotions.MUTATION
            )
        ) {
            val pInputEntity = EntityDnaItem.getEntityType(pInput) ?: return ItemStack.EMPTY
            EntityDnaItem.setMob(result, pInputEntity)

            return result
        }

        val pIngredientHasEntity =
            pIngredient.item == ModItems.CELL.get() && EntityDnaItem.getEntityType(pIngredient) != null
        if (pIngredientHasEntity && pInputPotion in setOf(ModPotions.CELL_GROWTH, ModPotions.MUTATION)) {
            val pIngredientEntity = EntityDnaItem.getEntityType(pIngredient) ?: return ItemStack.EMPTY
            EntityDnaItem.setMob(result, pIngredientEntity)

            return result
        }

        return result
    }
}