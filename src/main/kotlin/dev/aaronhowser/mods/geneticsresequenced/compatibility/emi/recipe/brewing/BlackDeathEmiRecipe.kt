package dev.aaronhowser.mods.geneticsresequenced.compatibility.emi.recipe.brewing

import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider
import dev.aaronhowser.mods.geneticsresequenced.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.geneticsresequenced.item.DnaHelixItem
import dev.aaronhowser.mods.geneticsresequenced.item.SyringeItem
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BlackDeathRecipe
import dev.aaronhowser.mods.geneticsresequenced.recipe.brewing.BrewingRecipes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModGenes
import dev.aaronhowser.mods.geneticsresequenced.registry.ModItems
import dev.aaronhowser.mods.geneticsresequenced.util.ClientUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil
import dev.aaronhowser.mods.geneticsresequenced.util.OtherUtil.withColor
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.component.ItemLore
import net.minecraft.world.item.crafting.Ingredient

class BlackDeathEmiRecipe(
    val isMetal: Boolean
) : AbstractEmiBrewingRecipe() {

    override val ingredient: EmiIngredient
    override val input: EmiIngredient = EmiIngredient.of(Ingredient.of(BrewingRecipes.viralAgentsPotionStack))
    override val output: EmiStack

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

        val loreComponent = ItemLore(
            listOf(
                Component.literal(""),
                ModLanguageProvider.Recipe.BLACK_DEATH.toComponent().withColor(ChatFormatting.GRAY)
            )
        )
        syringeStack.set(DataComponents.LORE, loreComponent)

        ingredient = EmiIngredient.of(Ingredient.of(syringeStack))

        val helixStack = ModItems.DNA_HELIX.toStack()
        DnaHelixItem.setGene(helixStack, ModGenes.BLACK_DEATH)
        output = EmiStack.of(helixStack)
    }

    override fun getId(): ResourceLocation {
        val type = if (isMetal) "/metal" else ""
        return OtherUtil.modResource("/brewing/black_death$type")
    }
}