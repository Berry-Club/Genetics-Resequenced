package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.emi.recipe.machine.incubator

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.SyringeItem
import dev.aaronhowser.mods.genetics_resequenced.recipe.BrewingRecipes
import dev.aaronhowser.mods.genetics_resequenced.recipe.incubator.BlackDeathRecipe
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Cow
import net.minecraft.world.item.crafting.Ingredient

class BlackDeathEmiRecipe(
	val isMetal: Boolean
) : AbstractEmiIncubatorRecipe() {

	override val ingredient: EmiIngredient
	override val input: EmiIngredient = EmiIngredient.of(Ingredient.of(BrewingRecipes.viralAgentsPotionStack))
	override val output: EmiStack

	companion object {

		fun getAllRecipes(): List<BlackDeathEmiRecipe> {
			return listOf(
				BlackDeathEmiRecipe(isMetal = false),
				BlackDeathEmiRecipe(isMetal = true)
			)
		}

	}

	init {
		val syringeStack = if (isMetal) ModItems.METAL_SYRINGE.toStack() else ModItems.SYRINGE.toStack()

		val localPlayer = AaronClientUtil.localPlayer ?: throw IllegalStateException("Local player is null")

		val entity = if (isMetal) {
			Cow(EntityType.COW, localPlayer.level())
		} else {
			localPlayer
		}

		SyringeItem.setEntity(syringeStack, entity, setContaminated = false)

		val requiredGenes = BlackDeathRecipe.getRequiredGenes(ClientUtil.localRegistryAccess!!)
		for (gene in requiredGenes) {
			SyringeItem.addGene(syringeStack, gene)
		}

		ingredient = EmiIngredient.of(Ingredient.of(syringeStack))

		val helixStack = DnaHelixItem.getHelixStack(ModGenes.BLACK_DEATH, ClientUtil.localRegistryAccess!!)
		output = EmiStack.of(helixStack)
	}

	override val tooltips: List<Component> = listOf(
		ModRecipeLang.BLACK_DEATH.toGrayComponent()
	)

	override fun getId(): Identifier {
		val type = if (isMetal) "/metal" else ""
		return GeneticsResequenced.modId("/brewing/black_death$type")
	}
}