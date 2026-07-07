package dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.emi.recipe.machine

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.genetics_resequenced.GeneticsResequenced
import dev.aaronhowser.mods.genetics_resequenced.compatibility.recipe.emi.ModEmiPlugin
import dev.aaronhowser.mods.genetics_resequenced.datagen.lang.ModRecipeLang
import dev.aaronhowser.mods.genetics_resequenced.gene.Gene
import dev.aaronhowser.mods.genetics_resequenced.item.DnaHelixItem
import dev.aaronhowser.mods.genetics_resequenced.item.PlasmidItem
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes
import dev.aaronhowser.mods.genetics_resequenced.registry.ModGenes.getHolderOrThrow
import dev.aaronhowser.mods.genetics_resequenced.registry.ModItems
import dev.aaronhowser.mods.genetics_resequenced.util.ClientUtil
import dev.emi.emi.api.recipe.EmiRecipe
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient

class PlasmidInfuserEmiRecipe(
	val geneHolder: Holder<Gene>,
	val basic: Boolean
) : EmiRecipe {

	companion object {
		fun getAllRecipes(): List<PlasmidInfuserEmiRecipe> {
			val recipes = mutableListOf<PlasmidInfuserEmiRecipe>()

			for (geneHolder in ModGenes
				.getRegistrySorted(ClientUtil.localRegistryAccess!!, includeHelixOnly = false)
			) {
				recipes.add(PlasmidInfuserEmiRecipe(geneHolder, basic = true))
				recipes.add(PlasmidInfuserEmiRecipe(geneHolder, basic = false))
			}

			return recipes.distinctBy(PlasmidInfuserEmiRecipe::getId)
		}
	}

	private val helix: EmiIngredient
	private val plasmid: EmiStack

	init {
		val stackSize = maxOf(
			1, if (basic) {
				geneHolder.value().dnaPointsRequired
			} else {
				geneHolder.value().dnaPointsRequired / 2
			}
		)

		val helixStack = DnaHelixItem.getHelixStack(
			if (basic) {
				ModGenes.BASIC.getHolderOrThrow(ClientUtil.localRegistryAccess!!)
			} else {
				geneHolder
			}
		)

		helix = EmiIngredient.of(Ingredient.of(helixStack))

		val plasmidStack = ModItems.PLASMID.toStack()
		PlasmidItem.setGene(plasmidStack, geneHolder, geneHolder.value().dnaPointsRequired)
		plasmid = EmiStack.of(plasmidStack)
	}

	override fun getCategory(): EmiRecipeCategory {
		return ModEmiPlugin.PLASMID_INFUSER_CATEGORY
	}

	override fun getId(): Identifier {
		val geneString = geneHolder.key!!.identifier().toString().replace(':', '/')
		val basicString = if (basic) "/basic" else ""

		return GeneticsResequenced.modId("/plasmid_infuser/$geneString$basicString")
	}

	override fun getInputs(): List<EmiIngredient> {
		return listOf(helix)
	}

	override fun getOutputs(): List<EmiStack> {
		return listOf(plasmid)
	}

	override fun getDisplayWidth(): Int {
		return 76
	}

	override fun getDisplayHeight(): Int {
		return 18
	}

	private val tooltips: List<Component> = listOf(
		ModRecipeLang.REQUIRES_POINTS
			.toGrayComponent(
				Gene
					.getNameComponent(geneHolder)
					.withStyle(ChatFormatting.GRAY),
				geneHolder
					.value()
					.dnaPointsRequired
			),
		ModRecipeLang.BASIC_WORTH.toGrayComponent(),
		ModRecipeLang.MATCHING_WORTH.toGrayComponent()
	)

	override fun addWidgets(widgets: WidgetHolder) {
		widgets.addTexture(EmiTexture.EMPTY_ARROW, 26, 1)
		widgets.addSlot(helix, 0, 0)
		widgets.addSlot(plasmid, 58, 0).recipeContext(this)

		widgets.addTooltipText(tooltips, 0, 0, 76, 18)
	}
}