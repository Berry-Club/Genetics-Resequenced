package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient

class BlackDeathEmiRecipe(
    val isMetal: Boolean
) : EmiRecipe {

    companion object {
        val BACKGROUND: ResourceLocation =
            ResourceLocation.withDefaultNamespace("textures/gui/container/brewing_stand.png")
    }

    private val blazePowder: EmiStack = EmiStack.of(Items.BLAZE_POWDER)
    private val inputSyringe: EmiIngredient
    private val viralAgents: EmiIngredient = EmiIngredient.of(Ingredient.of(BrewingRecipes.viralAgentsPotionStack))
    private val blackDeathHelix: EmiStack

    init {
        val syringeStack = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

        val localPlayer = ClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")

        val entity = if (isMetal) {
            Cow(EntityType.COW, localPlayer.level())
        } else {
            localPlayer
        }

        SyringeItem.setEntity(syringeStack, entity, setContaminated = false)

        for (gene in BlackDeathRecipe.requiredGenes) {
            SyringeItem.addGene(syringeStack, gene)
        }

        inputSyringe = EmiIngredient.of(Ingredient.of(syringeStack))

        val helixStack = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGene(helixStack, ModGenes.BLACK_DEATH)
        blackDeathHelix = EmiStack.of(helixStack)
    }

    override fun getCategory(): EmiRecipeCategory {
        return VanillaEmiRecipeCategories.BREWING
    }

    override fun getId(): ResourceLocation {
        val type = if (isMetal) "/metal" else ""
        return OtherUtil.modResource("/brewing/black_death$type")
    }

    override fun getInputs(): List<EmiIngredient> {
        return listOf(inputSyringe, viralAgents)
    }

    override fun getOutputs(): List<EmiStack> {
        return listOf(blackDeathHelix)
    }

    override fun getDisplayWidth(): Int {
        return 120
    }

    override fun getDisplayHeight(): Int {
        return 61
    }

    override fun addWidgets(widgets: WidgetHolder) {

        widgets.addTexture(BACKGROUND, 0, 0, 103, 61, 16, 14)
        widgets.addAnimatedTexture(BACKGROUND, 81, 2, 9, 28, 176, 0, 1000 * 20, false, false, false)
        widgets.addAnimatedTexture(BACKGROUND, 47, 0, 12, 29, 185, 0, 700, false, true, false)
        widgets.addTexture(BACKGROUND, 44, 30, 18, 4, 176, 29);
        widgets.addSlot(blazePowder, 0, 2).drawBack(false)
        widgets.addSlot(viralAgents, 39, 36).drawBack(false)
        widgets.addSlot(inputSyringe, 62, 2).drawBack(false)
        widgets.addSlot(blackDeathHelix, 85, 36).drawBack(false).recipeContext(this)

    }
}